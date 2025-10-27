package com.foursoft.harness.kbl2vec.transform.topology.placements.wire_protection;

import com.foursoft.harness.kbl.v25.KblWireProtectionOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecOnWayPlacement;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OnWayPlacementTransformerTest {

    @Test
    void should_transformOnWayPlacement() {
        // Given
        final OnWayPlacementTransformer transformer = new OnWayPlacementTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblWireProtectionOccurrence source = new KblWireProtectionOccurrence();
        source.setId("TestId");

        final VecPlaceableElementRole placeableElementRole = new VecPlaceableElementRole();
        orchestrator.addMockMapping(source, placeableElementRole);

        // When
        final VecOnWayPlacement result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestId", VecOnWayPlacement::getIdentification)
                .satisfies(v -> assertThat(v.getPlacedElement()).containsExactly(placeableElementRole));
    }
}
