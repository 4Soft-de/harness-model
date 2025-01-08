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
package com.foursoft.harness.vec.rdf.changes.equivalences;

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

    public UnorderedCollectionEquivalence(final Equivalence<E> elementEquivalence) {
        this.elementEquivalence = elementEquivalence;
    }

    @Override
    protected boolean doEquivalent(final Collection<T> a, final Collection<T> b) {
        if (a.size() != b.size()) {
            return false;
        }
        final Set<Wrapper<T>> setA = a.stream()
                .map(elementEquivalence::wrap)
                .collect(Collectors.toSet());
        final Set<Wrapper<T>> setB = b.stream()
                .map(elementEquivalence::wrap)
                .collect(Collectors.toSet());

        if (setA.containsAll(setB)) {
            return true;
        }

        if (LOGGER.isInfoEnabled()) {
            final Set<T> aOnly = SetUtils.difference(setA, setB)
                    .stream()
                    .map(Wrapper::get)
                    .collect(Collectors.toSet());
            final Set<T> bOnly = SetUtils.difference(setB, setA)
                    .stream()
                    .map(Wrapper::get)
                    .collect(Collectors.toSet());
            LOGGER.info("Collection Difference, Side A only: {}, Side B only: {}", aOnly, bOnly);
        }

        return false;
    }

    @Override
    protected int doHash(final Collection<T> ts) {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(769627, 94811);
        ts.stream()
                .map(elementEquivalence::hash)
                .sorted()
                .forEach(hashCodeBuilder::append);
        return hashCodeBuilder.toHashCode();
    }
}
