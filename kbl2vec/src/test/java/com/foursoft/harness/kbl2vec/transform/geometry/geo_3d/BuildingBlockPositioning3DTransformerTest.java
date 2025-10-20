package com.foursoft.harness.kbl2vec.transform.geometry.geo_3d;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecBuildingBlockPositioning3D;
import com.foursoft.harness.vec.v2x.VecBuildingBlockSpecification3D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BuildingBlockPositioning3DTransformerTest {

    @Test
    void should_transformBuildingBlockPositioning3D() {
        // Given
        final BuildingBlockPositioning3DTransformer transformer = new BuildingBlockPositioning3DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblHarness source = new KblHarness();

        final VecBuildingBlockSpecification3D blockSpecification3D = new VecBuildingBlockSpecification3D();
        orchestrator.addMockMapping(source, blockSpecification3D);

        // When
        final VecBuildingBlockPositioning3D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(blockSpecification3D, VecBuildingBlockPositioning3D::getReferenced3DBuildingBlock);
    }
}
