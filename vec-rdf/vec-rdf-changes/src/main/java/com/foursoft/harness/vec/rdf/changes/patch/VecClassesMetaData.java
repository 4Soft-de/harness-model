/*-
 * ========================LICENSE_START=================================
 * VEC RDF Changesets (Experimental)
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
package com.foursoft.harness.vec.rdf.changes.patch;

import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Stream;

public class VecClassesMetaData {

    private static final VecClassesMetaData INSTANCE = new VecClassesMetaData();

    private VecClassesMetaData() {
        // HIDE
    }

    public static VecClassesMetaData metaData() {
        return INSTANCE;
    }

    private final Map<Class<?>, VecField[]> classFields = new HashMap<Class<?>, VecField[]>();

    public VecField[] fieldsForClass(final Class<?> clazz) {
        return classFields.computeIfAbsent(clazz, this::computeFieldsForClass);
    }

    private VecField[] computeFieldsForClass(final Class<?> clazz) {
        Class<?> currentClass = clazz;
        List<VecField> vecFields = new ArrayList<>();
        while (currentClass != null) {
            Stream.of(currentClass.getDeclaredFields())
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .map(VecField::new)
                    .forEach(vecFields::add);

            currentClass = currentClass.getSuperclass();
        }
        return vecFields.toArray(new VecField[0]);
    }

    public VecField fieldForPropertyDescriptor(PropertyDescriptor propertyDescriptor) {
        return Arrays.stream(fieldsForClass(propertyDescriptor.getReadMethod()
                                                    .getDeclaringClass()))
                .filter(vecField -> vecField.getField()
                        .getName()
                        .equals(propertyDescriptor.getName()))
                .findAny()
                .orElseThrow(() -> new VecRdfException("No VEC field descriptor found for " + propertyDescriptor));
    }

}
