package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblCavity;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecPinComponent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PinCompFromCavityTransformerTest {

    @Test
    void should_transformPinComponentFromCavity() {
        // Given
        final PinCompFromCavityTransformer transformer = new PinCompFromCavityTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCavity source = new KblCavity();

        // When
        final VecPinComponent result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull();
    }
}
