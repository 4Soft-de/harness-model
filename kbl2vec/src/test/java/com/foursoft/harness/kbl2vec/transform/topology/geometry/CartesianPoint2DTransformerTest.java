package com.foursoft.harness.kbl2vec.transform.topology.geometry;

import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCartesianPoint2D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartesianPoint2DTransformerTest {

    @Test
    void should_transformCartesianPoint2D() {
        // Given
        final CartesianPoint2DTransformer transformer = new CartesianPoint2DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCartesianPoint source = new KblCartesianPoint();
        source.getCoordinates().add(1.0);
        source.getCoordinates().add(2.0);

        // When
        final VecCartesianPoint2D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(1.0, VecCartesianPoint2D::getX)
                .returns(2.0, VecCartesianPoint2D::getY);
    }
}
