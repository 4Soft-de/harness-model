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

import com.foursoft.harness.vec.common.HasIdentification;
import com.foursoft.harness.vec.common.traversal.MultiNavigation;
import com.foursoft.harness.vec.common.traversal.SingleNavigation;
import com.foursoft.harness.vec.v12x.HasOccurrenceOrUsages;
import com.foursoft.harness.vec.v12x.VecBuildingBlockPositioning2D;
import com.foursoft.harness.vec.v12x.VecBuildingBlockPositioning3D;
import com.foursoft.harness.vec.v12x.VecBuildingBlockSpecification2D;
import com.foursoft.harness.vec.v12x.VecBuildingBlockSpecification3D;
import com.foursoft.harness.vec.v12x.VecDocumentVersion;
import com.foursoft.harness.vec.v12x.VecGeometryNode;
import com.foursoft.harness.vec.v12x.VecGeometryNode2D;
import com.foursoft.harness.vec.v12x.VecGeometryNode3D;
import com.foursoft.harness.vec.v12x.VecGeometrySegment2D;
import com.foursoft.harness.vec.v12x.VecGeometrySegment3D;
import com.foursoft.harness.vec.v12x.VecHarnessDrawingSpecification2D;
import com.foursoft.harness.vec.v12x.VecHarnessGeometrySpecification3D;
import com.foursoft.harness.vec.v12x.VecNodeLocation;
import com.foursoft.harness.vec.v12x.VecOccurrenceOrUsage;
import com.foursoft.harness.vec.v12x.VecOccurrenceOrUsageViewItem2D;
import com.foursoft.harness.vec.v12x.VecOccurrenceOrUsageViewItem3D;
import com.foursoft.harness.vec.v12x.VecOnPointPlacement;
import com.foursoft.harness.vec.v12x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v12x.VecPlacement;
import com.foursoft.harness.vec.v12x.VecPlacementSpecification;
import com.foursoft.harness.vec.v12x.VecSpecification;
import com.foursoft.harness.vec.v12x.VecTopologyNode;
import com.foursoft.harness.vec.v12x.VecTopologySegment;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Navigations starting at a {@link VecDocumentVersion}.
 * <p>
 * A document version reaches most of its content through its {@link VecSpecification}s, so the steps here are
 * composed of {@link SpecificationOwners#toSpecifications()} and the matching step of {@link Specifications}.
 * The navigations taking an argument select a single element among those, which is why they keep a noun
 * phrase as their name.
 */
public final class DocumentVersions {

    private DocumentVersions() {
        // hide default constructor
    }

    /**
     * Navigates to the 2D building blocks of a document version.
     *
     * @return A navigation to the 2D building block specifications of a document version.
     */
    public static MultiNavigation<VecDocumentVersion, VecBuildingBlockSpecification2D>
    toBuildingBlockSpecifications2D() {
        return SpecificationOwners.<VecDocumentVersion>toSpecifications()
                .ofType(VecBuildingBlockSpecification2D.class);
    }

    /**
     * Navigates to the 3D building blocks of a document version.
     *
     * @return A navigation to the 3D building block specifications of a document version.
     */
    public static MultiNavigation<VecDocumentVersion, VecBuildingBlockSpecification3D>
    toBuildingBlockSpecifications3D() {
        return SpecificationOwners.<VecDocumentVersion>toSpecifications()
                .ofType(VecBuildingBlockSpecification3D.class);
    }

    /**
     * Navigates to the 2D geometry nodes of all building blocks of a document version.
     *
     * @return A navigation to the 2D geometry nodes of a document version.
     */
    public static MultiNavigation<VecDocumentVersion, VecGeometryNode2D> toGeometryNodes2D() {
        return toBuildingBlockSpecifications2D().then(Specifications.toGeometryNodes2D());
    }

    /**
     * Navigates to the 3D geometry nodes of all building blocks of a document version.
     *
     * @return A navigation to the 3D geometry nodes of a document version.
     */
    public static MultiNavigation<VecDocumentVersion, VecGeometryNode3D> toGeometryNodes3D() {
        return toBuildingBlockSpecifications3D().then(Specifications.toGeometryNodes3D());
    }

    /**
     * Navigates to the 2D geometry segments of all building blocks of a document version.
     *
     * @return A navigation to the 2D geometry segments of a document version.
     */
    public static MultiNavigation<VecDocumentVersion, VecGeometrySegment2D> toGeometrySegments2D() {
        return toBuildingBlockSpecifications2D().then(Specifications.toGeometrySegments2D());
    }

    /**
     * Navigates to the 3D geometry segments of all building blocks of a document version.
     *
     * @return A navigation to the 3D geometry segments of a document version.
     */
    public static MultiNavigation<VecDocumentVersion, VecGeometrySegment3D> toGeometrySegments3D() {
        return toBuildingBlockSpecifications3D().then(Specifications.toGeometrySegments3D());
    }

    /**
     * Navigates to the 2D view items of all building blocks of a document version.
     *
     * @return A navigation to the 2D view items of a document version.
     */
    public static MultiNavigation<VecDocumentVersion, VecOccurrenceOrUsageViewItem2D> toViewItems2D() {
        return toBuildingBlockSpecifications2D().then(Specifications.toViewItems2D());
    }

    /**
     * Navigates to the 3D view items of all building blocks of a document version.
     *
     * @return A navigation to the 3D view items of a document version.
     */
    public static MultiNavigation<VecDocumentVersion, VecOccurrenceOrUsageViewItem3D> toViewItems3D() {
        return toBuildingBlockSpecifications3D().then(Specifications.toViewItems3D());
    }

    /**
     * Navigates to the placements of all {@link VecPlacementSpecification}s of a document version.
     *
     * @return A navigation to the placements of a document version.
     */
    public static MultiNavigation<VecDocumentVersion, VecPlacement> toPlacements() {
        return SpecificationOwners.<VecDocumentVersion>toSpecifications()
                .ofType(VecPlacementSpecification.class)
                .then(Specifications.toPlacements());
    }

    /**
     * Navigates to the {@link VecNodeLocation}s of all on point placements of a document version.
     *
     * @return A navigation to the node locations of a document version.
     */
    public static MultiNavigation<VecDocumentVersion, VecNodeLocation> toNodeLocations() {
        return toPlacements()
                .ofType(VecOnPointPlacement.class)
                .then(Placements.toLocations())
                .ofType(VecNodeLocation.class);
    }

    /**
     * Navigates to the 2D building block positionings of all
     * {@link VecHarnessDrawingSpecification2D}s of a document version.
     *
     * @return A navigation to the 2D building block positionings of a document version.
     */
    public static MultiNavigation<VecDocumentVersion, VecBuildingBlockPositioning2D>
    toBuildingBlockPositionings2D() {
        return SpecificationOwners.<VecDocumentVersion>toSpecifications()
                .ofType(VecHarnessDrawingSpecification2D.class)
                .then(Specifications.toBuildingBlockPositionings2D());
    }

    /**
     * Navigates to the 3D building block positionings of all
     * {@link VecHarnessGeometrySpecification3D}s of a document version.
     *
     * @return A navigation to the 3D building block positionings of a document version.
     */
    public static MultiNavigation<VecDocumentVersion, VecBuildingBlockPositioning3D>
    toBuildingBlockPositionings3D() {
        return SpecificationOwners.<VecDocumentVersion>toSpecifications()
                .ofType(VecHarnessGeometrySpecification3D.class)
                .then(Specifications.toBuildingBlockPositionings3D());
    }

    /**
     * Navigates to the 2D building block with the given identification.
     *
     * @param specificationId Identification of the building block to navigate to.
     * @return A navigation to the identified 2D building block specification.
     */
    public static SingleNavigation<VecDocumentVersion, VecBuildingBlockSpecification2D>
    buildingBlockSpecification2dBy(final String specificationId) {
        return toBuildingBlockSpecifications2D().filter(identifiedBy(specificationId)).atMostOne();
    }

    /**
     * Navigates to the 3D building block with the given identification.
     *
     * @param specificationId Identification of the building block to navigate to.
     * @return A navigation to the identified 3D building block specification.
     */
    public static SingleNavigation<VecDocumentVersion, VecBuildingBlockSpecification3D>
    buildingBlockSpecification3dBy(final String specificationId) {
        return toBuildingBlockSpecifications3D().filter(identifiedBy(specificationId)).atMostOne();
    }

    /**
     * Navigates to the 3D geometry nodes representing the given {@link VecTopologyNode}.
     *
     * @param topologyNode Topology node the geometry nodes have to reference.
     * @return A navigation to the 3D geometry nodes of the given topology node.
     */
    public static MultiNavigation<VecDocumentVersion, VecGeometryNode3D> geometryNodes3dBy(
            final VecTopologyNode topologyNode) {
        return toGeometryNodes3D().filter(referencing(topologyNode));
    }

    /**
     * Navigates to the 3D geometry segments representing the given {@link VecTopologySegment}.
     *
     * @param topologySegment Topology segment the geometry segments have to reference.
     * @return A navigation to the 3D geometry segments of the given topology segment.
     */
    public static MultiNavigation<VecDocumentVersion, VecGeometrySegment3D> geometrySegments3dBy(
            final VecTopologySegment topologySegment) {
        return toGeometrySegments3D()
                .filter(segment -> Objects.equals(topologySegment, segment.getReferenceSegment()));
    }

    /**
     * Navigates to the 2D geometry node representing the topology node of the given {@link VecNodeLocation}.
     *
     * @param location Location whose referenced topology node the geometry node has to reference.
     * @return A navigation to the 2D geometry node of the given location.
     */
    public static SingleNavigation<VecDocumentVersion, VecGeometryNode2D> geometryNode2dBy(
            final VecNodeLocation location) {
        return toGeometryNodes2D().filter(referencing(location.getReferencedNode())).atMostOne();
    }

    /**
     * Navigates to the 3D geometry node representing the topology node of the given {@link VecNodeLocation}.
     *
     * @param location Location whose referenced topology node the geometry node has to reference.
     * @return A navigation to the 3D geometry node of the given location.
     */
    public static SingleNavigation<VecDocumentVersion, VecGeometryNode3D> geometryNode3dBy(
            final VecNodeLocation location) {
        return toGeometryNodes3D().filter(referencing(location.getReferencedNode())).atMostOne();
    }

    /**
     * Navigates to the 3D view items showing the given occurrence or usage.
     *
     * @param occurrenceOrUsage Occurrence or usage the view items have to show.
     * @return A navigation to the 3D view items of the given occurrence or usage.
     */
    public static MultiNavigation<VecDocumentVersion, VecOccurrenceOrUsageViewItem3D> viewItems3dBy(
            final VecOccurrenceOrUsage occurrenceOrUsage) {
        return toViewItems3D().filter(viewItem -> viewItem.getOccurrenceOrUsage().contains(occurrenceOrUsage));
    }

    /**
     * Navigates to the 2D view item showing the occurrence or usage with the given identification.
     *
     * @param occurrenceOrUsageId Identification of the occurrence or usage the view item has to show.
     * @return A navigation to the 2D view item of the identified occurrence or usage.
     */
    public static SingleNavigation<VecDocumentVersion, VecOccurrenceOrUsageViewItem2D> viewItem2dBy(
            final String occurrenceOrUsageId) {
        return toViewItems2D().filter(showing(occurrenceOrUsageId)).atMostOne();
    }

    /**
     * Navigates to the 3D view item showing the occurrence or usage with the given identification.
     *
     * @param occurrenceOrUsageId Identification of the occurrence or usage the view item has to show.
     * @return A navigation to the 3D view item of the identified occurrence or usage.
     */
    public static SingleNavigation<VecDocumentVersion, VecOccurrenceOrUsageViewItem3D> viewItem3dBy(
            final String occurrenceOrUsageId) {
        return toViewItems3D().filter(showing(occurrenceOrUsageId)).atMostOne();
    }

    /**
     * Navigates to the 2D building block positioning referencing the given building block.
     *
     * @param buildingBlock Building block the positioning has to reference.
     * @return A navigation to the positioning of the given 2D building block.
     */
    public static SingleNavigation<VecDocumentVersion, VecBuildingBlockPositioning2D> positioning2dWith(
            final VecBuildingBlockSpecification2D buildingBlock) {
        return toBuildingBlockPositionings2D()
                .filter(positioning -> Objects.equals(buildingBlock, positioning.getReferenced2DBuildingBlock()))
                .atMostOne();
    }

    /**
     * Navigates to the 3D building block positioning referencing the given building block.
     *
     * @param buildingBlock Building block the positioning has to reference.
     * @return A navigation to the positioning of the given 3D building block.
     */
    public static SingleNavigation<VecDocumentVersion, VecBuildingBlockPositioning3D> positioning3dWith(
            final VecBuildingBlockSpecification3D buildingBlock) {
        return toBuildingBlockPositionings3D()
                .filter(positioning -> Objects.equals(buildingBlock, positioning.getReferenced3DBuildingBlock()))
                .atMostOne();
    }

    /**
     * Navigates to the placements placing the given {@link VecPlaceableElementRole}.
     *
     * @param placedElement Role the placements have to place.
     * @return A navigation to the placements of the given role.
     */
    public static MultiNavigation<VecDocumentVersion, VecPlacement> placementsOf(
            final VecPlaceableElementRole placedElement) {
        return toPlacements().filter(placing(placedElement));
    }

    /**
     * Navigates to the {@link VecNodeLocation}s of the on point placements placing the given
     * {@link VecPlaceableElementRole}.
     *
     * @param placedElement Role the placements have to place.
     * @return A navigation to the node locations of the given role.
     */
    public static MultiNavigation<VecDocumentVersion, VecNodeLocation> nodeLocationsOf(
            final VecPlaceableElementRole placedElement) {
        return toPlacements()
                .ofType(VecOnPointPlacement.class)
                .filter(placing(placedElement))
                .then(Placements.toLocations())
                .ofType(VecNodeLocation.class);
    }

    /**
     * Navigates to the {@link VecPlaceableElementRole} of the component with the given identification, taken
     * from the composition specification with the given identification.
     *
     * @param compositionSpecificationId Identification of the composition specification holding the component.
     * @param occurrenceOrUsageId        Identification of the component.
     * @return A navigation to the placeable element role of the identified component.
     */
    public static SingleNavigation<VecDocumentVersion, VecPlaceableElementRole> placeableElementRoleBy(
            final String compositionSpecificationId, final String occurrenceOrUsageId) {
        return SpecificationOwners.<VecDocumentVersion>componentsBy(compositionSpecificationId)
                .filter(identifiedBy(occurrenceOrUsageId))
                .then(OccurrenceOrUsages.toRoles())
                .ofType(VecPlaceableElementRole.class)
                .atMostOne();
    }

    /**
     * Navigates to the placement of the component with the given identification, taken from the composition
     * specification with the given identification.
     *
     * @param compositionSpecificationId Identification of the composition specification holding the component.
     * @param occurrenceOrUsageId        Identification of the component.
     * @return A navigation to the placement of the identified component.
     * @see #placeableElementRoleBy(String, String)
     */
    public static SingleNavigation<VecDocumentVersion, VecPlacement> placementBy(
            final String compositionSpecificationId, final String occurrenceOrUsageId) {
        final SingleNavigation<VecDocumentVersion, VecPlaceableElementRole> placedElement =
                placeableElementRoleBy(compositionSpecificationId, occurrenceOrUsageId);
        return documentVersion -> placedElement.from(documentVersion)
                .flatMap(role -> placementsOf(role).atMostOne().from(documentVersion));
    }

    /**
     * Navigates to the {@link VecTopologyNode} the component with the given identification is placed on.
     *
     * @param occurrenceIdentification Identification of the component.
     * @return A navigation to the topology node of the identified component.
     */
    public static SingleNavigation<VecDocumentVersion, VecTopologyNode> topologyNodeBy(
            final String occurrenceIdentification) {
        return SpecificationOwners.<VecDocumentVersion>toComponents()
                .filter(identifiedBy(occurrenceIdentification))
                .then(OccurrenceOrUsages.toReferencedTopologyNode())
                .atMostOne();
    }

    /**
     * Navigates to the {@link VecTopologyNode} the given component is placed on, by looking up the node
     * locations of its {@link VecPlaceableElementRole} in the document version.
     *
     * @param component Occurrence or usage to find the topology node of.
     * @return A navigation to the topology node of the given component.
     */
    public static SingleNavigation<VecDocumentVersion, VecTopologyNode> topologyNodeOf(
            final VecOccurrenceOrUsage component) {
        return documentVersion -> OccurrenceOrUsages.toPlaceableElementRole()
                .from(component)
                .flatMap(role -> nodeLocationsOf(role).atMostOne().from(documentVersion))
                .map(VecNodeLocation::getReferencedNode);
    }

    private static Predicate<VecGeometryNode> referencing(final VecTopologyNode topologyNode) {
        return geometryNode -> Objects.equals(topologyNode, geometryNode.getReferenceNode());
    }

    private static Predicate<VecPlacement> placing(final VecPlaceableElementRole placedElement) {
        return placement -> placement.getPlacedElement().contains(placedElement);
    }

    private static Predicate<HasIdentification> identifiedBy(final String identification) {
        return element -> identification.equals(element.getIdentification());
    }

    private static Predicate<HasOccurrenceOrUsages> showing(final String occurrenceOrUsageId) {
        return viewItem -> ViewItems.toOccurrenceOrUsages()
                .atMostOne()
                .from(viewItem)
                .map(VecOccurrenceOrUsage::getIdentification)
                .filter(occurrenceOrUsageId::equals)
                .isPresent();
    }

}
