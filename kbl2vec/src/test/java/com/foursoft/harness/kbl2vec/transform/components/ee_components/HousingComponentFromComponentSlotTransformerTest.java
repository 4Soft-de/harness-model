package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponentCavity;
import com.foursoft.harness.kbl.v25.KblComponentSlot;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecHousingComponent;
import com.foursoft.harness.vec.v2x.VecPinComponent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HousingComponentFromComponentSlotTransformerTest {

    @Test
    void should_transformHousingComponent() {
        // Given
        final HousingComponentFromComponentSlotTransformer
                transformer = new HousingComponentFromComponentSlotTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblComponentSlot source = new KblComponentSlot();
        source.setId("TestId");

        final KblComponentCavity cavity = new KblComponentCavity();
        final VecPinComponent vecPinComponent = new VecPinComponent();
        orchestrator.addMockMapping(cavity, vecPinComponent);

        // When
        final VecHousingComponent result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestId", VecHousingComponent::getIdentification);
    }
}
