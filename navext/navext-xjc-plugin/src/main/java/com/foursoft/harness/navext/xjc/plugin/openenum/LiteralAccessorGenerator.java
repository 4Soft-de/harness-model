/*-
 * ========================LICENSE_START=================================
 * NavExt XJC Plugin
 * %%
 * Copyright (C) 2019 - 2026 4Soft GmbH
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
package com.foursoft.harness.navext.xjc.plugin.openenum;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JForEach;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.ErrorReceiver;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Adds the typed accessors of an open enumeration property to a generated model class.
 *
 * <p>
 * The accessors are purely additive: the JAXB-mapped {@code String} property, its getter and its
 * setter are left untouched, so nothing that reads or writes XML is affected and no published
 * signature changes. They are suffixed with {@code Literal} rather than overloading the existing
 * accessors, because an overloaded {@code setX(null)} would become an ambiguous method invocation and
 * break callers.
 * </p>
 */
final class LiteralAccessorGenerator {

    private static final String LITERAL = "Literal";

    /** Name of the accessor every open enumeration literal has. */
    private static final String VALUE = "value";

    private final JCodeModel codeModel;
    private final ErrorReceiver errorReceiver;

    LiteralAccessorGenerator(final JCodeModel codeModel, final ErrorReceiver errorReceiver) {
        this.codeModel = codeModel;
        this.errorReceiver = errorReceiver;
    }

    /**
     * @param classOutline     The class declaring the property.
     * @param field            The property, which must be mapped to {@code String} or
     *                         {@code List<String>}.
     * @param elementName      The name of the XSD element the property is mapped from.
     * @param literalInterface The interface of the open enumeration the property is typed with.
     * @return {@code true} if the accessors were added.
     */
    boolean addAccessors(final ClassOutline classOutline, final FieldOutline field, final String elementName,
                         final JClass literalInterface) {
        final CPropertyInfo property = field.getPropertyInfo();
        if (!isStringTyped(field)) {
            errorReceiver.warning(property.getLocator(), String.format(
                    "Skipping the open enumeration property %s#%s: expected String or List<String> but found %s.",
                    classOutline.implClass.fullName(), property.getName(true), field.getRawType()
                            .fullName()));
            return false;
        }

        if (property.isCollection()) {
            return addCollectionAccessors(classOutline, property, elementName, literalInterface);
        }
        return addSingleAccessors(classOutline, property, literalInterface);
    }

    private boolean addSingleAccessors(final ClassOutline classOutline, final CPropertyInfo property,
                                       final JClass literalInterface) {
        final JDefinedClass implClass = classOutline.implClass;
        final String baseName = property.getName(true);

        final String plainGetterName = "get" + baseName;
        final String plainSetterName = "set" + baseName;
        if (implClass.getMethod(plainGetterName, new JType[0]) == null) {
            errorReceiver.warning(property.getLocator(), String.format(
                    "Skipping the open enumeration property %s#%s: no %s() to delegate to.",
                    implClass.fullName(), baseName, plainGetterName));
            return false;
        }

        final String getterName = plainGetterName + LITERAL;
        final String setterName = plainSetterName + LITERAL;
        if (isTaken(implClass, getterName) || isTaken(implClass, setterName)) {
            reportTaken(classOutline, getterName);
            return false;
        }

        final JMethod getter = implClass.method(JMod.PUBLIC, literalInterface, getterName);
        getter.javadoc()
                .append(String.format("Returns {@link #%s()} as a typed literal.", plainGetterName));
        getter.javadoc()
                .addReturn()
                .append("The literal, or {@code null} if the property is not set. A literal that is neither "
                                + "defined by the standard nor contributed by an {@code OpenEnumLiteralProvider} "
                                + "is returned as a {@code Custom} literal, never as {@code null}.");
        getter.body()
                ._return(literalInterface.staticInvoke("of")
                                 .arg(JExpr.invoke(plainGetterName)));

        final JMethod setter = implClass.method(JMod.PUBLIC, codeModel.VOID, setterName);
        final JVar value = setter.param(literalInterface, VALUE);
        setter.javadoc()
                .append(String.format("Sets {@link #%s(String)} to the value of the given literal.", plainSetterName));
        setter.javadoc()
                .addParam(value)
                .append("The literal to set, or {@code null} to unset the property.");
        setter.body()
                .add(JExpr.invoke(plainSetterName)
                             .arg(JOp.cond(value.eq(JExpr._null()), JExpr._null(), value.invoke(VALUE))));
        return true;
    }

