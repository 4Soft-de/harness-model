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
package com.foursoft.harness.vec.v12x.navigations;

import com.foursoft.harness.vec.common.traversal.MultiNavigation;
import com.foursoft.harness.vec.v12x.VecContent;
import com.foursoft.harness.vec.v12x.VecDocumentVersion;
import com.foursoft.harness.vec.v12x.VecSpecification;
import com.foursoft.harness.vec.v12x.traversal.Contents;
import com.foursoft.harness.vec.v12x.traversal.SpecificationOwners;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Navigation methods for the {@link VecContent}.
 *
 * @deprecated Use {@link Contents} instead.
 */
@Deprecated(forRemoval = true)
public final class ContentNavs {

    private ContentNavs() {
        // hide default constructor
    }

    /**
     * @deprecated Compose the navigation at the call site instead:
     * {@code Contents.toDocumentVersions().then(SpecificationOwners.toSpecifications()).ofType(clazz)}.
     */
    @Deprecated(forRemoval = true)
    public static <T extends VecSpecification> Function<VecContent, List<T>> allSpecificationsOf(final Class<T> clazz) {
        final MultiNavigation<VecContent, T> specifications = Contents.toDocumentVersions()
                .then(SpecificationOwners.<VecDocumentVersion>toSpecifications())
                .ofType(clazz);
        return specifications::listFrom;
    }

    /**
     * @deprecated Use {@link Contents#documentVersionBy(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecContent, Optional<VecDocumentVersion>> documentVersionBy(final String documentNumber) {
        return Contents.documentVersionBy(documentNumber);
    }

}
