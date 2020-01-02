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

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;

import com.foursoft.xml.ExtendedUnmarshaller;

public final class CodeModelUtility {

    private CodeModelUtility() {
        // HIDE
    }

    public static JDefinedClass findSchemaType(final Outline outline, final String schemaTypeName) {
        for (final ClassOutline classOutline : outline.getClasses()) {
            if (classOutline.target.getTypeName()
                    .getLocalPart()
                    .equals(schemaTypeName)) {
                return classOutline.implClass;
            }
        }
        throw new RuntimeException(schemaTypeName
                + " is not a type in this schema and has no generated class, so no backreference can be set!");
    }

    public static JDocComment appendGetterDisclaimer(final JDocComment comment, final JCodeModel codeModel) {
        return comment.append(String.format("%n"))
                .append("<b>Warning!:</b> This is a readonly property! It has to be initialized during the unmarshalling process by the ")
                .append(codeModel.ref(ExtendedUnmarshaller.class))
                .append(".<br/>")
                .append("This property is consistent to the state of object model at the time of unmarshalling. It does <b>not</b> reflect any changes done in the object model after the unmarshalling.<br/>")
                .append(String.format("%n"))
                .append("This property has no effect when the object is marshalled to xml.");
    }

}
