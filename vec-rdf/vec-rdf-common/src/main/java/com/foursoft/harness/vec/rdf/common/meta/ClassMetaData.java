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
package com.foursoft.harness.vec.rdf.common.meta;

import com.foursoft.harness.vec.rdf.common.meta.xmi.VecModelProvider;
import jakarta.xml.bind.annotation.XmlElement;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.Arrays;

public class ClassMetaData {
    private final Class<?> type;
    private final FieldMetaData[] fields;

    private final boolean anonymous;

    ClassMetaData(Class<?> type, VecModelProvider modelProvider) {
        this.type = type;

        anonymous = modelProvider.getAnonymousClasses().contains(type.getSimpleName()
                                                                         .replaceFirst("Vec", ""));

        fields = Arrays.stream(FieldUtils.getAllFields(type))
                .filter(f -> f.getAnnotation(XmlElement.class) != null)
                .map(f -> new FieldMetaData(f, modelProvider))
                .toArray(FieldMetaData[]::new);
    }

    public Class<?> getType() {
        return type;
    }

    public FieldMetaData[] fields() {
        return this.fields;
    }

    public boolean isAnonymous() {
        return anonymous;
    }
}
