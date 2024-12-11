package com.foursoft.harness.vec.rdf.changes.equals;

import com.google.common.base.Equivalence;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UnorderedCollectionEquivalence<E, T extends E> extends Equivalence<Collection<T>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnorderedCollectionEquivalence.class);

    private final Equivalence<E> elementEquivalence;

    public UnorderedCollectionEquivalence(Equivalence<E> elementEquivalence) {
        this.elementEquivalence = elementEquivalence;
    }

    @Override
    protected boolean doEquivalent(Collection<T> a, Collection<T> b) {
        if (a.size() != b.size()) {
            return false;
        }
        Set<Wrapper<T>> setA = a.stream()
                .map(elementEquivalence::wrap)
                .collect(Collectors.toSet());
        Set<Wrapper<T>> setB = b.stream()
                .map(elementEquivalence::wrap)
                .collect(Collectors.toSet());

        if (setA.containsAll(setB)) {
            return true;
        }
        if (LOGGER.isInfoEnabled()) {
            Set<T> aOnly = SetUtils.difference(setA, setB)
                    .stream()
                    .map(Wrapper::get)
                    .collect(Collectors.toSet());
            Set<T> bOnly = SetUtils.difference(setB, setA)
                    .stream()
                    .map(Wrapper::get)
                    .collect(Collectors.toSet());
            LOGGER.info("Collection Difference, Side A only: {}, Side B only: {}", aOnly, bOnly);
        }

        return false;
    }

    @Override
    protected int doHash(Collection<T> ts) {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(769627, 94811);
        ts.stream()
                .map(elementEquivalence::hash)
                .sorted()
                .forEach(hashCodeBuilder::append);
        return hashCodeBuilder.toHashCode();
    }
}
