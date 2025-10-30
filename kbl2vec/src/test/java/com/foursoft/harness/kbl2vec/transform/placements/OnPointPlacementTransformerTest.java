package com.foursoft.harness.kbl2vec.transform.placements;

import com.foursoft.harness.kbl.v25.FixedComponent;
import com.foursoft.harness.kbl.v25.KblFixingAssignment;
import com.foursoft.harness.kbl.v25.KblFixingOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecOnPointPlacement;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v2x.VecSegmentLocation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OnPointPlacementTransformerTest {

    @Test
    void should_transformOnPointPlacement() {
        // Given
        final OnPointPlacementTransformer transformer = new OnPointPlacementTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblFixingAssignment source = new KblFixingAssignment();
        source.setId("TestId");

        final VecSegmentLocation location = new VecSegmentLocation();
        orchestrator.addMockMapping(source, location);

        final FixedComponent fixing = new KblFixingOccurrence();
        source.setFixing(fixing);

        final VecPlaceableElementRole role = new VecPlaceableElementRole();
        orchestrator.addMockMapping(fixing, role);

        // When
        final VecOnPointPlacement result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestId", VecOnPointPlacement::getIdentification)
                .satisfies(v -> assertThat(v.getPlacedElement()).containsExactly(role))
                .satisfies(v -> assertThat(v.getLocations()).containsExactly(location));
    }
}
