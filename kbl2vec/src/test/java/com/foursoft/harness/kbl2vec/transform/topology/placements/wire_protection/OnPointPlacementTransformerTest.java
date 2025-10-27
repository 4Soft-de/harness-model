package com.foursoft.harness.kbl2vec.transform.topology.placements.wire_protection;

import com.foursoft.harness.kbl.v25.KblWireProtectionOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecOnPointPlacement;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OnPointPlacementTransformerTest {

    @Test
    void should_transformOnPointPlacement() {
        // Given
        final OnPointPlacementTransformer transformer = new OnPointPlacementTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblWireProtectionOccurrence source = new KblWireProtectionOccurrence();
        source.setId("TestId");

        final VecPlaceableElementRole placeableElementRole = new VecPlaceableElementRole();
        orchestrator.addMockMapping(source, placeableElementRole);

        // When
        final VecOnPointPlacement result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestId", VecOnPointPlacement::getIdentification)
                .satisfies(v -> assertThat(v.getPlacedElement()).containsExactly(placeableElementRole));
    }
}
