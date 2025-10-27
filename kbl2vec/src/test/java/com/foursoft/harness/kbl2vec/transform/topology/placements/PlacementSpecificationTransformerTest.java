package com.foursoft.harness.kbl2vec.transform.topology.placements;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblWireProtectionOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecOnPointPlacement;
import com.foursoft.harness.vec.v2x.VecOnWayPlacement;
import com.foursoft.harness.vec.v2x.VecPlacementSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlacementSpecificationTransformerTest {

    @Test
    void should_transformPlacementSpecification() {
        // Given
        final PlacementSpecificationTransformer transformer = new PlacementSpecificationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblHarness source = new KblHarness();

        final KblWireProtectionOccurrence wireProtectionOccurrence = new KblWireProtectionOccurrence();
        source.getWireProtectionOccurrences().add(wireProtectionOccurrence);

        final VecOnPointPlacement vecOnPointPlacement = new VecOnPointPlacement();
        orchestrator.addMockMapping(wireProtectionOccurrence, vecOnPointPlacement);

        final VecOnWayPlacement vecOnWayPlacement = new VecOnWayPlacement();
        orchestrator.addMockMapping(wireProtectionOccurrence, vecOnWayPlacement);

        // When
        final VecPlacementSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getPlacements()).contains(vecOnPointPlacement, vecOnWayPlacement));
    }
}
