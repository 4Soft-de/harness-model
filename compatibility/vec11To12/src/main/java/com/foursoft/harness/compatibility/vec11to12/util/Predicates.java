package com.foursoft.harness.compatibility.vec11to12.util;

import java.util.function.Predicate;

/**
 * Predicates for several cases.
 */
public class Predicates {

    private static final String DOCUMENT_TYPE_PART_MASTER = "PartMaster";

    private Predicates() {
        // Shadow constructor.
    }

    /**
     * Checks if the {@link com.foursoft.harness.vec.v12x.VecDocumentVersion} is a Part Master.
     *
     * @return Predicate to check if the {@link com.foursoft.harness.vec.v12x.VecDocumentVersion} is a Part Master.
     */
    public static Predicate<com.foursoft.harness.vec.v12x.VecDocumentVersion> partMasterV12() {
        return documentVersion -> documentVersion.getDocumentType().equals(DOCUMENT_TYPE_PART_MASTER);
    }

    /**
     * Checks if the {@link com.foursoft.harness.vec.v113.VecDocumentVersion} is a Part Master.
     *
     * @return Predicate to check if the {@link com.foursoft.harness.vec.v113.VecDocumentVersion} is a Part Master.
     */
    public static Predicate<com.foursoft.harness.vec.v113.VecDocumentVersion> partMasterV11() {
        return documentVersion -> documentVersion.getDocumentType().equals(DOCUMENT_TYPE_PART_MASTER);
    }

}
