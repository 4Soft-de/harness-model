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

import com.foursoft.harness.vec.common.HasIdentification;
import com.foursoft.harness.vec.common.annotations.RequiresBackReferences;
import com.foursoft.harness.vec.common.traversal.MultiNavigation;
import com.foursoft.harness.vec.common.traversal.Navigations;
import com.foursoft.harness.vec.common.traversal.SingleNavigation;
import com.foursoft.harness.vec.v2x.VecBuildingBlockPositioning2D;
import com.foursoft.harness.vec.v2x.VecBuildingBlockPositioning3D;
import com.foursoft.harness.vec.v2x.VecBuildingBlockSpecification2D;
import com.foursoft.harness.vec.v2x.VecBuildingBlockSpecification3D;
import com.foursoft.harness.vec.v2x.VecCompositionSpecification;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecGeometryNode2D;
import com.foursoft.harness.vec.v2x.VecGeometryNode3D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment2D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment3D;
import com.foursoft.harness.vec.v2x.VecHarnessDrawingSpecification2D;
import com.foursoft.harness.vec.v2x.VecHarnessGeometrySpecification3D;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsageViewItem2D;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsageViewItem3D;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPartUsage;
import com.foursoft.harness.vec.v2x.VecPartUsageSpecification;
import com.foursoft.harness.vec.v2x.VecPlacement;
import com.foursoft.harness.vec.v2x.VecPlacementSpecification;
import com.foursoft.harness.vec.v2x.VecSheetOrChapter;
import com.foursoft.harness.vec.v2x.VecSpecification;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Navigations starting at a {@link VecSpecification} or one of its sub types.
 * <p>
 * The steps of the sub types name the elements they lead to, which is what the {@code 2D} and {@code 3D} in
 * their names refer to. Where a document version rather than a single specification is at hand,
 * {@link DocumentVersions} offers the same targets from there.
 */
public final class Specifications {

    private Specifications() {
        // hide default constructor
    }

    /**
     * Navigates to the {@link VecDocumentVersion} a specification belongs to, whether it is held by the
     * document version directly or by one of its {@link VecSheetOrChapter}s.
     *
     * @return A navigation to the parent document version of a specification.
     */
    @RequiresBackReferences
    public static SingleNavigation<VecSpecification, VecDocumentVersion> toParentDocumentVersion() {
        return specification -> Optional.ofNullable(specification.getParentDocumentVersion())
                .or(() -> Optional.ofNullable(specification.getParentSheetOrChapter())
                        .map(VecSheetOrChapter::getParentDocumentVersion));
    }

    /**
     * Navigates to the document number of the {@link VecDocumentVersion} a specification belongs to.
     *
     * @return A navigation to the parent document number of a specification.
     * @see #toParentDocumentVersion()
     */
    @RequiresBackReferences
    public static SingleNavigation<VecSpecification, String> toParentDocumentNumber() {
        return toParentDocumentVersion().then(Navigations.nullable(VecDocumentVersion::getDocumentNumber));
    }

    /**
     * Navigates to the components of a {@link VecCompositionSpecification}.
     *
     * @return A navigation to the components of a composition specification.
     */
    public static MultiNavigation<VecCompositionSpecification, VecPartOccurrence> toComponents() {
        return Navigations.collection(VecCompositionSpecification::getComponents);
    }

    /**
     * Navigates to the part usages of a {@link VecPartUsageSpecification}.
     *
     * @return A navigation to the part usages of a part usage specification.
     */
    public static MultiNavigation<VecPartUsageSpecification, VecPartUsage> toPartUsages() {
        return Navigations.collection(VecPartUsageSpecification::getPartUsages);
    }

    /**
     * Navigates to the placements of a {@link VecPlacementSpecification}.
     *
     * @return A navigation to the placements of a placement specification.
     */
    public static MultiNavigation<VecPlacementSpecification, VecPlacement> toPlacements() {
        return Navigations.collection(VecPlacementSpecification::getPlacements);
    }

    /**
     * Navigates to the geometry nodes of a {@link VecBuildingBlockSpecification2D}.
     *
     * @return A navigation to the 2D geometry nodes of a building block.
     */
    public static MultiNavigation<VecBuildingBlockSpecification2D, VecGeometryNode2D> toGeometryNodes2D() {
        return Navigations.collection(VecBuildingBlockSpecification2D::getGeometryNodes);
    }

    /**
     * Navigates to the geometry nodes of a {@link VecBuildingBlockSpecification3D}.
     *
     * @return A navigation to the 3D geometry nodes of a building block.
     */
    public static MultiNavigation<VecBuildingBlockSpecification3D, VecGeometryNode3D> toGeometryNodes3D() {
        return Navigations.collection(VecBuildingBlockSpecification3D::getGeometryNodes);
    }

