package com.foursoft.harness.kbl2vec.transform.geometry.geo_3d;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecBuildingBlockPositioning3D;
import com.foursoft.harness.vec.v2x.VecHarnessGeometrySpecification3D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HarnessGeometrySpecification3DTransformerTest {

    @Test
    void should_transformHarnessGeometrySpecification3D() {
        // Given
        final HarnessGeometrySpecification3DTransformer transformer = new HarnessGeometrySpecification3DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblHarness source = new KblHarness();

        final VecBuildingBlockPositioning3D vecBuildingBlockPositioning3D = new VecBuildingBlockPositioning3D();
        orchestrator.addMockMapping(source, vecBuildingBlockPositioning3D);

        // When
        final VecHarnessGeometrySpecification3D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("Dmu", VecHarnessGeometrySpecification3D::getType)
                .satisfies(v -> assertThat(v.getBuildingBlockPositionings()).contains(vecBuildingBlockPositioning3D));
    }
}
