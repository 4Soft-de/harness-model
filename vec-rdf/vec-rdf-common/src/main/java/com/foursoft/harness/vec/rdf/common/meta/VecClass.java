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

import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.function.Predicate.not;

public class VecClass {

    private static final Map<Class<?>, VecClass> introspectionCache = new ConcurrentHashMap<>();

    public static VecClass analyzeClass(final Class<?> clazz) {
        return introspectionCache.computeIfAbsent(clazz, c -> new VecClass(clazz));
    }

    private final Class<?> type;
    private final VecField[] fields;

    private VecClass(final Class<?> type) {
        this.type = type;

        fields = Arrays.stream(FieldUtils.getAllFields(type))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(VecField::new)
                .filter(not(VecField::isTransient).and(not(VecField::isXmlId)))
                .toArray(VecField[]::new);
    }

    public Class<?> getType() {
        return type;
    }

    public VecField[] getFields() {
        return fields;
    }

    public VecField getField(final String name) {
        return Arrays.stream(fields).filter(field -> name.equals(field.getName())).findAny().orElseThrow(
                () -> new VecRdfException("No VEC field descriptor found for " + name));
    }

    public VecField getField(final PropertyDescriptor propertyDescriptor) {
        return getField(propertyDescriptor.getName());
    }

    public static boolean isVecClass(final Class<?> clazz) {
        return clazz.getPackageName().startsWith("com.foursoft.harness.vec");
    }
}
