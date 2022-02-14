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
package com.foursoft.jaxb.navext.runtime.xjc.plugin;

import javax.xml.bind.annotation.XmlTransient;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;

public class PropertyBuilder {

    private static final String GETTER_PREFIX = "get";

    private final JCodeModel codeModel;
    private String name;
    private String getterName;
    private JType baseType;
    private JExpression init;
    private JDocComment getterJavadoc;

    public PropertyBuilder(final JCodeModel codeModel) {
        this.codeModel = codeModel;
    }

    public PropertyBuilder withName(final String name) {
        this.name = name;
        final String suffix = name.substring(0, 1)
                .toUpperCase() + name.substring(1);
        getterName = GETTER_PREFIX + suffix;
        return this;
    }

    public PropertyBuilder withBaseType(final String typeName) throws ClassNotFoundException {
        baseType = codeModel.parseType(typeName);
        return this;
    }

    public PropertyBuilder withBaseType(final JType baseType) {
        this.baseType = baseType;
        return this;
    }

    public JFieldVar build(final JDefinedClass targetClass) {
        final JFieldVar field = targetClass.field(JMod.PRIVATE, baseType, name);
        if (init != null) {
            field.init(init);
        }

        field.annotate(codeModel.ref(XmlTransient.class));

        final JMethod getter = targetClass.method(JMod.PUBLIC, baseType, getterName);
        getter.body()
                ._return(JExpr.ref(field.name()));

        if (getterJavadoc != null) {
            getter.javadoc()
                    .addAll(getterJavadoc);
        }

        return field;
    }

    public void buildAbstract(final JDefinedClass targetClass) {
        if (targetClass.getMethod(getterName, new JType[] {}) != null) {
            return;
        }
        final JMethod getter = targetClass.method(JMod.PUBLIC, baseType, getterName);

        if (getterJavadoc != null) {
            getter.javadoc()
                    .addAll(getterJavadoc);
        }

    }

    public PropertyBuilder withInit(final JExpression init) {
        this.init = init;
        return this;
    }

    public PropertyBuilder withGetterJavadoc(final JDocComment comment) {
        this.getterJavadoc = comment;
        return this;
    }
}
