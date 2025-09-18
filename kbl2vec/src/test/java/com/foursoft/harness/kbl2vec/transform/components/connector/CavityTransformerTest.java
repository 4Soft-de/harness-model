package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.KblCavity;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCavity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CavityTransformerTest {

    @Test
    void should_transformCavity() {
        // Given
        final CavityTransformer transformer = new CavityTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCavity source = new KblCavity();
        source.setCavityNumber("TestCavityNumber");

        // When
        final VecCavity result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull().returns("TestCavityNumber", VecCavity::getCavityNumber);
    }
}