    /**
     * Navigates to the geometry segments of a {@link VecBuildingBlockSpecification2D}.
     *
     * @return A navigation to the 2D geometry segments of a building block.
     */
    public static MultiNavigation<VecBuildingBlockSpecification2D, VecGeometrySegment2D> toGeometrySegments2D() {
        return Navigations.collection(VecBuildingBlockSpecification2D::getGeometrySegments);
    }

    /**
     * Navigates to the geometry segments of a {@link VecBuildingBlockSpecification3D}.
     *
     * @return A navigation to the 3D geometry segments of a building block.
     */
    public static MultiNavigation<VecBuildingBlockSpecification3D, VecGeometrySegment3D> toGeometrySegments3D() {
        return Navigations.collection(VecBuildingBlockSpecification3D::getGeometrySegments);
    }

    /**
     * Navigates to the view items of a {@link VecBuildingBlockSpecification2D}.
     *
     * @return A navigation to the 2D view items of a building block.
     */
    public static MultiNavigation<VecBuildingBlockSpecification2D, VecOccurrenceOrUsageViewItem2D> toViewItems2D() {
        return Navigations.collection(VecBuildingBlockSpecification2D::getPlacedElementViewItems);
    }

    /**
     * Navigates to the view items of a {@link VecBuildingBlockSpecification3D}.
     *
     * @return A navigation to the 3D view items of a building block.
     */
    public static MultiNavigation<VecBuildingBlockSpecification3D, VecOccurrenceOrUsageViewItem3D> toViewItems3D() {
        return Navigations.collection(VecBuildingBlockSpecification3D::getPlacedElementViewItem3Ds);
    }

    /**
     * Navigates to the building block positionings of a {@link VecHarnessDrawingSpecification2D}.
     *
     * @return A navigation to the 2D building block positionings of a harness drawing.
     */
    public static MultiNavigation<VecHarnessDrawingSpecification2D, VecBuildingBlockPositioning2D>
    toBuildingBlockPositionings2D() {
        return Navigations.collection(VecHarnessDrawingSpecification2D::getBuildingBlockPositionings);
    }

    /**
     * Navigates to the building block positionings of a {@link VecHarnessGeometrySpecification3D}.
     *
     * @return A navigation to the 3D building block positionings of a harness geometry.
     */
    public static MultiNavigation<VecHarnessGeometrySpecification3D, VecBuildingBlockPositioning3D>
    toBuildingBlockPositionings3D() {
        return Navigations.collection(VecHarnessGeometrySpecification3D::getBuildingBlockPositionings);
    }

    /**
     * Navigates to the geometry node of a {@link VecBuildingBlockSpecification2D} with the given identification.
     *
     * @param nodeId Identification of the geometry node to navigate to.
     * @return A navigation to the identified 2D geometry node.
     */
    public static SingleNavigation<VecBuildingBlockSpecification2D, VecGeometryNode2D> geometryNode2dBy(
            final String nodeId) {
        return toGeometryNodes2D().filter(identifiedBy(nodeId)).atMostOne();
    }

    /**
     * Navigates to the geometry node of a {@link VecBuildingBlockSpecification3D} with the given identification.
     *
     * @param nodeId Identification of the geometry node to navigate to.
     * @return A navigation to the identified 3D geometry node.
     */
    public static SingleNavigation<VecBuildingBlockSpecification3D, VecGeometryNode3D> geometryNode3dBy(
            final String nodeId) {
        return toGeometryNodes3D().filter(identifiedBy(nodeId)).atMostOne();
    }

    /**
     * Navigates to the geometry segment of a {@link VecBuildingBlockSpecification2D} with the given
     * identification.
     *
     * @param segmentId Identification of the geometry segment to navigate to.
     * @return A navigation to the identified 2D geometry segment.
     */
    public static SingleNavigation<VecBuildingBlockSpecification2D, VecGeometrySegment2D> geometrySegment2dBy(
            final String segmentId) {
        return toGeometrySegments2D().filter(identifiedBy(segmentId)).atMostOne();
    }

    /**
     * Navigates to the geometry segment of a {@link VecBuildingBlockSpecification3D} with the given
     * identification.
     *
     * @param segmentId Identification of the geometry segment to navigate to.
     * @return A navigation to the identified 3D geometry segment.
     */
    public static SingleNavigation<VecBuildingBlockSpecification3D, VecGeometrySegment3D> geometrySegment3dBy(
            final String segmentId) {
        return toGeometrySegments3D().filter(identifiedBy(segmentId)).atMostOne();
    }

    private static Predicate<HasIdentification> identifiedBy(final String identification) {
        return element -> element.getIdentification().equals(identification);
    }

}
