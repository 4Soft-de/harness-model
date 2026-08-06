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
package com.foursoft.harness.vec.v2x.navigations;

import com.foursoft.harness.vec.v2x.VecBuildingBlockPositioning2D;
import com.foursoft.harness.vec.v2x.VecBuildingBlockPositioning3D;
import com.foursoft.harness.vec.v2x.VecBuildingBlockSpecification2D;
import com.foursoft.harness.vec.v2x.VecBuildingBlockSpecification3D;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecGeometryNode2D;
import com.foursoft.harness.vec.v2x.VecGeometryNode3D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment3D;
import com.foursoft.harness.vec.v2x.VecNodeLocation;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsage;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsageViewItem2D;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsageViewItem3D;
import com.foursoft.harness.vec.v2x.VecOnPointPlacement;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v2x.VecPlacement;
import com.foursoft.harness.vec.v2x.VecTopologyNode;
import com.foursoft.harness.vec.v2x.VecTopologySegment;
import com.foursoft.harness.vec.v2x.traversal.DocumentVersions;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Navigation methods for the {@link VecDocumentVersion}.
 *
 * @deprecated Use {@link DocumentVersions} instead.
 */
@Deprecated(forRemoval = true)
public final class DocumentVersionNavs {

    private DocumentVersionNavs() {
        // hide default constructor
    }

    /**
     * @deprecated Use {@link DocumentVersions#geometryNodes3dBy(VecTopologyNode)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecDocumentVersion, List<VecGeometryNode3D>> geometryNodes3dBy(
            final VecTopologyNode topologyNode) {
        return DocumentVersions.geometryNodes3dBy(topologyNode)::listFrom;
    }

    /**
     * @deprecated Use {@link DocumentVersions#geometrySegments3dBy(VecTopologySegment)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecDocumentVersion, List<VecGeometrySegment3D>> geometrySegments3dBy(
            final VecTopologySegment topologySegment) {
        return DocumentVersions.geometrySegments3dBy(topologySegment)::listFrom;
    }

    /**
     * @deprecated Use {@link DocumentVersions#viewItems3dBy(VecOccurrenceOrUsage)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecDocumentVersion, List<VecOccurrenceOrUsageViewItem3D>> viewItems3dBy(
            final VecOccurrenceOrUsage occurrence) {
        return DocumentVersions.viewItems3dBy(occurrence)::listFrom;
    }

    /**
     * @deprecated Use {@link DocumentVersions#topologyNodeBy(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecDocumentVersion, Optional<VecTopologyNode>> topologyNodeBy(
            final String occurrenceIdentification) {
        return DocumentVersions.topologyNodeBy(occurrenceIdentification);
    }

    /**
     * Navigation method to get the {@link VecNodeLocation}s from a given {@link VecPlaceableElementRole}.
     *
     * @param placedElement Placed Element the {@link VecOnPointPlacement} needs to contain.
     *                      May be {@code null} to not filter for this.
     * @return A possibly-empty list of VecNodeLocations.
     * @deprecated Use {@link DocumentVersions#nodeLocationsOf(VecPlaceableElementRole)} instead, or
     * {@link DocumentVersions#toNodeLocations()} for the unfiltered navigation which passing {@code null}
     * stood for.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecDocumentVersion, List<VecNodeLocation>> nodeLocationsBy(
            final VecPlaceableElementRole placedElement) {
        return placedElement == null
                ? DocumentVersions.toNodeLocations()::listFrom
                : DocumentVersions.nodeLocationsOf(placedElement)::listFrom;
    }

    /**
     * @deprecated Use {@link DocumentVersions#placeableElementRoleBy(String, String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecDocumentVersion, VecPlaceableElementRole> placeableElementRoleBy(
            final String compositionSpecificationId,
            final String occurrenceOrUsageId) {
        return DocumentVersions.placeableElementRoleBy(compositionSpecificationId, occurrenceOrUsageId)::orElseNull;
    }

    /**
     * @deprecated Use {@link DocumentVersions#placementBy(String, String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecDocumentVersion, VecPlacement> placementBy(final String compositionSpecificationId,
                                                                        final String occurrenceOrUsageId) {
        return DocumentVersions.placementBy(compositionSpecificationId, occurrenceOrUsageId)::orElseNull;
    }

    /**
     * @deprecated Use {@link DocumentVersions#geometryNode2dBy(VecNodeLocation)} instead, which searches all
     * {@link VecBuildingBlockSpecification2D}s of the document version rather than only the first one.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecDocumentVersion, VecGeometryNode2D> geometryNode2dBy(final VecNodeLocation location) {
        return DocumentVersions.geometryNode2dBy(location)::orElseNull;
    }

    /**
     * @deprecated Use {@link DocumentVersions#geometryNode3dBy(VecNodeLocation)} instead, which searches all
     * {@link VecBuildingBlockSpecification3D}s of the document version rather than only the first one.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecDocumentVersion, VecGeometryNode3D> geometryNode3dBy(final VecNodeLocation location) {
        return DocumentVersions.geometryNode3dBy(location)::orElseNull;
    }

    /**
     * @deprecated Use {@link DocumentVersions#viewItem2dBy(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecDocumentVersion, Optional<VecOccurrenceOrUsageViewItem2D>> viewItem2dBy(
            final String occurrenceOrUsageId) {
        return DocumentVersions.viewItem2dBy(occurrenceOrUsageId);
    }

    /**
     * @deprecated Use {@link DocumentVersions#viewItem3dBy(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecDocumentVersion, Optional<VecOccurrenceOrUsageViewItem3D>> viewItem3dBy(
            final String occurrenceOrUsageId) {
        return DocumentVersions.viewItem3dBy(occurrenceOrUsageId);
    }

    /**
     * @deprecated Use {@link DocumentVersions#buildingBlockSpecification2dBy(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecDocumentVersion, Optional<VecBuildingBlockSpecification2D>> buildingBlockSpecification2dBy(
            final String specificationId) {
        return DocumentVersions.buildingBlockSpecification2dBy(specificationId);
    }

    /**
     * @deprecated Use {@link DocumentVersions#buildingBlockSpecification3dBy(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecDocumentVersion, Optional<VecBuildingBlockSpecification3D>> buildingBlockSpecification3dBy(
            final String specificationId) {
        return DocumentVersions.buildingBlockSpecification3dBy(specificationId);
    }

    /**
     * @deprecated Use {@link DocumentVersions#positioning2dWith(VecBuildingBlockSpecification2D)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecDocumentVersion, Optional<VecBuildingBlockPositioning2D>> positioning2dWith(
            final VecBuildingBlockSpecification2D buildingBlock) {
        return DocumentVersions.positioning2dWith(buildingBlock);
    }

    /**
     * @deprecated Use {@link DocumentVersions#positioning3dWith(VecBuildingBlockSpecification3D)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecDocumentVersion, Optional<VecBuildingBlockPositioning3D>> positioning3dWith(
            final VecBuildingBlockSpecification3D buildingBlock) {
        return DocumentVersions.positioning3dWith(buildingBlock);
    }

}
