/*-
 * ========================LICENSE_START=================================
 * VEC 1.2.X
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
package com.foursoft.harness.vec.v12x.traversal;

import com.foursoft.harness.vec.common.traversal.MultiNavigation;
import com.foursoft.harness.vec.common.traversal.Navigations;
import com.foursoft.harness.vec.v12x.VecDocumentVersion;
import com.foursoft.harness.vec.v12x.VecExtendableElement;

/**
 * Navigations starting at a {@link VecExtendableElement}, the common super type of the elements which can
 * carry custom properties and reference external documents.
 */
public final class ExtendableElements {

    private ExtendableElements() {
        // hide default constructor
    }

    /**
     * Navigates to the external documents an element references.
     *
     * @return A navigation to the referenced external documents of an element.
     */
    public static MultiNavigation<VecExtendableElement, VecDocumentVersion> toReferencedExternalDocuments() {
        return Navigations.collection(VecExtendableElement::getReferencedExternalDocuments);
    }

    /**
     * Navigates to the document numbers of the external documents an element references.
     *
     * @return A navigation to the document numbers of the referenced external documents.
     */
    public static MultiNavigation<VecExtendableElement, String> toExternalDocumentNumbers() {
        return toReferencedExternalDocuments()
                .then(Navigations.nullable(VecDocumentVersion::getDocumentNumber));
    }

}
