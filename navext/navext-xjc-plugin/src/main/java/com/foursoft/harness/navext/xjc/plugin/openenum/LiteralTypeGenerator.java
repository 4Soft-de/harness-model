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

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JEnumConstant;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JForEach;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.outline.Outline;
import org.xml.sax.SAXException;

import java.util.Map;
import java.util.Objects;

/**
 * Generates the types representing one open enumeration:
 *
 * <ul>
 *     <li>an interface, which is the type used by the generated accessors and which anybody may
 *     implement to contribute literals of their own,</li>
 *     <li>a nested {@code Custom} class of that interface, holding a literal read from a document
 *     that nothing recognizes, and</li>
 *     <li>an enum holding the literals defined by the schema.</li>
 * </ul>
 *
 * <p>
 * The enum is shaped like the enums XJC generates for closed enumerations, with one difference:
 * {@code fromValue} returns {@code null} instead of throwing, because an unknown literal is expected
 * rather than exceptional here.
 * </p>
 */
final class LiteralTypeGenerator {

    private final Outline outline;
    private final JCodeModel codeModel;
    private final OpenEnumRuntime runtime;
    private final ConstantNamer constantNamer;

    LiteralTypeGenerator(final Outline outline, final OpenEnumRuntime runtime, final ConstantNamer constantNamer) {
        this.outline = outline;
        this.codeModel = outline.getCodeModel();
        this.runtime = runtime;
        this.constantNamer = constantNamer;
    }

    /**
     * @param pkg        The package to generate into.
     * @param className  The name of the enum. The interface is named after it, suffixed with
     *                   {@code Literal}.
     * @param definition The open enumeration to generate.
     * @return The generated interface, which is the type the accessors use.
     * @throws SAXException If the types cannot be generated.
     */
    JDefinedClass generate(final JPackage pkg, final String className, final OpenEnumDefinition definition)
            throws SAXException {
        final Map<String, OpenEnumDefinition.Literal> literals = constantNamer.nameLiterals(definition);

        final JDefinedClass literalInterface = createInterface(pkg, className + "Literal", definition);
        final JDefinedClass customLiteral = createCustomLiteral(literalInterface, definition);
        final JDefinedClass literalEnum = createEnum(pkg, className, definition, literals);

        literalEnum._implements(literalInterface);
        createFactoryMethod(literalInterface, literalEnum, customLiteral, definition);

        return literalInterface;
    }

    private JDefinedClass createInterface(final JPackage pkg, final String name,
                                          final OpenEnumDefinition definition) {
        final JDefinedClass literalInterface = outline.getClassFactory()
                .createClass(pkg, name, null, ClassType.INTERFACE);
        literalInterface._implements(runtime.literal());

        appendDocumentation(literalInterface.javadoc(), definition.documentation());
        literalInterface.javadoc()
                .append(String.format("%n<p>%nA literal of the open enumeration {@code %s}. The literals defined by "
                                              + "the standard are the constants of {@link %s}; a literal read from a "
                                              + "document that is not defined there and not contributed by an "
                                              + "{@code OpenEnumLiteralProvider} is represented by a {@link Custom}.%n"
                                              + "</p>",
                                      definition.typeName()
                                              .getLocalPart(),
                                      name.substring(0, name.length() - "Literal".length())));
        return literalInterface;
    }

    private JDefinedClass createCustomLiteral(final JDefinedClass literalInterface,
                                              final OpenEnumDefinition definition) throws SAXException {
        final JDefinedClass custom = nestedClass(literalInterface, "Custom");
        custom._implements(literalInterface);
        custom._implements(runtime.customLiteral());
        custom.javadoc()
                .append(String.format("A literal of %s that is neither defined by the standard nor contributed by an "
                                              + "{@code OpenEnumLiteralProvider}.",
                                      definition.typeName()
                                              .getLocalPart()));

        custom.field(JMod.PRIVATE | JMod.FINAL, String.class, "value");

        final JMethod constructor = custom.constructor(JMod.PUBLIC);
        final JVar value = constructor.param(String.class, "value");
        constructor.javadoc()
                .addParam(value)
                .append("The literal as it appears in the XML. Must not be null.");
        constructor.body()
                .assign(JExpr.refthis("value"), codeModel.ref(Objects.class)
                        .staticInvoke("requireNonNull")
                        .arg(value)
                        .arg(JExpr.lit("The value of a custom literal must not be null.")));

        final JMethod valueMethod = custom.method(JMod.PUBLIC, String.class, "value");
        valueMethod.annotate(Override.class);
        valueMethod.body()
                ._return(JExpr.ref("value"));

        createCustomEquals(custom);

        final JMethod hashCode = custom.method(JMod.PUBLIC, codeModel.INT, "hashCode");
        hashCode.annotate(Override.class);
        hashCode.body()
                ._return(JExpr.ref("value")
                                 .invoke("hashCode"));

        final JMethod toString = custom.method(JMod.PUBLIC, String.class, "toString");
        toString.annotate(Override.class);
        toString.body()
                ._return(JExpr.ref("value"));

        return custom;
    }

