package com.foursoft.harness.kbl2vec.transform.topology.geometry;

import com.foursoft.harness.kbl.v25.KblNode;
import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCartesianVector3D;
import com.foursoft.harness.vec.v2x.VecGeometryNode3D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment3D;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GeometrySegment3DTransformerTest {

    @Test
    void should_transformGeometry3DTransformer() {
        // Given
        final GeometrySegment3DTransformer transformer = new GeometrySegment3DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblSegment source = new KblSegment();

        final List<Double> startVectors = source.getStartVectors();
        startVectors.add(1.0);
        final List<Double> endVectors = source.getEndVectors();
        endVectors.add(2.0);

        final VecCartesianVector3D vecStartVector = new VecCartesianVector3D();
        final VecCartesianVector3D vecEndVector = new VecCartesianVector3D();
        orchestrator.addMockMapping(startVectors, vecStartVector);
        orchestrator.addMockMapping(endVectors, vecEndVector);

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
                .returns(vecEndVector, VecGeometrySegment3D::getEndVector)
                .returns(vecStartVector, VecGeometrySegment3D::getStartVector);
    }
}
