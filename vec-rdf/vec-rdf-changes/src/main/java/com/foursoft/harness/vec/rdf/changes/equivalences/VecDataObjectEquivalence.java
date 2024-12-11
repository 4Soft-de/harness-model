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

import com.foursoft.harness.vec.rdf.changes.patch.VecClassesMetaData;
import com.foursoft.harness.vec.rdf.changes.patch.VecField;
import com.google.common.base.Equivalence;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class VecDataObjectEquivalence extends Equivalence<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(VecDataObjectEquivalence.class);
    private final VecClassesMetaData metaData = VecClassesMetaData.metaData();

    private static final VecDataObjectEquivalence INSTANCE = new VecDataObjectEquivalence();

    public static VecDataObjectEquivalence instance() {
        return INSTANCE;
    }

    private Stream<VecField> relevantFields(Class<?> clazz) {
        return Arrays.stream(metaData.fieldsForClass(clazz))
                .filter(f -> !f.isTransient() && !f.isXmlId());
    }

    @Override
    protected boolean doEquivalent(Object a, Object b) {
        if (a.getClass() != b.getClass()) {
            LOGGER.info("{} - {} not equivalent due to different classes.", a, b);
            return false;
        }
        if (a == b) {
            return true;
        }

        Optional<VecField> unequivalentField = relevantFields(a.getClass()).filter(f -> !f.equivalence()
                        .equivalent(a, b))
                .findAny();

        return unequivalentField.isEmpty();
    }

    @Override
    protected int doHash(Object o) {
        HashCodeBuilder hcb = new HashCodeBuilder(215893, 383261);

        relevantFields(o.getClass()).map(f -> f.equivalence()
                        .hash(o))
                .forEach(hcb::append);

        return hcb.toHashCode();
    }

}
