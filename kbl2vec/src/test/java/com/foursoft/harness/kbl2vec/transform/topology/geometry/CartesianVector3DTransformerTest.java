package com.foursoft.harness.kbl2vec.transform.topology.geometry;

import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCartesianVector3D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CartesianVector3DTransformerTest {

    @Test
    void should_transformCartesianVector3D() {
        // Given
        final CartesianVector3DTransformer transformer = new CartesianVector3DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final List<Double> source = new ArrayList<>();
        source.add(1.0);
        source.add(2.0);
        source.add(3.0);

        // When
        final VecCartesianVector3D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(1.0, VecCartesianVector3D::getX)
                .returns(2.0, VecCartesianVector3D::getY)
                .returns(3.0, VecCartesianVector3D::getZ);
    }
}
