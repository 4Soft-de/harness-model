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

import com.foursoft.harness.kbl.v25.KblNode;
import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecGeometryNode2D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment2D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GeometrySegment2DTransformerTest {

    @Test
    void should_transformGeometry2DTransformer() {
        // Given
        final GeometrySegment2DTransformer transformer = new GeometrySegment2DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblSegment source = new KblSegment();
        source.setId("TestId");

        source.getStartVectors().add(1.0);
        source.getStartVectors().add(2.0);
        source.getEndVectors().add(1.0);
        source.getEndVectors().add(2.0);

        final KblNode startNode = new KblNode();
        final KblNode endNode = new KblNode();
        source.setStartNode(startNode);
        source.setEndNode(endNode);

        final VecGeometryNode2D vecStartNode = new VecGeometryNode2D();
        final VecGeometryNode2D vecEndNode = new VecGeometryNode2D();
        orchestrator.addMockMapping(startNode, vecStartNode);
        orchestrator.addMockMapping(endNode, vecEndNode);

        // When
        final VecGeometrySegment2D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestId", VecGeometrySegment2D::getIdentification)
                .returns(vecEndNode, VecGeometrySegment2D::getEndNode)
                .returns(vecStartNode, VecGeometrySegment2D::getStartNode)
                .satisfies(v -> assertThat(v.getStartVector().getX()).isEqualTo(1.0))
                .satisfies(v -> assertThat(v.getStartVector().getY()).isEqualTo(2.0))
                .satisfies(v -> assertThat(v.getEndVector().getX()).isEqualTo(1.0))
                .satisfies(v -> assertThat(v.getEndVector().getY()).isEqualTo(2.0));
    }

    @Test
    void should_fillMissingCoordinatesWithZero() {
        // Given
        final GeometrySegment2DTransformer transformer = new GeometrySegment2DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblSegment source = new KblSegment();

        source.getStartVectors().add(1.0);
        source.getEndVectors().add(1.0);

        final KblNode startNode = new KblNode();
        final KblNode endNode = new KblNode();
        source.setStartNode(startNode);
        source.setEndNode(endNode);

        final VecGeometryNode2D vecStartNode = new VecGeometryNode2D();
        final VecGeometryNode2D vecEndNode = new VecGeometryNode2D();
        orchestrator.addMockMapping(startNode, vecStartNode);
        orchestrator.addMockMapping(endNode, vecEndNode);

        // When
        final VecGeometrySegment2D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(vecEndNode, VecGeometrySegment2D::getEndNode)
                .returns(vecStartNode, VecGeometrySegment2D::getStartNode)
                .satisfies(v -> assertThat(v.getStartVector().getX()).isEqualTo(1.0))
                .satisfies(v -> assertThat(v.getStartVector().getY()).isEqualTo(0.0))
                .satisfies(v -> assertThat(v.getEndVector().getX()).isEqualTo(1.0))
                .satisfies(v -> assertThat(v.getEndVector().getY()).isEqualTo(0.0));
    }

    @Test
    void should_requireAtLeastOneCoordinate() {
        // Given
        final GeometrySegment2DTransformer transformer = new GeometrySegment2DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblSegment source = new KblSegment();

        final KblNode startNode = new KblNode();
        final KblNode endNode = new KblNode();
        source.setStartNode(startNode);
        source.setEndNode(endNode);

        final VecGeometryNode2D vecStartNode = new VecGeometryNode2D();
        final VecGeometryNode2D vecEndNode = new VecGeometryNode2D();
        orchestrator.addMockMapping(startNode, vecStartNode);
        orchestrator.addMockMapping(endNode, vecEndNode);

        // When
        final VecGeometrySegment2D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(null, VecGeometrySegment2D::getStartVector)
                .returns(null, VecGeometrySegment2D::getEndVector);
    }
}