    private void createCustomEquals(final JDefinedClass custom) {
        final JMethod equals = custom.method(JMod.PUBLIC, codeModel.BOOLEAN, "equals");
        equals.annotate(Override.class);
        final JVar other = equals.param(Object.class, "obj");

        final JBlock body = equals.body();
        body._if(JExpr._this()
                         .eq(other))
                ._then()
                ._return(JExpr.TRUE);
        body._if(other._instanceof(custom)
                         .not())
                ._then()
                ._return(JExpr.FALSE);
        body._return(JExpr.ref("value")
                             .invoke("equals")
                             .arg(JExpr.invoke(JExpr.cast(custom, other), "value")));
    }

    private JDefinedClass createEnum(final JPackage pkg, final String className,
                                     final OpenEnumDefinition definition,
                                     final Map<String, OpenEnumDefinition.Literal> literals) {
        final JDefinedClass literalEnum = outline.getClassFactory()
                .createClass(pkg, className, null, ClassType.ENUM);

        appendDocumentation(literalEnum.javadoc(), definition.documentation());
        literalEnum.javadoc()
                .append(String.format("%n<p>%nThe literals of the open enumeration {@code %s} that are defined by the "
                                              + "standard. A document may use others; see {@link %sLiteral}.%n</p>",
                                      definition.typeName()
                                              .getLocalPart(), className));

        for (final Map.Entry<String, OpenEnumDefinition.Literal> entry : literals.entrySet()) {
            final JEnumConstant constant = literalEnum.enumConstant(entry.getKey());
            constant.arg(JExpr.lit(entry.getValue()
                                           .value()));
            appendDocumentation(constant.javadoc(), entry.getValue()
                    .documentation());
        }

        literalEnum.field(JMod.PRIVATE | JMod.FINAL, String.class, "value");

        final JMethod constructor = literalEnum.constructor(JMod.NONE);
        final JVar value = constructor.param(String.class, "value");
        constructor.body()
                .assign(JExpr.refthis("value"), value);

        final JMethod valueMethod = literalEnum.method(JMod.PUBLIC, String.class, "value");
        valueMethod.annotate(Override.class);
        valueMethod.body()
                ._return(JExpr.ref("value"));

        createFromValue(literalEnum);

        return literalEnum;
    }

    private void createFromValue(final JDefinedClass literalEnum) {
        final JMethod fromValue = literalEnum.method(JMod.PUBLIC | JMod.STATIC, literalEnum, "fromValue");
        final JVar value = fromValue.param(String.class, "value");
        fromValue.javadoc()
                .append("Returns the constant with the given value.");
        fromValue.javadoc()
                .addParam(value)
                .append("The literal as it appears in the XML.");
        fromValue.javadoc()
                .addReturn()
                .append("The constant with that value, or {@code null} if the standard does not define it. "
                                + "Never throws.");

        final JBlock body = fromValue.body();
        final JForEach candidate = body.forEach(literalEnum, "candidate", literalEnum.staticInvoke("values"));
        candidate.body()
                ._if(candidate.var()
                             .invoke("value")
                             .invoke("equals")
                             .arg(value))
                ._then()
                ._return(candidate.var());
        body._return(JExpr._null());
    }

    private void createFactoryMethod(final JDefinedClass literalInterface, final JDefinedClass literalEnum,
                                     final JDefinedClass customLiteral, final OpenEnumDefinition definition) {
        final JMethod of = literalInterface.method(JMod.PUBLIC | JMod.STATIC, literalInterface, "of");
        final JVar value = of.param(String.class, "value");
        of.javadoc()
                .append(String.format("Returns the literal of %s with the given value.",
                                      definition.typeName()
                                              .getLocalPart()));
        of.javadoc()
                .addParam(value)
                .append("The literal as it appears in the XML.");
        of.javadoc()
                .addReturn()
                .append("The constant defined by the standard, a literal contributed by an "
                                + "{@code OpenEnumLiteralProvider}, or a {@link Custom}. "
                                + "{@code null} only for a {@code null} argument. Never throws.");

        final JBlock body = of.body();
        body._if(value.eq(JExpr._null()))
                ._then()
                ._return(JExpr._null());

        final JVar defined = body.decl(literalEnum, "defined", literalEnum.staticInvoke("fromValue")
                .arg(value));
        body._if(defined.ne(JExpr._null()))
                ._then()
                ._return(defined);

        final JVar contributed = body.decl(literalInterface, "contributed", runtime.registry()
                .staticInvoke("resolve")
                .arg(literalInterface.dotclass())
                .arg(value));
        body._if(contributed.ne(JExpr._null()))
                ._then()
                ._return(contributed);

        body._return(JExpr._new(customLiteral)
                             .arg(value));
    }

    private JDefinedClass nestedClass(final JDefinedClass owner, final String name) throws SAXException {
        try {
            return owner._class(JMod.PUBLIC | JMod.STATIC | JMod.FINAL, name, ClassType.CLASS);
        } catch (final JClassAlreadyExistsException e) {
            throw new SAXException("Could not generate " + owner.fullName() + "." + name + ".", e);
        }
    }

    private static void appendDocumentation(final JDocComment javadoc, final String documentation) {
        if (documentation != null) {
            javadoc.append(documentation);
        }
    }

}
