package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.KblCavity;
import com.foursoft.harness.kbl.v25.KblCavityOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCavity;
import com.foursoft.harness.vec.v2x.VecCavityReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CavityReferenceTransformerTest {

    @Test
    void should_transformCavityReference() {
        // Given
        final CavityReferenceTransformer transformer = new CavityReferenceTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCavity part = new KblCavity();
        part.setCavityNumber("TestCavityNumber");

        final KblCavityOccurrence source = new KblCavityOccurrence();
        source.setPart(part);

        final VecCavity vecCavity = new VecCavity();
        orchestrator.addMockMapping(part, vecCavity);

        // When
        final VecCavityReference result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestCavityNumber", VecCavityReference::getIdentification)
                .returns(vecCavity, VecCavityReference::getReferencedCavity);
    }
}
