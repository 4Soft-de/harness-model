package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblCavity;
import com.foursoft.harness.kbl.v25.KblComponentBoxConnection;
import com.foursoft.harness.kbl.v25.KblComponentCavity;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecInternalComponentConnection;
import com.foursoft.harness.vec.v2x.VecPinComponent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InternalComponentConnectionTransformerTest {

    @Test
    void should_transformInternalComponentConnection() {
        // Given
        final InternalComponentConnectionTransformer transformer = new InternalComponentConnectionTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblComponentBoxConnection source = new KblComponentBoxConnection();

        final KblComponentCavity kblComponentCavity = new KblComponentCavity();
        final KblCavity kblCavity = new KblCavity();
        source.getComponentCavities().add(kblComponentCavity);
        source.getCavities().add(kblCavity);

        final VecPinComponent vecPinComponentFromComponentCavity = new VecPinComponent();
        final VecPinComponent vecPinComponentFromCavity = new VecPinComponent();
        orchestrator.addMockMapping(kblComponentCavity, vecPinComponentFromComponentCavity);
        orchestrator.addMockMapping(kblCavity, vecPinComponentFromCavity);

        // When
        final VecInternalComponentConnection result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getPins()).containsExactlyInAnyOrder(vecPinComponentFromCavity,
                                                                                  vecPinComponentFromComponentCavity));
    }
}
