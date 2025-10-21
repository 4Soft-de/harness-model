package com.foursoft.harness.kbl2vec.transform.components.wires.multi_cores;

import com.foursoft.harness.kbl.v25.KblCore;
import com.foursoft.harness.kbl.v25.KblWireColour;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecInsulationSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InsulationSpecificationTransformerTest {

    @Test
    void should_transformInsulationSpecification() {
        // Given
        final InsulationSpecificationTransformer transformer = new InsulationSpecificationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCore source = new KblCore();

        final KblWireColour kblWireColour = new KblWireColour();
        kblWireColour.setColourValue("TestColourValue");

        source.getCoreColours().add(kblWireColour);

        // When
        final VecInsulationSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getBaseColors()).isNotEmpty());
    }
}
