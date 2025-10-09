package com.foursoft.harness.kbl2vec.transform.topology.geometry.d2;

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
                .returns(vecEndNode, VecGeometrySegment2D::getEndNode)
                .returns(vecStartNode, VecGeometrySegment2D::getStartNode)
                .satisfies(v -> assertThat(v.getStartVector().getX()).isEqualTo(1.0))
                .satisfies(v -> assertThat(v.getStartVector().getY()).isEqualTo(2.0))
                .satisfies(v -> assertThat(v.getEndVector().getX()).isEqualTo(1.0))
                .satisfies(v -> assertThat(v.getEndVector().getY()).isEqualTo(2.0));
    }
}
