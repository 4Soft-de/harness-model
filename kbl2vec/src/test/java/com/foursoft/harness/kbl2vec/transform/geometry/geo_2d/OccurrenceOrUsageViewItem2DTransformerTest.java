package com.foursoft.harness.kbl2vec.transform.geometry.geo_2d;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecAliasIdentification;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsageViewItem2D;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecTransformation2D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OccurrenceOrUsageViewItem2DTransformerTest {
    @Test
    void should_transformOccurrenceOrUsageViewItem2D() {
        // Given
        final OccurrenceOrUsageViewItem2DTransformer transformer = new OccurrenceOrUsageViewItem2DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblAssemblyPartOccurrence source = new KblAssemblyPartOccurrence();
        final KblTransformation transformation = new KblTransformation();
        source.setPlacement(transformation);

        final VecTransformation2D vecTransformation2D = new VecTransformation2D();
        orchestrator.addMockMapping(transformation, vecTransformation2D);

        final KblAliasIdentification aliasIdentification = new KblAliasIdentification();
        source.getAliasIds().add(aliasIdentification);

        final VecAliasIdentification vecAliasIdentification = new VecAliasIdentification();
        orchestrator.addMockMapping(aliasIdentification, vecAliasIdentification);

        final VecPartOccurrence vecPartOccurrence = new VecPartOccurrence();
        orchestrator.addMockMapping(source, vecPartOccurrence);

        // When
        final VecOccurrenceOrUsageViewItem2D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(vecTransformation2D, VecOccurrenceOrUsageViewItem2D::getOrientation)
                .satisfies(v -> assertThat(v.getAliasIds()).containsExactly(vecAliasIdentification))
                .satisfies(v -> assertThat(v.getOccurrenceOrUsage()).containsExactly(vecPartOccurrence));
    }
}
