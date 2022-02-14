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

import javax.xml.bind.Marshaller;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;

public class EmptyListHandlerPlugin extends Plugin {

    @Override
    public String getOptionName() {
        return "Xnull-empty-lists";
    }

    @Override
    public String getUsage() {
        return "to be defined!";

    }

    @Override
    public boolean run(final Outline outline, final Options opt, final ErrorHandler errorHandler) throws SAXException {
        outline.getClasses()
                .forEach(c -> createBeforeMarshall(outline.getCodeModel(), c));
        return true;
    }

    public void createBeforeMarshall(final JCodeModel jCodeModel, final ClassOutline outline) {
        // boolean beforeMarshal(Marshaller)
        final JDefinedClass targetClass = outline.implClass;
        // targetClass.method(JMod.PUBLIC, baseType, getterName)
        final JMethod method = targetClass.method(JMod.PUBLIC, jCodeModel.BOOLEAN, "beforeMarshal");

        final JVar marshaller = method.param(Marshaller.class, "marshaller");

        final JBlock body = method.body();
        if (outline.getSuperClass() != null) {
            body.invoke(JExpr._super(), "beforeMarshal")
                    .arg(marshaller);
        }

        for (final FieldOutline f : outline.getDeclaredFields()) {
            createEmptyCheck(f, body);
        }

        body._return(JExpr.TRUE);

    }

    private void createEmptyCheck(final FieldOutline fieldOutline, final JBlock body) {
        final CPropertyInfo propertyInfo = fieldOutline.getPropertyInfo();
        if (propertyInfo.isCollection()) {
            final JFieldRef fieldRef = JExpr.ref(propertyInfo.getName(false));
            final JBlock conditional = body._if(fieldRef.ne(JExpr._null())
                    .cand(fieldRef.invoke("isEmpty")))
                    ._then();
            conditional.assign(fieldRef, JExpr._null());
        }

    }

}
