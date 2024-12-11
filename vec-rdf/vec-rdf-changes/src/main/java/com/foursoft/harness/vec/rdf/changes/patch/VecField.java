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

import com.foursoft.harness.vec.rdf.changes.equivalences.VecDataObjectEquivalence;
import com.foursoft.harness.vec.rdf.changes.equivalences.VecFieldEquivalence;
import com.google.common.base.Equivalence;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlTransient;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public class VecField {

    private final Field field;

    private final Equivalence<Object> equivalence;

    public VecField(Field field) {
        this.field = field;
        this.equivalence = new VecFieldEquivalence(this);
    }

    public Equivalence<Object> equivalence() {
        return equivalence;
    }

    public Equivalence<Object> elementEquivalence() {
        if (isReference()) {
            return Equivalence.equals();
        }
        if (isVecClass()) {
            return VecDataObjectEquivalence.instance();
        }
        return Equivalence.equals();
    }

    public Field getField() {
        return field;
    }

    @Override
    public String toString() {
        return String.format("VecField [field=%s]", field);
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

    public boolean isVecClass() {
        return getValueType().getPackageName()
                .startsWith("com.foursoft.harness.vec");
    }

    public Class<?> getValueType() {
        if (isCollection()) {
            if (field.getGenericType() instanceof ParameterizedType parameterizedType) {
                if (parameterizedType.getActualTypeArguments().length == 1) {
                    Type type = parameterizedType.getActualTypeArguments()[0];
                    if (type instanceof Class<?>) {
                        return (Class<?>) type;
                    }
                }
            }
            throw new UnsupportedOperationException("Unable to extract value type from " + field);
        }
        return field.getType();
    }
}
