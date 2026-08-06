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

import com.foursoft.harness.vec.common.HasModifiableIdentification;
import com.foursoft.harness.vec.v2x.VecBuildingBlockSpecification2D;
import com.foursoft.harness.vec.v2x.VecBuildingBlockSpecification3D;
import com.foursoft.harness.vec.v2x.VecCompositionSpecification;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecGeometryNode2D;
import com.foursoft.harness.vec.v2x.VecGeometryNode3D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment2D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment3D;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPartUsage;
import com.foursoft.harness.vec.v2x.VecPartUsageSpecification;
import com.foursoft.harness.vec.v2x.VecPlacementSpecification;
import com.foursoft.harness.vec.v2x.VecSheetOrChapter;
import com.foursoft.harness.vec.v2x.VecSpecification;
import com.foursoft.harness.vec.v2x.navigations.SpecificationNavs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpecificationsTest {

    private final VecDocumentVersion documentVersion = new VecDocumentVersion();
    private final VecBuildingBlockSpecification2D buildingBlock2D = new VecBuildingBlockSpecification2D();
    private final VecBuildingBlockSpecification3D buildingBlock3D = new VecBuildingBlockSpecification3D();

    private final VecGeometryNode2D node2D = identified(new VecGeometryNode2D(), "N-1");
    private final VecGeometryNode3D node3D = identified(new VecGeometryNode3D(), "N-1");
    private final VecGeometrySegment2D segment2D = identified(new VecGeometrySegment2D(), "S-1");
    private final VecGeometrySegment3D segment3D = identified(new VecGeometrySegment3D(), "S-1");

    private static <T extends HasModifiableIdentification> T identified(
            final T element, final String identification) {
        element.setIdentification(identification);
        return element;
    }

    @BeforeEach
    void setUp() {
        documentVersion.setDocumentNumber("DOC-1");
        buildingBlock2D.getGeometryNodes().add(node2D);
        buildingBlock2D.getGeometrySegments().add(segment2D);
        buildingBlock3D.getGeometryNodes().add(node3D);
        buildingBlock3D.getGeometrySegments().add(segment3D);
    }

    @Test
    void navigatesToTheParentDocumentVersionHeldByTheSpecificationItself() {
        final VecSpecification specification = new VecPlacementSpecification();
        specification.setParentDocumentVersion(documentVersion);

        assertThat(Specifications.toParentDocumentVersion().from(specification)).contains(documentVersion);
        assertThat(Specifications.toParentDocumentNumber().from(specification)).contains("DOC-1");
    }

    @Test
    void navigatesToTheParentDocumentVersionThroughTheSheetOrChapter() {
        final VecSheetOrChapter sheet = new VecSheetOrChapter();
        sheet.setParentDocumentVersion(documentVersion);
        final VecSpecification specification = new VecPlacementSpecification();
        specification.setParentSheetOrChapter(sheet);

        assertThat(Specifications.toParentDocumentVersion().from(specification)).contains(documentVersion);
    }

    @Test
    void navigatesToNoParentDocumentVersionForAnUnattachedSpecification() {
        assertThat(Specifications.toParentDocumentVersion().from(new VecPlacementSpecification())).isEmpty();
        assertThat(Specifications.toParentDocumentNumber().from(new VecPlacementSpecification())).isEmpty();
    }

    @Test
    void navigatesToTheComponentsAndPartUsagesOfASpecification() {
        final VecPartOccurrence component = new VecPartOccurrence();
        final VecCompositionSpecification composition = new VecCompositionSpecification();
        composition.getComponents().add(component);

        final VecPartUsage partUsage = new VecPartUsage();
        final VecPartUsageSpecification partUsages = new VecPartUsageSpecification();
        partUsages.getPartUsages().add(partUsage);

        assertThat(Specifications.toComponents().listFrom(composition)).containsExactly(component);
        assertThat(Specifications.toPartUsages().listFrom(partUsages)).containsExactly(partUsage);
    }

    @Test
    void navigatesToTheGeometryOfABuildingBlock() {
        assertThat(Specifications.toGeometryNodes2D().listFrom(buildingBlock2D)).containsExactly(node2D);
        assertThat(Specifications.toGeometryNodes3D().listFrom(buildingBlock3D)).containsExactly(node3D);
        assertThat(Specifications.toGeometrySegments2D().listFrom(buildingBlock2D)).containsExactly(segment2D);
        assertThat(Specifications.toGeometrySegments3D().listFrom(buildingBlock3D)).containsExactly(segment3D);
    }

    @Test
    void navigatesToTheGeometryWithTheGivenIdentification() {
        assertThat(Specifications.geometryNode2dBy("N-1").from(buildingBlock2D)).contains(node2D);
        assertThat(Specifications.geometryNode3dBy("N-1").from(buildingBlock3D)).contains(node3D);
        assertThat(Specifications.geometrySegment2dBy("S-1").from(buildingBlock2D)).contains(segment2D);
        assertThat(Specifications.geometrySegment3dBy("S-1").from(buildingBlock3D)).contains(segment3D);
    }

    @Test
    void navigatesToNoGeometryForAnUnknownIdentification() {
        assertThat(Specifications.geometryNode2dBy("N-2").from(buildingBlock2D)).isEmpty();
        assertThat(Specifications.geometrySegment3dBy("S-2").from(buildingBlock3D)).isEmpty();
    }

    /**
     * Characterisation test for the deprecated {@link SpecificationNavs}, which delegates to
     * {@link Specifications}. Can be removed together with {@link SpecificationNavs}.
     */
    @Test
    @SuppressWarnings({"deprecation", "removal"})
    void deprecatedNavigationsBehaveLikeTheirReplacement() {
        final VecSpecification specification = new VecPlacementSpecification();
        specification.setParentDocumentVersion(documentVersion);

        assertThat(SpecificationNavs.parentDocumentVersion().apply(specification)).isEqualTo(documentVersion);
        assertThat(SpecificationNavs.parentDocumentNumber().apply(specification)).isEqualTo("DOC-1");
        assertThat(SpecificationNavs.geometryNode2dBy("N-1").apply(buildingBlock2D)).isEqualTo(node2D);
        assertThat(SpecificationNavs.geometryNode3dBy("N-1").apply(buildingBlock3D)).isEqualTo(node3D);
        assertThat(SpecificationNavs.geometrySegment2dBy("S-1").apply(buildingBlock2D)).isEqualTo(segment2D);
        assertThat(SpecificationNavs.geometrySegment3dBy("S-1").apply(buildingBlock3D)).isEqualTo(segment3D);
        assertThat(SpecificationNavs.geometryNode2dBy("N-2").apply(buildingBlock2D)).isNull();
    }

}
