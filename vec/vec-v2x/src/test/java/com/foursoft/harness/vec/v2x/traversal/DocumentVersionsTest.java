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
import com.foursoft.harness.vec.v2x.VecNodeLocation;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsageViewItem2D;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsageViewItem3D;
import com.foursoft.harness.vec.v2x.VecOnPointPlacement;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v2x.VecPlacementSpecification;
import com.foursoft.harness.vec.v2x.VecTopologyNode;
import com.foursoft.harness.vec.v2x.VecTopologySegment;
import com.foursoft.harness.vec.v2x.navigations.DocumentVersionNavs;
import com.foursoft.harness.vec.v2x.navigations.PartOccurrenceOrUsageNavs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentVersionsTest {

    private static final String COMPOSITION_ID = "COMP-1";
    private static final String COMPONENT_ID = "OCC-1";

    private final VecDocumentVersion documentVersion = new VecDocumentVersion();

    private final VecBuildingBlockSpecification2D buildingBlock2D = new VecBuildingBlockSpecification2D();
    private final VecBuildingBlockSpecification3D buildingBlock3D = new VecBuildingBlockSpecification3D();
    private final VecPlacementSpecification placements = new VecPlacementSpecification();
    private final VecCompositionSpecification composition = new VecCompositionSpecification();
    private final VecHarnessDrawingSpecification2D drawing = new VecHarnessDrawingSpecification2D();
    private final VecHarnessGeometrySpecification3D geometry = new VecHarnessGeometrySpecification3D();

    private final VecTopologyNode topologyNode = new VecTopologyNode();
    private final VecTopologySegment topologySegment = new VecTopologySegment();
    private final VecGeometryNode2D node2D = new VecGeometryNode2D();
    private final VecGeometryNode3D node3D = new VecGeometryNode3D();
    private final VecGeometrySegment2D segment2D = new VecGeometrySegment2D();
    private final VecGeometrySegment3D segment3D = new VecGeometrySegment3D();
    private final VecNodeLocation location = new VecNodeLocation();
    private final VecOnPointPlacement placement = new VecOnPointPlacement();
    private final VecPlaceableElementRole placedElement = new VecPlaceableElementRole();
    private final VecPartOccurrence component = new VecPartOccurrence();
    private final VecOccurrenceOrUsageViewItem2D viewItem2D = new VecOccurrenceOrUsageViewItem2D();
    private final VecOccurrenceOrUsageViewItem3D viewItem3D = new VecOccurrenceOrUsageViewItem3D();
    private final VecBuildingBlockPositioning2D positioning2D = new VecBuildingBlockPositioning2D();
    private final VecBuildingBlockPositioning3D positioning3D = new VecBuildingBlockPositioning3D();

    @BeforeEach
    void setUp() {
        buildingBlock2D.setIdentification("BB-2D");
        buildingBlock3D.setIdentification("BB-3D");
        composition.setIdentification(COMPOSITION_ID);
        component.setIdentification(COMPONENT_ID);

        node2D.setReferenceNode(topologyNode);
        node3D.setReferenceNode(topologyNode);
        segment3D.setReferenceSegment(topologySegment);
        location.setReferencedNode(topologyNode);

        placement.getLocations().add(location);
        placement.getPlacedElement().add(placedElement);
        placedElement.getRefPlacement().add(placement);
        component.getRoles().add(placedElement);

        viewItem2D.getOccurrenceOrUsage().add(component);
        viewItem3D.getOccurrenceOrUsage().add(component);

        positioning2D.setReferenced2DBuildingBlock(buildingBlock2D);
        positioning3D.setReferenced3DBuildingBlock(buildingBlock3D);

        buildingBlock2D.getGeometryNodes().add(node2D);
        buildingBlock2D.getGeometrySegments().add(segment2D);
        buildingBlock2D.getPlacedElementViewItems().add(viewItem2D);
        buildingBlock3D.getGeometryNodes().add(node3D);
        buildingBlock3D.getGeometrySegments().add(segment3D);
        buildingBlock3D.getPlacedElementViewItem3Ds().add(viewItem3D);
        placements.getPlacements().add(placement);
        composition.getComponents().add(component);
        drawing.getBuildingBlockPositionings().add(positioning2D);
        geometry.getBuildingBlockPositionings().add(positioning3D);

        documentVersion.getSpecifications().add(buildingBlock2D);
        documentVersion.getSpecifications().add(buildingBlock3D);
        documentVersion.getSpecifications().add(placements);
        documentVersion.getSpecifications().add(composition);
        documentVersion.getSpecifications().add(drawing);
        documentVersion.getSpecifications().add(geometry);
    }

    @Test
    void navigatesToTheBuildingBlocksOfADocumentVersion() {
        assertThat(DocumentVersions.toBuildingBlockSpecifications2D().listFrom(documentVersion))
                .containsExactly(buildingBlock2D);
        assertThat(DocumentVersions.toBuildingBlockSpecifications3D().listFrom(documentVersion))
                .containsExactly(buildingBlock3D);
        assertThat(DocumentVersions.buildingBlockSpecification2dBy("BB-2D").from(documentVersion))
                .contains(buildingBlock2D);
        assertThat(DocumentVersions.buildingBlockSpecification3dBy("BB-2D").from(documentVersion)).isEmpty();
    }

    @Test
    void navigatesToTheGeometryOfAllBuildingBlocks() {
        assertThat(DocumentVersions.toGeometryNodes2D().listFrom(documentVersion)).containsExactly(node2D);
        assertThat(DocumentVersions.toGeometryNodes3D().listFrom(documentVersion)).containsExactly(node3D);
        assertThat(DocumentVersions.toGeometrySegments3D().listFrom(documentVersion)).containsExactly(segment3D);
        assertThat(DocumentVersions.toGeometrySegments2D().listFrom(documentVersion)).containsExactly(segment2D);
    }

    @Test
    void navigatesToTheGeometryOfTheGivenTopologyElement() {
        assertThat(DocumentVersions.geometryNodes3dBy(topologyNode).listFrom(documentVersion))
                .containsExactly(node3D);
        assertThat(DocumentVersions.geometryNodes3dBy(new VecTopologyNode()).listFrom(documentVersion)).isEmpty();
        assertThat(DocumentVersions.geometrySegments3dBy(topologySegment).listFrom(documentVersion))
                .containsExactly(segment3D);
        assertThat(DocumentVersions.geometryNode2dBy(location).from(documentVersion)).contains(node2D);
        assertThat(DocumentVersions.geometryNode3dBy(location).from(documentVersion)).contains(node3D);
    }

    @Test
    void navigatesToTheViewItemsOfADocumentVersion() {
        assertThat(DocumentVersions.toViewItems2D().listFrom(documentVersion)).containsExactly(viewItem2D);
        assertThat(DocumentVersions.toViewItems3D().listFrom(documentVersion)).containsExactly(viewItem3D);
        assertThat(DocumentVersions.viewItems3dBy(component).listFrom(documentVersion)).containsExactly(viewItem3D);
        assertThat(DocumentVersions.viewItem2dBy(COMPONENT_ID).from(documentVersion)).contains(viewItem2D);
        assertThat(DocumentVersions.viewItem3dBy(COMPONENT_ID).from(documentVersion)).contains(viewItem3D);
        assertThat(DocumentVersions.viewItem2dBy("OCC-2").from(documentVersion)).isEmpty();
    }

    @Test
    void navigatesToThePlacementsAndNodeLocationsOfADocumentVersion() {
        assertThat(DocumentVersions.toPlacements().listFrom(documentVersion)).containsExactly(placement);
        assertThat(DocumentVersions.toNodeLocations().listFrom(documentVersion)).containsExactly(location);
        assertThat(DocumentVersions.placementsOf(placedElement).listFrom(documentVersion))
                .containsExactly(placement);
        assertThat(DocumentVersions.nodeLocationsOf(placedElement).listFrom(documentVersion))
                .containsExactly(location);
        assertThat(DocumentVersions.nodeLocationsOf(new VecPlaceableElementRole()).listFrom(documentVersion))
                .isEmpty();
    }

    @Test
    void navigatesToTheBuildingBlockPositionings() {
        assertThat(DocumentVersions.toBuildingBlockPositionings2D().listFrom(documentVersion))
                .containsExactly(positioning2D);
        assertThat(DocumentVersions.toBuildingBlockPositionings3D().listFrom(documentVersion))
                .containsExactly(positioning3D);
        assertThat(DocumentVersions.positioning2dWith(buildingBlock2D).from(documentVersion))
                .contains(positioning2D);
        assertThat(DocumentVersions.positioning3dWith(buildingBlock3D).from(documentVersion))
                .contains(positioning3D);
        assertThat(DocumentVersions.positioning2dWith(new VecBuildingBlockSpecification2D())
                           .from(documentVersion)).isEmpty();
    }

    @Test
    void navigatesToTheRoleAndPlacementOfTheIdentifiedComponent() {
        assertThat(DocumentVersions.placeableElementRoleBy(COMPOSITION_ID, COMPONENT_ID).from(documentVersion))
                .contains(placedElement);
        assertThat(DocumentVersions.placementBy(COMPOSITION_ID, COMPONENT_ID).from(documentVersion))
                .contains(placement);
        assertThat(DocumentVersions.placeableElementRoleBy(COMPOSITION_ID, "OCC-2").from(documentVersion))
                .isEmpty();
        assertThat(DocumentVersions.placementBy("COMP-2", COMPONENT_ID).from(documentVersion)).isEmpty();
    }

    @Test
    void navigatesToTheTopologyNodeOfAComponent() {
        assertThat(DocumentVersions.topologyNodeBy(COMPONENT_ID).from(documentVersion)).contains(topologyNode);
        assertThat(DocumentVersions.topologyNodeBy("OCC-2").from(documentVersion)).isEmpty();
        assertThat(DocumentVersions.topologyNodeOf(component).from(documentVersion)).contains(topologyNode);
        assertThat(DocumentVersions.topologyNodeOf(new VecPartOccurrence()).from(documentVersion)).isEmpty();
    }

    /**
     * Characterisation test for the deprecated {@link DocumentVersionNavs}, which delegates to
     * {@link DocumentVersions}. Can be removed together with {@link DocumentVersionNavs}.
     */
    @Test
    @SuppressWarnings({"deprecation", "removal"})
    void deprecatedNavigationsBehaveLikeTheirReplacement() {
        assertThat(DocumentVersionNavs.geometryNodes3dBy(topologyNode).apply(documentVersion))
                .isEqualTo(DocumentVersions.geometryNodes3dBy(topologyNode).listFrom(documentVersion));
        assertThat(DocumentVersionNavs.geometrySegments3dBy(topologySegment).apply(documentVersion))
                .isEqualTo(DocumentVersions.geometrySegments3dBy(topologySegment).listFrom(documentVersion));
        assertThat(DocumentVersionNavs.viewItems3dBy(component).apply(documentVersion))
                .isEqualTo(DocumentVersions.viewItems3dBy(component).listFrom(documentVersion));
        assertThat(DocumentVersionNavs.topologyNodeBy(COMPONENT_ID).apply(documentVersion))
                .isEqualTo(DocumentVersions.topologyNodeBy(COMPONENT_ID).from(documentVersion));
        assertThat(DocumentVersionNavs.nodeLocationsBy(placedElement).apply(documentVersion))
                .isEqualTo(DocumentVersions.nodeLocationsOf(placedElement).listFrom(documentVersion));
        assertThat(DocumentVersionNavs.nodeLocationsBy(null).apply(documentVersion))
                .isEqualTo(DocumentVersions.toNodeLocations().listFrom(documentVersion));
        assertThat(DocumentVersionNavs.placeableElementRoleBy(COMPOSITION_ID, COMPONENT_ID).apply(documentVersion))
                .isEqualTo(placedElement);
        assertThat(DocumentVersionNavs.placementBy(COMPOSITION_ID, COMPONENT_ID).apply(documentVersion))
                .isEqualTo(placement);
        assertThat(DocumentVersionNavs.placeableElementRoleBy(COMPOSITION_ID, "OCC-2").apply(documentVersion))
                .isNull();
        assertThat(DocumentVersionNavs.geometryNode2dBy(location).apply(documentVersion)).isEqualTo(node2D);
        assertThat(DocumentVersionNavs.geometryNode3dBy(location).apply(documentVersion)).isEqualTo(node3D);
        assertThat(DocumentVersionNavs.viewItem2dBy(COMPONENT_ID).apply(documentVersion))
                .isEqualTo(DocumentVersions.viewItem2dBy(COMPONENT_ID).from(documentVersion));
        assertThat(DocumentVersionNavs.viewItem3dBy(COMPONENT_ID).apply(documentVersion))
                .isEqualTo(DocumentVersions.viewItem3dBy(COMPONENT_ID).from(documentVersion));
        assertThat(DocumentVersionNavs.buildingBlockSpecification2dBy("BB-2D").apply(documentVersion))
                .isEqualTo(DocumentVersions.buildingBlockSpecification2dBy("BB-2D").from(documentVersion));
        assertThat(DocumentVersionNavs.buildingBlockSpecification3dBy("BB-3D").apply(documentVersion))
                .isEqualTo(DocumentVersions.buildingBlockSpecification3dBy("BB-3D").from(documentVersion));
        assertThat(DocumentVersionNavs.positioning2dWith(buildingBlock2D).apply(documentVersion))
                .isEqualTo(DocumentVersions.positioning2dWith(buildingBlock2D).from(documentVersion));
        assertThat(DocumentVersionNavs.positioning3dWith(buildingBlock3D).apply(documentVersion))
                .isEqualTo(DocumentVersions.positioning3dWith(buildingBlock3D).from(documentVersion));
        assertThat(PartOccurrenceOrUsageNavs.findNodeOfComponent().apply(component, documentVersion))
                .isEqualTo(DocumentVersions.topologyNodeOf(component).from(documentVersion));
    }

}
