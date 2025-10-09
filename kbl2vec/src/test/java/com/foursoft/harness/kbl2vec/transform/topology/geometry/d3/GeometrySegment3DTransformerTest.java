package com.foursoft.harness.kbl2vec.transform.topology.geometry.d3;

import com.foursoft.harness.kbl.v25.KblNode;
import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecGeometryNode3D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment3D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GeometrySegment3DTransformerTest {

    @Test
    void should_transformGeometry3DTransformer() {
        // Given
        final GeometrySegment3DTransformer transformer = new GeometrySegment3DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblSegment source = new KblSegment();

        source.getStartVectors().add(1.0);
        source.getStartVectors().add(2.0);
        source.getStartVectors().add(3.0);
        source.getEndVectors().add(1.0);
        source.getEndVectors().add(2.0);
        source.getEndVectors().add(3.0);

        final KblNode startNode = new KblNode();
        final KblNode endNode = new KblNode();
        source.setStartNode(startNode);
        source.setEndNode(endNode);

        final VecGeometryNode3D vecStartNode = new VecGeometryNode3D();
        final VecGeometryNode3D vecEndNode = new VecGeometryNode3D();
        orchestrator.addMockMapping(startNode, vecStartNode);
        orchestrator.addMockMapping(endNode, vecEndNode);

        // When
        final VecGeometrySegment3D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(vecEndNode, VecGeometrySegment3D::getEndNode)
                .returns(vecStartNode, VecGeometrySegment3D::getStartNode)
                .satisfies(v -> assertThat(v.getStartVector().getX()).isEqualTo(1.0))
                .satisfies(v -> assertThat(v.getStartVector().getY()).isEqualTo(2.0))
                .satisfies(v -> assertThat(v.getStartVector().getZ()).isEqualTo(3.0))
                .satisfies(v -> assertThat(v.getEndVector().getX()).isEqualTo(1.0))
                .satisfies(v -> assertThat(v.getEndVector().getY()).isEqualTo(2.0))
                .satisfies(v -> assertThat(v.getEndVector().getZ()).isEqualTo(3.0));
    }
}
