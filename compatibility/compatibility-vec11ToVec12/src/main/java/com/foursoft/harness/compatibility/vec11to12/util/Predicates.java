/*-
 * ========================LICENSE_START=================================
 * compatibility-vec11tovec12
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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