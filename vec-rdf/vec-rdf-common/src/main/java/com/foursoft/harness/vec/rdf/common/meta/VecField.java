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
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public class VecField {

    private final Field field;

    public VecField(final Field field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return String.format("VecField [field=%s]", field);
    }

    public Field getField() {
        return field;
    }

    public String getName() {
        return field.getName();
    }

    public boolean isXmlId() {
        return field.isAnnotationPresent(XmlID.class);
    }

    public boolean isTransient() {
        return field.isAnnotationPresent(XmlTransient.class);
    }

    public boolean isReference() {
        return field.isAnnotationPresent(XmlIDREF.class);
    }

    public boolean isCollection() {
        return Collection.class.isAssignableFrom(field.getType());
    }

    /**
     * Returns the type of the field and if it is a collection the element type of the collection items.
     *
     * @return
     */
    public Class<?> getValueType() {
        if (isCollection()) {
            if (field.getGenericType() instanceof final ParameterizedType parameterizedType &&
                    parameterizedType.getActualTypeArguments().length == 1) {
                final Type type = parameterizedType.getActualTypeArguments()[0];
                if (type instanceof Class<?>) {
                    return (Class<?>) type;
                }
            }
            throw new UnsupportedOperationException("Unable to extract value type from " + field);
        }
        return field.getType();
    }

    public Object readValue(final Object target) {
        try {
            return FieldUtils.readField(field, target, true);
        } catch (final IllegalAccessException e) {
            throw new VecRdfException("Unable to read field value.", e);
        }
    }

}
