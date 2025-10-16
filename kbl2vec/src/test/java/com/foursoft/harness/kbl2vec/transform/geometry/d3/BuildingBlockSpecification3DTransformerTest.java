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
package com.foursoft.harness.kbl2vec.transform.geometry.d3;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecBuildingBlockSpecification3D;
import com.foursoft.harness.vec.v2x.VecCartesianPoint3D;
import com.foursoft.harness.vec.v2x.VecGeometryNode3D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment3D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BuildingBlockSpecification3DTransformerTest {

    @Test
    void should_transformBuildingBlockSpecification3D() {
        // Given
        final BuildingBlockSpecification3DTransformer transformer = new BuildingBlockSpecification3DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblHarness source = new KblHarness();

        final KBLContainer kblContainer = new KBLContainer();
        source.setParentKBLContainer(kblContainer);

        final KblCartesianPoint cartesianPoint = new KblCartesianPoint();
        cartesianPoint.getCoordinates().add(1.0);
        cartesianPoint.getCoordinates().add(2.0);
        cartesianPoint.getCoordinates().add(3.0);
        kblContainer.getCartesianPoints().add(cartesianPoint);

        final VecCartesianPoint3D vecCartesianPoint3D = new VecCartesianPoint3D();
        orchestrator.addMockMapping(cartesianPoint, vecCartesianPoint3D);

        final KblNode node = new KblNode();
        kblContainer.getNodes().add(node);

        final VecGeometryNode3D vecGeometryNode3D = new VecGeometryNode3D();
        orchestrator.addMockMapping(node, vecGeometryNode3D);

        final KblSegment segment = new KblSegment();
        kblContainer.getSegments().add(segment);

        final VecGeometrySegment3D vecGeometrySegment3D = new VecGeometrySegment3D();
        orchestrator.addMockMapping(segment, vecGeometrySegment3D);

        // When
        final VecBuildingBlockSpecification3D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getGeometryNodes()).containsExactly(vecGeometryNode3D))
                .satisfies(v -> assertThat(v.getGeometrySegments()).containsExactly(vecGeometrySegment3D))
                .satisfies(v -> assertThat(v.getCartesianPoints()).containsExactly(vecCartesianPoint3D));
    }
}
