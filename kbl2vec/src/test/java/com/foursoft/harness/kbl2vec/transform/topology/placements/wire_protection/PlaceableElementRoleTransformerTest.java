package com.foursoft.harness.kbl2vec.transform.topology.placements.wire_protection;

import com.foursoft.harness.kbl.v25.KblWireProtection;
import com.foursoft.harness.kbl.v25.KblWireProtectionOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v2x.VecPlaceableElementSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceableElementRoleTransformerTest {

    @Test
    void should_transformPlaceableElementRole() {
        // Given
        final PlaceableElementRoleTransformer transformer = new PlaceableElementRoleTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblWireProtectionOccurrence source = new KblWireProtectionOccurrence();
        source.setId("TestId");

        final KblWireProtection part = new KblWireProtection();
        source.setPart(part);

        final VecPlaceableElementSpecification specification = new VecPlaceableElementSpecification();
        orchestrator.addMockMapping(part, specification);

        // When
        final VecPlaceableElementRole result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestId", VecPlaceableElementRole::getIdentification)
                .returns(specification, VecPlaceableElementRole::getPlaceableElementSpecification);
    }
}
