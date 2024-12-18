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

import com.foursoft.harness.vec.rdf.common.NamingStrategy;
import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XmiUmlModelProvider implements UmlModelProvider {

    private final Map<FieldKey, UmlField> fieldMap;

    public XmiUmlModelProvider(final List<UmlField> modelFields) {
        fieldMap = modelFields.stream()
                .collect(Collectors.toMap(f -> new FieldKey(f.parent()
                                                                    .name()
                                                                    .toUpperCase(), f.name()
                                                                    .toUpperCase()), f -> f));

    }

    private UmlField findField(final String vecClassName, final String vecFieldName) {
        final FieldKey key = new FieldKey(vecClassName.toUpperCase(), vecFieldName.toUpperCase());
        final UmlField umlField = fieldMap.get(key);
        if (umlField == null) {
            throw new VecRdfException("No UML metadata found for " + vecClassName + "." + vecFieldName);
        }
        return umlField;
    }

    @Override
    public UmlField findField(final Field field) {
        return findField(NamingStrategy.modelName(field.getDeclaringClass()), NamingStrategy.modelName(field));
    }

    private record FieldKey(String vecClassName, String vecFieldName) {

    }
}
