package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.KblCavitySeal;
import com.foursoft.harness.kbl.v25.KblCavitySealOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CavitySealOccurrenceTransformerTest {

    @Test
    void should_transformCavitySealOccurrence() {
        // Given
        final CavitySealOccurrenceTransformer transformer = new CavitySealOccurrenceTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCavitySealOccurrence source = new KblCavitySealOccurrence();
        source.setId("TestId");

        final KblCavitySeal part = new KblCavitySeal();
        source.setPart(part);

        final VecPartVersion vecPartVersion = new VecPartVersion();

        orchestrator.addMockMapping(part, vecPartVersion);

        // When
        final VecPartOccurrence result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result)
                .isNotNull()
                .returns("TestId", VecPartOccurrence::getIdentification)
                .returns(vecPartVersion, VecPartOccurrence::getPart);
    }
}
