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

import com.foursoft.harness.vec.common.HasSpecifications;
import com.foursoft.harness.vec.common.annotations.RequiresBackReferences;
import com.foursoft.harness.vec.v12x.VecBuildingBlockSpecification2D;
import com.foursoft.harness.vec.v12x.VecBuildingBlockSpecification3D;
import com.foursoft.harness.vec.v12x.VecCompositionSpecification;
import com.foursoft.harness.vec.v12x.VecDocumentVersion;
import com.foursoft.harness.vec.v12x.VecGeometryNode2D;
import com.foursoft.harness.vec.v12x.VecGeometryNode3D;
import com.foursoft.harness.vec.v12x.VecGeometrySegment2D;
import com.foursoft.harness.vec.v12x.VecGeometrySegment3D;
import com.foursoft.harness.vec.v12x.VecOccurrenceOrUsage;
import com.foursoft.harness.vec.v12x.VecPartOccurrence;
import com.foursoft.harness.vec.v12x.VecSpecification;
import com.foursoft.harness.vec.v12x.traversal.SpecificationOwners;
import com.foursoft.harness.vec.v12x.traversal.Specifications;

import java.util.List;
import java.util.function.Function;

/**
 * Navigation methods for the {@link VecSpecification}.
 *
 * @deprecated These navigations start at a specification and at the element holding it, and are therefore
 * spread over the catalogs of those types: {@link Specifications} and {@link SpecificationOwners}.
 */
@Deprecated(forRemoval = true)
public final class SpecificationNavs {

    private SpecificationNavs() {
        // hide default constructor
    }

    /**
     * @deprecated Use {@link Specifications#toParentDocumentNumber()} instead.
     */
    @Deprecated(forRemoval = true)
    @RequiresBackReferences
    public static Function<VecSpecification, String> parentDocumentNumber() {
        return Specifications.toParentDocumentNumber()::orElseNull;
    }

    /**
     * @deprecated Use {@link Specifications#toParentDocumentVersion()} instead.
     */
    @Deprecated(forRemoval = true)
    @RequiresBackReferences
    public static Function<VecSpecification, VecDocumentVersion> parentDocumentVersion() {
        return Specifications.toParentDocumentVersion()::orElseNull;
    }

    /**
     * @deprecated Use {@link SpecificationOwners#toOccurrenceOrUsages()} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<HasSpecifications<VecSpecification>, List<VecOccurrenceOrUsage>> allOccurrenceOrUsages() {
        return SpecificationOwners.<HasSpecifications<VecSpecification>>toOccurrenceOrUsages()::listFrom;
    }

    /**
     * Gets the {@link VecCompositionSpecification}s and gets
     * their {@link VecCompositionSpecification#getComponents() components}.
     *
     * @return A possibly-empty list of Components.
     * @deprecated Use {@link SpecificationOwners#toComponents()} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<HasSpecifications<VecSpecification>, List<VecPartOccurrence>> components() {
        return SpecificationOwners.<HasSpecifications<VecSpecification>>toComponents()::listFrom;
    }

    /**
     * Gets the {@link VecCompositionSpecification} with the given specification value and
     * gets their {@link VecCompositionSpecification#getComponents() components}.
     *
     * @param compositionSpecificationId Id the {@link VecCompositionSpecification} has to have.
     * @return A possibly-empty list of Components.
     * @deprecated Use {@link SpecificationOwners#componentsBy(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<HasSpecifications<VecSpecification>, List<VecPartOccurrence>> componentsBy(
            final String compositionSpecificationId) {
        return SpecificationOwners.<HasSpecifications<VecSpecification>>componentsBy(
                compositionSpecificationId)::listFrom;
    }

    /**
     * @deprecated Use {@link Specifications#geometrySegment2dBy(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecBuildingBlockSpecification2D, VecGeometrySegment2D> geometrySegment2dBy(
            final String segmentId) {
        return Specifications.geometrySegment2dBy(segmentId)::orElseNull;
    }

    /**
     * @deprecated Use {@link Specifications#geometrySegment3dBy(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecBuildingBlockSpecification3D, VecGeometrySegment3D> geometrySegment3dBy(
            final String segmentId) {
        return Specifications.geometrySegment3dBy(segmentId)::orElseNull;
    }

    /**
     * @deprecated Use {@link Specifications#geometryNode2dBy(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecBuildingBlockSpecification2D, VecGeometryNode2D> geometryNode2dBy(final String nodeId) {
        return Specifications.geometryNode2dBy(nodeId)::orElseNull;
    }

    /**
     * @deprecated Use {@link Specifications#geometryNode3dBy(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecBuildingBlockSpecification3D, VecGeometryNode3D> geometryNode3dBy(final String nodeId) {
        return Specifications.geometryNode3dBy(nodeId)::orElseNull;
    }

}
