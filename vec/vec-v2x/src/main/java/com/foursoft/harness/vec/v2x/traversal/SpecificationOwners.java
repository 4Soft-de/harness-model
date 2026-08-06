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

import com.foursoft.harness.vec.common.HasSpecifications;
import com.foursoft.harness.vec.common.traversal.MultiNavigation;
import com.foursoft.harness.vec.common.traversal.Navigations;
import com.foursoft.harness.vec.v2x.VecCompositionSpecification;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsage;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPartUsage;
import com.foursoft.harness.vec.v2x.VecPartUsageSpecification;
import com.foursoft.harness.vec.v2x.VecSheetOrChapter;
import com.foursoft.harness.vec.v2x.VecSpecification;

import java.util.stream.Stream;

/**
 * Navigations starting at a {@link HasSpecifications} holding {@link VecSpecification}s, that is at a
 * {@link VecDocumentVersion} or a {@link VecSheetOrChapter}.
 * <p>
 * The catalog is not named {@code Specifications}, which is the catalog of the specifications themselves;
 * these navigations start at the element <em>holding</em> them. They are generic in their source, so a chain
 * starting at one of the holders keeps that holder as its source type:
 * <pre>
 * {@code
 * MultiNavigation<VecDocumentVersion, VecPlacementSpecification> placementSpecifications =
 *     SpecificationOwners.<VecDocumentVersion>toSpecifications().ofType(VecPlacementSpecification.class);
 * }
 * </pre>
 *
 * @see Specifications
 */
public final class SpecificationOwners {

    private SpecificationOwners() {
        // hide default constructor
    }

    /**
     * Navigates to the specifications of their holder.
     *
     * @param <S> Type of the specification holder to navigate from.
     * @return A navigation to the specifications of a holder.
     */
    public static <S extends HasSpecifications<VecSpecification>> MultiNavigation<S, VecSpecification>
    toSpecifications() {
        return Navigations.collection(HasSpecifications::getSpecifications);
    }

    /**
     * Navigates to the components of all {@link VecCompositionSpecification}s of a specification holder.
     *
     * @param <S> Type of the specification holder to navigate from.
     * @return A navigation to the components of a holder.
     */
    public static <S extends HasSpecifications<VecSpecification>> MultiNavigation<S, VecPartOccurrence>
    toComponents() {
        return SpecificationOwners.<S>toSpecifications()
                .ofType(VecCompositionSpecification.class)
                .then(Specifications.toComponents());
    }

    /**
     * Navigates to the part usages of all {@link VecPartUsageSpecification}s of a specification holder.
     *
     * @param <S> Type of the specification holder to navigate from.
     * @return A navigation to the part usages of a holder.
     */
    public static <S extends HasSpecifications<VecSpecification>> MultiNavigation<S, VecPartUsage> toPartUsages() {
        return SpecificationOwners.<S>toSpecifications()
                .ofType(VecPartUsageSpecification.class)
                .then(Specifications.toPartUsages());
    }

    /**
     * Navigates to all occurrences and usages of a specification holder, that is to its
     * {@linkplain #toComponents() components} followed by its {@linkplain #toPartUsages() part usages}.
     *
     * @param <S> Type of the specification holder to navigate from.
     * @return A navigation to the occurrences and usages of a holder.
     */
    public static <S extends HasSpecifications<VecSpecification>> MultiNavigation<S, VecOccurrenceOrUsage>
    toOccurrenceOrUsages() {
        final MultiNavigation<S, VecPartOccurrence> components = toComponents();
        final MultiNavigation<S, VecPartUsage> partUsages = toPartUsages();
        return source -> Stream.concat(components.from(source), partUsages.from(source));
    }

    /**
     * Navigates to the components of the {@link VecCompositionSpecification} with the given identification.
     *
     * @param compositionSpecificationId Identification of the composition specification to navigate into.
     * @param <S>                        Type of the specification holder to navigate from.
     * @return A navigation to the components of the identified composition specification.
     */
    public static <S extends HasSpecifications<VecSpecification>> MultiNavigation<S, VecPartOccurrence> componentsBy(
            final String compositionSpecificationId) {
        return SpecificationOwners.<S>toSpecifications()
                .ofType(VecCompositionSpecification.class)
                .filter(specification -> specification.getIdentification().equals(compositionSpecificationId))
                .atMostOne()
                .then(Specifications.toComponents());
    }

}
