package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.KblCavitySeal;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCavitySealSpecification;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CavitySealSpecificationTransformerTest {

    @Test
    void should_transformCavitySealSpecification() {
        // Given
        final CavitySealSpecificationTransformer transformer = new CavitySealSpecificationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCavitySeal source = new KblCavitySeal();
        source.setSealType("TestSealType");
        source.setPartNumber("TestPartNumber");
        final String expectedIdentification = "CSS-TestPartNumber";

        final VecPartVersion vecPartVersion = new VecPartVersion();
        orchestrator.addMockMapping(source, vecPartVersion);

        // When
        final VecCavitySealSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result)
                .isNotNull()
                .returns("TestSealType", VecCavitySealSpecification::getSpecialPartType)
                .returns(expectedIdentification, VecCavitySealSpecification::getIdentification)
                .satisfies(v -> assertThat(v.getDescribedPart()).containsExactly(vecPartVersion));
    }
}
