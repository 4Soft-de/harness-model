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
