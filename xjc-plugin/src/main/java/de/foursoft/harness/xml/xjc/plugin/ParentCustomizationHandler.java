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
package de.foursoft.harness.xml.xjc.plugin;

import org.w3c.dom.Element;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JFieldVar;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;

import de.foursoft.harness.xml.annotations.XmlParent;

public class ParentCustomizationHandler extends AbstractCustomizationHandler {

    @Override
    protected CustomizationTags handledTag() {
        return CustomizationTags.PARENT;
    }

    @Override
    protected void handleGlobal(final Outline outline, final CPluginCustomization customization) {
        throw new RuntimeException("No global customizations allowed!");
    }

    @Override
    protected void handleField(final FieldOutline fieldOutline, final CPluginCustomization customization) {
        throw new RuntimeException(CustomizationTags.PARENT + " customization is illegal for fields.");
    }

    @Override
    protected void handleClass(final ClassOutline classOutline, final CPluginCustomization customization) {
        try {
            final JDefinedClass targetClass = classOutline.implClass;
            final JCodeModel codeModel = classOutline.parent()
                    .getCodeModel();
            final Element customizationElement = customization.element;

            final String name = customizationElement.getAttribute(Consts.ATTR_NAME);
            final JDefinedClass baseType = findBaseType(classOutline, customizationElement);

            final PropertyBuilder propertyBuilder = new PropertyBuilder(codeModel);

            final JFieldVar parentField = propertyBuilder.withName(name)
                    .withBaseType(baseType)
                    .withGetterJavadoc(createGetterJavadoc(codeModel, baseType))
                    .build(targetClass);

            parentField.annotate(codeModel.ref(XmlParent.class));

            customization.markAsAcknowledged();
        } catch (final ClassNotFoundException e) {
            new RuntimeException(e);
        }

    }

    public JDefinedClass findBaseType(final ClassOutline classOutline, final Element customizationElement) {
        if (customizationElement.hasAttribute(Consts.ATTR_SCHEMA_TYPE)) {
            final String typeName = customizationElement.getAttribute(Consts.ATTR_SCHEMA_TYPE);
            return CodeModelUtility.findSchemaType(classOutline.parent(), typeName);
        }
        if (customizationElement.hasAttribute(Consts.ATTR_SELECTOR_TYPE)) {
            return findSelectorType(classOutline.implClass,
                    customizationElement.getAttribute(Consts.ATTR_SELECTOR_TYPE));
        }
        throw new RuntimeException("Either attribute '" + Consts.ATTR_SCHEMA_TYPE + "' or '" + Consts.ATTR_SELECTOR_TYPE
                + "' must be set for tag:" + CustomizationTags.PARENT.name());

    }

    private JDefinedClass findSelectorType(final JDefinedClass targetClass, final String selectorName) {
        final JDefinedClass target = targetClass.getPackage()
                ._getClass(selectorName);
        if (target == null) {
            throw new RuntimeException(selectorName + " is not a class in this generation, so no parent can be set!");

        }
        return target;
    }

    private JDocComment createGetterJavadoc(final JCodeModel codeModel, final JDefinedClass baseType)
            throws ClassNotFoundException {
        final JDocComment comment = new JDocComment(codeModel)
                .append("Gets a reference to the parent of this object in the XML DOM Tree. ")
                .append("If this class can have different parents in DOM, this property is initialized with the parent, if the parent is a ")
                .append(baseType)
                .append(" otherwise it will be <tt>null</tt><br/>");

        return CodeModelUtility.appendGetterDisclaimer(comment, codeModel);
    }

}
