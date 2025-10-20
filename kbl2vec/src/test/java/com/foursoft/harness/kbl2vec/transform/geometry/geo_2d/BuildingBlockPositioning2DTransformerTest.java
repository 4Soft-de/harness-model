package com.foursoft.harness.kbl2vec.transform.geometry.geo_2d;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecBuildingBlockPositioning2D;
import com.foursoft.harness.vec.v2x.VecBuildingBlockSpecification2D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BuildingBlockPositioning2DTransformerTest {

    @Test
    void should_transformBuildingBlockPositioning2D() {
        // Given
        final BuildingBlockPositioning2DTransformer transformer = new BuildingBlockPositioning2DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblHarness source = new KblHarness();

        final VecBuildingBlockSpecification2D blockSpecification2D = new VecBuildingBlockSpecification2D();
        orchestrator.addMockMapping(source, blockSpecification2D);

        // When
        final VecBuildingBlockPositioning2D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(blockSpecification2D, VecBuildingBlockPositioning2D::getReferenced2DBuildingBlock);
    }
}
