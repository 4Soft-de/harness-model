package com.foursoft.harness.vec.rdf.changes.equivalences;

import com.foursoft.harness.vec.rdf.changes.patch.VecField;
import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;
import com.google.common.base.Equivalence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;

public class VecFieldEquivalence extends Equivalence<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(VecFieldEquivalence.class);

    private final VecField vecField;

    private final Equivalence<Object> elementEquivalence;
    private final Equivalence<Collection<Object>> collectionEquivalence;

    public VecFieldEquivalence(VecField vecField) {
        this.vecField = vecField;
        this.vecField.getField()
                .trySetAccessible();

        this.elementEquivalence = vecField.elementEquivalence();
        collectionEquivalence = new UnorderedCollectionEquivalence<>(elementEquivalence);
    }

    @Override
    protected boolean doEquivalent(Object a, Object b) {
        Object valueA = getFieldValue(a);
        Object valueB = getFieldValue(b);

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

    private boolean doEquivalentCollection(Object fieldValueA, Object fieldValueB) {
        if (fieldValueA == fieldValueB) {
            return true;
        }
        return this.collectionEquivalence.equivalent(toCollection(fieldValueA), toCollection(fieldValueB));
    }

    private Collection<Object> toCollection(Object value) {
        if (value == null) {
            return Collections.emptyList();
        }
        if (value instanceof Collection<?> collection) {
            return (Collection<Object>) collection;
        }
        throw new VecRdfException(
                "Expected a Collection, but got a " + value.getClass() + " for field " + vecField.getField()
                        .toString());
    }

    @Override
    protected int doHash(Object t) {
        Object valueT = getFieldValue(t);
        if (vecField.isCollection()) {
            return collectionEquivalence.hash(toCollection(valueT));
        }
        return elementEquivalence.hash(valueT);
    }

    private Object getFieldValue(Object t) {
        try {
            return vecField.getField()
                    .get(t);
        } catch (IllegalAccessException e) {
            throw new VecRdfException("Unable to access field during equivalence check", e);
        }
    }
}
