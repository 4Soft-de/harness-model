/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.transform.geometry.geo_2d;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BuildingBlockSpecification2DTransformerTest {

    @Test
    void should_transformBuildingBlockSpecification2D() {
        // Given
        final BuildingBlockSpecification2DTransformer transformer = new BuildingBlockSpecification2DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblHarness source = new KblHarness();

        final KBLContainer kblContainer = new KBLContainer();
        source.setParentKBLContainer(kblContainer);

        final KblCartesianPoint cartesianPoint = new KblCartesianPoint();
        cartesianPoint.getCoordinates().add(1.0);
        cartesianPoint.getCoordinates().add(2.0);
        kblContainer.getCartesianPoints().add(cartesianPoint);

        final VecCartesianPoint2D vecCartesianPoint2D = new VecCartesianPoint2D();
        orchestrator.addMockMapping(cartesianPoint, vecCartesianPoint2D);

        final KblNode node = new KblNode();
        kblContainer.getNodes().add(node);

        final VecGeometryNode2D vecGeometryNode2D = new VecGeometryNode2D();
        orchestrator.addMockMapping(node, vecGeometryNode2D);

        final KblSegment segment = new KblSegment();
        kblContainer.getSegments().add(segment);

        final VecGeometrySegment2D vecGeometrySegment2D = new VecGeometrySegment2D();
        orchestrator.addMockMapping(segment, vecGeometrySegment2D);

        final KblAssemblyPartOccurrence placeableOccurrence = new KblAssemblyPartOccurrence();
        source.getAssemblyPartOccurrences().add(placeableOccurrence);

        final VecOccurrenceOrUsageViewItem2D viewItem2D = new VecOccurrenceOrUsageViewItem2D();
        orchestrator.addMockMapping(placeableOccurrence, viewItem2D);

        // When
        final VecBuildingBlockSpecification2D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getGeometryNodes()).containsExactly(vecGeometryNode2D))
                .satisfies(v -> assertThat(v.getGeometrySegments()).containsExactly(vecGeometrySegment2D))
                .satisfies(v -> assertThat(v.getCartesianPoints()).containsExactly(vecCartesianPoint2D))
                .satisfies(v -> assertThat(v.getPlacedElementViewItems()).containsExactly(viewItem2D));
    }
}
