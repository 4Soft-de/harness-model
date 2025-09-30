package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponentCavity;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecPinComponent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PinComponentFromComponentCavityTest {

    @Test
    void should_transformPinComponentFromComponentCavity() {
        // Given
        final PinComponentFromComponentCavityTransformer transformer = new PinComponentFromComponentCavityTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblComponentCavity source = new KblComponentCavity();

        // When
        final VecPinComponent result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull();
    }
}