    private boolean addCollectionAccessors(final ClassOutline classOutline, final CPropertyInfo property,
                                           final String elementName, final JClass literalInterface) {
        final JDefinedClass implClass = classOutline.implClass;
        final String rawGetterName = "get" + property.getName(true);
        if (implClass.getMethod(rawGetterName, new JType[0]) == null) {
            errorReceiver.warning(property.getLocator(), String.format(
                    "Skipping the open enumeration property %s#%s: no %s() to delegate to.",
                    implClass.fullName(), property.getName(true), rawGetterName));
            return false;
        }

        final String getterName = "get" + elementName + LITERAL + "s";
        final String adderName = "add" + elementName + LITERAL;
        final String setterName = "set" + elementName + LITERAL + "s";
        if (isTaken(implClass, getterName) || isTaken(implClass, adderName) || isTaken(implClass, setterName)) {
            reportTaken(classOutline, getterName);
            return false;
        }

        addCollectionGetter(implClass, literalInterface, getterName, rawGetterName, adderName);
        addCollectionAdder(implClass, literalInterface, adderName, rawGetterName);
        addCollectionSetter(implClass, literalInterface, setterName, rawGetterName);
        return true;
    }

    private void addCollectionGetter(final JDefinedClass implClass, final JClass literalInterface,
                                     final String getterName, final String rawGetterName, final String adderName) {
        final JClass literalList = codeModel.ref(List.class)
                .narrow(literalInterface);

        final JMethod getter = implClass.method(JMod.PUBLIC, literalList, getterName);
        getter.javadoc()
                .append(String.format("Returns {@link #%s()} as typed literals.", rawGetterName));
        getter.javadoc()
                .addReturn()
                .append(String.format("An unmodifiable snapshot, in the order of the underlying list. Modify the "
                                              + "property through {@link #%s()} or {@link #%s(%s)}.",
                                      rawGetterName, adderName, literalInterface.name()));

        final JBlock body = getter.body();
        final JVar literals = body.decl(literalList, "literals", JExpr._new(codeModel.ref(ArrayList.class)
                                                                                   .narrow(literalInterface)));
        final JForEach value = body.forEach(codeModel.ref(String.class), VALUE, JExpr.invoke(rawGetterName));
        value.body()
                .add(literals.invoke("add")
                             .arg(literalInterface.staticInvoke("of")
                                          .arg(value.var())));
        body._return(codeModel.ref(Collections.class)
                             .staticInvoke("unmodifiableList")
                             .arg(literals));
    }

    private void addCollectionAdder(final JDefinedClass implClass, final JClass literalInterface,
                                    final String adderName, final String rawGetterName) {
        final JMethod adder = implClass.method(JMod.PUBLIC, codeModel.VOID, adderName);
        final JVar value = adder.param(literalInterface, VALUE);
        adder.javadoc()
                .append(String.format("Adds the value of the given literal to {@link #%s()}.", rawGetterName));
        adder.javadoc()
                .addParam(value)
                .append("The literal to add. Must not be null.");

        final JBlock body = adder.body();
        body.add(codeModel.ref(Objects.class)
                         .staticInvoke("requireNonNull")
                         .arg(value)
                         .arg(JExpr.lit("The literal to add must not be null.")));
        body.add(JExpr.invoke(rawGetterName)
                         .invoke("add")
                         .arg(value.invoke(VALUE)));
    }

    private void addCollectionSetter(final JDefinedClass implClass, final JClass literalInterface,
                                     final String setterName, final String rawGetterName) {
        final JMethod setter = implClass.method(JMod.PUBLIC, codeModel.VOID, setterName);
        final JVar values = setter.param(codeModel.ref(Collection.class)
                                                 .narrow(literalInterface.wildcard()), "values");
        setter.javadoc()
                .append(String.format("Replaces the content of {@link #%s()} with the values of the given literals.",
                                      rawGetterName));
        setter.javadoc()
                .addParam(values)
                .append("The literals to set, or {@code null} to clear the property.");

        final JBlock body = setter.body();
        final JVar target = body.decl(codeModel.ref(List.class)
                                              .narrow(String.class), "target", JExpr.invoke(rawGetterName));
        body.add(target.invoke("clear"));

        final JBlock guarded = body._if(values.ne(JExpr._null()))
                ._then();
        final JForEach value = guarded.forEach(literalInterface, VALUE, values);
        value.body()
                .add(target.invoke("add")
                             .arg(value.var()
                                          .invoke(VALUE)));
    }

    private boolean isStringTyped(final FieldOutline field) {
        final JType rawType = field.getRawType();
        final JClass stringType = codeModel.ref(String.class);
        if (!field.getPropertyInfo()
                .isCollection()) {
            return stringType.equals(rawType);
        }
        return rawType instanceof final JClass rawClass
                && !rawClass.getTypeParameters()
                .isEmpty()
                && stringType.equals(rawClass.getTypeParameters()
                                             .get(0));
    }

    private static boolean isTaken(final JDefinedClass implClass, final String methodName) {
        return implClass.methods()
                .stream()
                .anyMatch(method -> method.name()
                        .equals(methodName));
    }

    private void reportTaken(final ClassOutline classOutline, final String methodName) {
        errorReceiver.warning(classOutline.target.getLocator(), String.format(
                "Skipping the open enumeration accessors of %s: %s is already defined.",
                classOutline.implClass.fullName(), methodName));
    }

}
