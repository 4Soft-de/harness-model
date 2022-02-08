/*-
 * ========================LICENSE_START=================================
 * xjc-plugin
 * %%
 * Copyright (C) 2019 4Soft GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
package com.foursoft.xml.xjc.plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import jakarta.xml.bind.annotation.XmlElement;

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;

import com.foursoft.xml.annotations.XmlBackReference;

public class ExtReferenceCustomizationHandler extends AbstractCustomizationHandler {

    @Override
    protected CustomizationTags handledTag() {
        return CustomizationTags.EXT_REFERENCE;
    }

    @Override
    protected void handleGlobal(final Outline outline, final CPluginCustomization customization) {
        throw new RuntimeException("No global customizations allowed!");
    }

    @Override
    protected void handleField(final FieldOutline fieldOutline, final CPluginCustomization customization) {
        final ExtReferenceFieldContext context = new ExtReferenceFieldContext(fieldOutline, customization);
        if (context.isCollection()) {
            handleTypeSafeCollection(context);
        } else {
            handleTypeSafeProperty(context);
        }

        handleInverseNavigation(context);

        customization.markAsAcknowledged();
    }

    @Override
    protected void handleClass(final ClassOutline classOutline, final CPluginCustomization customization) {
        throw new RuntimeException(CustomizationTags.EXT_REFERENCE + " customization is illegal for class-types.");
    }

    private void handleInverseNavigation(final ExtReferenceFieldContext context) {
        final Optional<String> inverseProperty = context.getInversePropertyName();
        if (!inverseProperty.isPresent()) {
            return;
        }
        final JCodeModel codeModel = context.getCodeModel();
        final JFieldVar sourceField = context.getSourceField();
        final JDefinedClass sourceClass = context.getSourceClass();
        final JDefinedClass targetClass = context.getTargetClass();
        final String inversePropertyName = inverseProperty.get();

        final PropertyBuilder propertyBuilder = new PropertyBuilder(codeModel);
        propertyBuilder.withName(inversePropertyName)
                .withBaseType(codeModel.ref(Set.class)
                        .narrow(sourceClass))
                .withInit(JExpr._new(codeModel.ref(HashSet.class)
                        .narrow(sourceClass)))
                .withGetterJavadoc(createGetterJavadoc(codeModel, sourceClass));

        findImplementationClasses(context, targetClass).filter(c -> !c.fields()
                .containsKey(inversePropertyName))
                .forEach(propertyBuilder::build);

        if (targetClass.isInterface()) {
            propertyBuilder.buildAbstract(targetClass);
        }

        final JAnnotationUse sourceAnnotation = sourceField.annotate(codeModel.ref(XmlBackReference.class));
        sourceAnnotation.param("destinationField", inversePropertyName);
    }

    private Stream<JDefinedClass> findImplementationClasses(final ExtReferenceFieldContext context,
            final JDefinedClass targetClass) {
        if (targetClass.isInterface()) {
            return toStream(context.getCodeModel()
                    .packages()).flatMap(p -> toStream(p.classes()))
                            .filter(c -> {
                                return toStream(c._implements()).anyMatch(iface -> iface == targetClass);
                            });
        }
        return Stream.of(targetClass);
    }

    private JDocComment createGetterJavadoc(final JCodeModel codeModel, final JDefinedClass sourceClass) {
        final JDocComment comment = new JDocComment(codeModel).append("Gets a ")
                .append(codeModel.ref(Set.class))
                .append(" of all ")
                .append(sourceClass)
                .append(" that have a outgoing reference to this object.<br/>");

        return CodeModelUtility.appendGetterDisclaimer(comment, codeModel);
    }

    private void handleTypeSafeCollection(final ExtReferenceFieldContext context) {
        final JCodeModel codeModel = context.getCodeModel();
        final JClass fieldType = codeModel.ref(List.class)
                .narrow(context.getTargetClass());
        final JFieldVar sourceField = context.getSourceField();
        final JDefinedClass sourceClass = context.getSourceClass();

        // Modify field type.
        sourceField.type(fieldType);
        modifyXmlElementAttribute(sourceField);
        // Modify getter-Method with List initialization.
        final JMethod originalMethod = sourceClass.getMethod(context.getGetterName(), new JType[] {});
        sourceClass.methods()
                .remove(originalMethod);

        final JMethod newMethod = sourceClass.method(JMod.PUBLIC, fieldType, context.getGetterName());

        newMethod.javadoc()
                .addAll(originalMethod.javadoc());

        newMethod.body()
                ._if(sourceField.eq(JExpr._null()))
                ._then()
                .assign(sourceField, JExpr._new(codeModel.ref(ArrayList.class)
                        .narrow(context.getTargetClass())));

        newMethod.body()
                ._return(JExpr.refthis(sourceField.name()));

    }

    private void handleTypeSafeProperty(final ExtReferenceFieldContext context) {
        final JCodeModel codeModel = context.getCodeModel();
        final JClass fieldType = context.getTargetClass();
        final JFieldVar sourceField = context.getSourceField();
        final JDefinedClass sourceClass = context.getSourceClass();
        // Modify field type.
        sourceField.type(fieldType);
        modifyXmlElementAttribute(sourceField);
        // Modify getter & setter methods
        sourceClass.getMethod(context.getGetterName(), new JType[] {})
                .type(fieldType);

        sourceClass.getMethod(context.getSetterName(), new JType[] { codeModel.ref(Object.class) })
                .params()
                .get(0)
                .type(fieldType);
    }

    private void modifyXmlElementAttribute(final JFieldVar sourceField) {
        for (final JAnnotationUse use : sourceField.annotations()) {
            if (use.getAnnotationClass()
                    .fullName()
                    .equals(XmlElement.class.getName())) {
                use.param("type", Object.class);
            }
        }
    }

    private <T> Stream<T> toStream(final Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }

}
