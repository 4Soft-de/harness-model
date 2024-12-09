/*-
 * ========================LICENSE_START=================================
 * VEC RDF Common
 * %%
 * Copyright (C) 2024 4Soft GmbH
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
package com.foursoft.harness.vec.rdf.common.meta.xmi;

import java.util.Arrays;
import java.util.Objects;

public record UmlType(String xmiId, String name, String xmiType, UmlLiteral[] literals) {

    public boolean isClass() {
        return VecModelProviderBuilder.UML_CLASS.equals(xmiType);
    }

    public boolean isEnum() {
        return VecModelProviderBuilder.UML_ENUMERATION.equals(xmiType);
    }

    public boolean isPrimitive() {
        return VecModelProviderBuilder.UML_PRIMITIVE.equals(xmiType);
    }

    @Override public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final UmlType umlType = (UmlType) o;
        return Objects.equals(name, umlType.name) && Objects.equals(xmiId, umlType.xmiId) &&
                Objects.equals(xmiType, umlType.xmiType) && Arrays.equals(literals, umlType.literals);
    }

    @Override public int hashCode() {
        int result = Objects.hashCode(xmiId);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(xmiType);
        result = 31 * result + Arrays.hashCode(literals);
        return result;
    }

    @Override public String toString() {
        return "UmlType{" +
                "xmiId='" + xmiId + '\'' +
                ", name='" + name + '\'' +
                ", xmiType='" + xmiType + '\'' +
                ", literals=" + Arrays.toString(literals) +
                '}';
    }
}
