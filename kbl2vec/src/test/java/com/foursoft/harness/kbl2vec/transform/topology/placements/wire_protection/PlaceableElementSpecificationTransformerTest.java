package com.foursoft.harness.kbl2vec.transform.topology.placements.wire_protection;

import com.foursoft.harness.kbl.v25.KblWireProtection;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecPlaceableElementSpecification;
import com.foursoft.harness.vec.v2x.VecPlacementType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceableElementSpecificationTransformerTest {

    @Test
    void shouldTransformPlaceableElementSpecification() {
        // Given
        final PlaceableElementSpecificationTransformer transformer = new PlaceableElementSpecificationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblWireProtection source = new KblWireProtection();

        final VecPartVersion vecPartVersion = new VecPartVersion();
        orchestrator.addMockMapping(source, vecPartVersion);

        // When
        final VecPlaceableElementSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getDescribedPart()).containsExactly(vecPartVersion))
                .satisfies(
                        v -> assertThat(v.getValidPlacementTypes()).containsExactlyInAnyOrder(VecPlacementType.ON_WAY,
                                                                                              VecPlacementType.ON_POINT));
    }
}
