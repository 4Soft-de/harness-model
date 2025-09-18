package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.KblCavity;
import com.foursoft.harness.kbl.v25.KblSlot;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCavity;
import com.foursoft.harness.vec.v2x.VecSlot;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SlotTransformerTest {

    @Test
    void should_transformSlotReference() {
        // Given
        final SlotTransformer transformer = new SlotTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblSlot source = new KblSlot();
        source.setId("TestId");

        final KblCavity kblCavity = new KblCavity();
        source.getCavities().add(kblCavity);

        final VecCavity vecCavity = new VecCavity();
        orchestrator.addMockMapping(kblCavity, vecCavity);

        // When
        final VecSlot result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestId", VecSlot::getSlotNumber)
                .satisfies(
                        v -> assertThat(v.getCavities()).contains(vecCavity)
                );
    }
}
