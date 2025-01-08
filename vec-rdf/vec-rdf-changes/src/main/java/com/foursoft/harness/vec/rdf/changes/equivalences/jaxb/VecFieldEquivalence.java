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
package com.foursoft.harness.vec.rdf.changes.equivalences.jaxb;

import com.foursoft.harness.vec.rdf.changes.equivalences.UnorderedCollectionEquivalence;
import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;
import com.foursoft.harness.vec.rdf.common.meta.VecClass;
import com.foursoft.harness.vec.rdf.common.meta.VecField;
import com.google.common.base.Equivalence;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;

public class VecFieldEquivalence extends Equivalence<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(VecFieldEquivalence.class);

    private final VecField vecField;

    private final Equivalence<Object> elementEquivalence;
    private final Equivalence<Collection<Object>> collectionEquivalence;

    /**
     * @param vecField
     * @param valueObjectEquivalence the Equivalence used to compare VEC value objects
     */
    public VecFieldEquivalence(final VecField vecField, final Equivalence<Object> valueObjectEquivalence) {
        this.vecField = vecField;

        this.elementEquivalence = elementEquivalence(vecField, valueObjectEquivalence);
        collectionEquivalence = new UnorderedCollectionEquivalence<>(elementEquivalence);
    }

    public static Equivalence<Object> elementEquivalence(final VecField vecField,
                                                         final Equivalence<Object> valueObjectEquivalence) {
        if (vecField.isReference()) {
            return Equivalence.equals();
        }
        if (VecClass.isVecClass(vecField.getValueType())) {
            return valueObjectEquivalence;
        }
        return Equivalence.equals();
    }

    @Override
    protected boolean doEquivalent(final Object a, final Object b) {
        final Object valueA = getFieldValue(a);
        final Object valueB = getFieldValue(b);

        if (vecField.isCollection()) {
            if (!doEquivalentCollection(valueA, valueB)) {
                LOGGER.info("{} - {} not equivalent because of field '{}' has collection element difference", a, b,
                            vecField.getField());
                return false;
            } else {
                return true;
            }
        }
        if (!this.elementEquivalence.equivalent(valueA, valueB)) {
            LOGGER.info("{} - {} not equivalent because of field '{}': {} - {}.", a, b, vecField.getField()
                    .getName(), valueA, valueB);
            return false;
        }
        return true;
    }

    private boolean doEquivalentCollection(final Object fieldValueA, final Object fieldValueB) {
        if (fieldValueA == fieldValueB) {
            return true;
        }
        return this.collectionEquivalence.equivalent(toCollection(fieldValueA), toCollection(fieldValueB));
    }

    private Collection<Object> toCollection(final Object value) {
        if (value == null) {
            return Collections.emptyList();
        }
        if (value instanceof final Collection<?> collection) {
            return (Collection<Object>) collection;
        }
        throw new VecRdfException(
                "Expected a Collection, but got a " + value.getClass() + " for field " + vecField.getField()
                        .toString());
    }

    @Override
    protected int doHash(final Object t) {
        final Object valueT = getFieldValue(t);
        if (vecField.isCollection()) {
            return collectionEquivalence.hash(toCollection(valueT));
        }
        return elementEquivalence.hash(valueT);
    }

    private Object getFieldValue(final Object t) {
        try {
            return FieldUtils.readField(vecField.getField(), t, true);
        } catch (final IllegalAccessException e) {
            throw new VecRdfException("Unable to access field during equivalence check", e);
        }
    }
}
