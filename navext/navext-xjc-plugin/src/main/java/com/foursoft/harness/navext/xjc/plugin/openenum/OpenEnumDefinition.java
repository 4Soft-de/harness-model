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

import javax.xml.namespace.QName;
import java.util.List;

/**
 * The literals an open enumeration declares in the literal schema.
 *
 * @param typeName      The qualified name of the simple type.
 * @param documentation The documentation of the simple type, or {@code null}.
 * @param literals      The literals, in schema order. Never empty.
 */
public record OpenEnumDefinition(QName typeName, String documentation, List<Literal> literals) {

    public OpenEnumDefinition {
        literals = List.copyOf(literals);
    }

    /**
     * A single literal of an open enumeration.
     *
     * @param value         The literal as it appears in the XML.
     * @param documentation The documentation of the literal, or {@code null}.
     */
    public record Literal(String value, String documentation) {
    }

}
