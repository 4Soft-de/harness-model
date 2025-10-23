package com.foursoft.harness.kbl2vec.transform.geometry.geo_3d;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecAliasIdentification;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsageViewItem3D;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecTransformation3D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OccurrenceOrUsageViewItem3DTransformerTest {
    @Test
    void should_transformOccurrenceOrUsageViewItem3D() {
        // Given
        final OccurrenceOrUsageViewItem3DTransformer transformer = new OccurrenceOrUsageViewItem3DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblAssemblyPartOccurrence source = new KblAssemblyPartOccurrence();
        final KblTransformation transformation = new KblTransformation();
        source.setPlacement(transformation);

        final VecTransformation3D vecTransformation3D = new VecTransformation3D();
        orchestrator.addMockMapping(transformation, vecTransformation3D);

        final KblAliasIdentification aliasIdentification = new KblAliasIdentification();
        source.getAliasIds().add(aliasIdentification);

        final VecAliasIdentification vecAliasIdentification = new VecAliasIdentification();
        orchestrator.addMockMapping(aliasIdentification, vecAliasIdentification);

        final VecPartOccurrence vecPartOccurrence = new VecPartOccurrence();
        orchestrator.addMockMapping(source, vecPartOccurrence);

        // When
        final VecOccurrenceOrUsageViewItem3D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(vecTransformation3D, VecOccurrenceOrUsageViewItem3D::getOrientation)
                .satisfies(v -> assertThat(v.getAliasIds()).containsExactly(vecAliasIdentification))
                .satisfies(v -> assertThat(v.getOccurrenceOrUsage()).containsExactly(vecPartOccurrence));
    }
}
