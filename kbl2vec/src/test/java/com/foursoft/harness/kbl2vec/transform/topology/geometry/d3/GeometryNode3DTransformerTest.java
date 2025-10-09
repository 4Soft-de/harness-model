package com.foursoft.harness.kbl2vec.transform.topology.geometry.d3;

import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl.v25.KblNode;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCartesianPoint3D;
import com.foursoft.harness.vec.v2x.VecGeometryNode3D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GeometryNode3DTransformerTest {

    @Test
    void should_transformGeometryNode3D() {
        // Given
        final GeometryNode3DTransformer transformer = new GeometryNode3DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblNode source = new KblNode();
        final KblCartesianPoint cartesianPoint = new KblCartesianPoint();
        source.setCartesianPoint(cartesianPoint);

        final VecCartesianPoint3D vecCartesianPoint3D = new VecCartesianPoint3D();
        orchestrator.addMockMapping(cartesianPoint, vecCartesianPoint3D);

        // When
        final VecGeometryNode3D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(vecCartesianPoint3D, VecGeometryNode3D::getCartesianPoint);
    }
}
