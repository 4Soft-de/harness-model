package com.foursoft.harness.kbl2vec.transform.topology.geometry.d3;

import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCartesianPoint3D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartesianPoint3DTransformerTest {

    @Test
    void should_transformCartesianPoint3D() {
        // Given
        final CartesianPoint3DTransformer transformer = new CartesianPoint3DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCartesianPoint source = new KblCartesianPoint();
        source.getCoordinates().add(1.0);
        source.getCoordinates().add(2.0);
        source.getCoordinates().add(3.0);

        // When
        final VecCartesianPoint3D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(1.0, VecCartesianPoint3D::getX)
                .returns(2.0, VecCartesianPoint3D::getY)
                .returns(3.0, VecCartesianPoint3D::getZ);
    }
}
