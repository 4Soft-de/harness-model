package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.KblCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblSlot;
import com.foursoft.harness.kbl.v25.KblSlotOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCavityReference;
import com.foursoft.harness.vec.v2x.VecSlot;
import com.foursoft.harness.vec.v2x.VecSlotReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SlotReferenceTransformerTest {

    @Test
    void should_transformSlotReference() {
        // Given
        final SlotReferenceTransformer transformer = new SlotReferenceTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblSlotOccurrence source = new KblSlotOccurrence();
        final KblSlot part = new KblSlot();
        part.setId("TestPartId");
        source.setPart(part);

        final KblCavityOccurrence kblCavityOccurrence = new KblCavityOccurrence();
        source.getCavities().add(kblCavityOccurrence);

        final VecCavityReference vecCavityReference = new VecCavityReference();
        final VecSlot vecSlot = new VecSlot();

        orchestrator.addMockMapping(kblCavityOccurrence, vecCavityReference);
        orchestrator.addMockMapping(part, vecSlot);

        // When
        final VecSlotReference result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestPartId", VecSlotReference::getIdentification)
                .returns(vecSlot, VecSlotReference::getReferencedSlot)
                .satisfies(
                        v -> assertThat(v.getCavityReferences()).contains(vecCavityReference)
                );
    }
}
