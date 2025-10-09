package com.foursoft.harness.kbl2vec.transform.topology.geometry.d2;

import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl.v25.KblNode;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCartesianPoint2D;
import com.foursoft.harness.vec.v2x.VecGeometryNode2D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GeometryNode2DTransformerTest {

    @Test
    void should_transformGeometryNode2D() {
        // Given
        final GeometryNode2DTransformer transformer = new GeometryNode2DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblNode source = new KblNode();
        final KblCartesianPoint cartesianPoint = new KblCartesianPoint();
        source.setCartesianPoint(cartesianPoint);

        final VecCartesianPoint2D vecCartesianPoint2D = new VecCartesianPoint2D();
        orchestrator.addMockMapping(cartesianPoint, vecCartesianPoint2D);

        // When
        final VecGeometryNode2D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(vecCartesianPoint2D, VecGeometryNode2D::getCartesianPoint);
    }
}
