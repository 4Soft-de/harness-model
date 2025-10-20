package com.foursoft.harness.kbl2vec.transform.geometry.geo_2d;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecBuildingBlockPositioning2D;
import com.foursoft.harness.vec.v2x.VecHarnessDrawingSpecification2D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HarnessDrawingSpecification2DTransformerTest {

    @Test
    void should_transformHarnessDrawingSpecification2D() {
        // Given
        final HarnessDrawingSpecification2DTransformer transformer = new HarnessDrawingSpecification2DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblHarness source = new KblHarness();

        final VecBuildingBlockPositioning2D vecBuildingBlockPositioning2D = new VecBuildingBlockPositioning2D();
        orchestrator.addMockMapping(source, vecBuildingBlockPositioning2D);

        // When
        final VecHarnessDrawingSpecification2D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getBuildingBlockPositionings()).contains(vecBuildingBlockPositioning2D));
    }
}
