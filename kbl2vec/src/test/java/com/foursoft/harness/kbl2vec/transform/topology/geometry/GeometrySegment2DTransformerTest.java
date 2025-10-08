package com.foursoft.harness.kbl2vec.transform.topology.geometry;

import com.foursoft.harness.kbl.v25.KblNode;
import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCartesianVector2D;
import com.foursoft.harness.vec.v2x.VecGeometryNode2D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment2D;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GeometrySegment2DTransformerTest {

    @Test
    void should_transformGeometry2DTransformer() {
        // Given
        final GeometrySegment2DTransformer transformer = new GeometrySegment2DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblSegment source = new KblSegment();

        final List<Double> startVectors = source.getStartVectors();
        startVectors.add(1.0);
        final List<Double> endVectors = source.getEndVectors();
        endVectors.add(2.0);

        final VecCartesianVector2D vecStartVector = new VecCartesianVector2D();
        final VecCartesianVector2D vecEndVector = new VecCartesianVector2D();
        orchestrator.addMockMapping(startVectors, vecStartVector);
        orchestrator.addMockMapping(endVectors, vecEndVector);

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
                .returns(vecEndVector, VecGeometrySegment2D::getEndVector)
                .returns(vecStartVector, VecGeometrySegment2D::getStartVector);
    }
}
