/*-
 * ========================LICENSE_START=================================
 * VEC 2.X
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
package com.foursoft.harness.vec.v2x.traversal;

import com.foursoft.harness.vec.common.traversal.MultiNavigation;
import com.foursoft.harness.vec.common.traversal.Navigations;
import com.foursoft.harness.vec.common.traversal.SingleNavigation;
import com.foursoft.harness.vec.v2x.VecContent;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;

/**
 * Navigations starting at the {@link VecContent}, the root of a VEC model.
 * <p>
 * To reach the specifications of all document versions, continue this navigation at the call site, for example
 * <pre>
 * {@code
 * Contents.toDocumentVersions()
 *         .then(SpecificationOwners.toSpecifications())
 *         .ofType(VecPlacementSpecification.class);
 * }
 * </pre>
 */
public final class Contents {

    private Contents() {
        // hide default constructor
    }

    /**
     * Navigates to the document versions of a content.
     *
     * @return A navigation to the document versions of a content.
     */
    public static MultiNavigation<VecContent, VecDocumentVersion> toDocumentVersions() {
        return Navigations.collection(VecContent::getDocumentVersions);
    }

    /**
     * Navigates to the document version with the given document number.
     *
     * @param documentNumber Document number of the document version to navigate to.
     * @return A navigation to the identified document version.
     */
    public static SingleNavigation<VecContent, VecDocumentVersion> documentVersionBy(final String documentNumber) {
        return toDocumentVersions()
                .filter(documentVersion -> documentNumber.equals(documentVersion.getDocumentNumber()))
                .atMostOne();
    }

}
