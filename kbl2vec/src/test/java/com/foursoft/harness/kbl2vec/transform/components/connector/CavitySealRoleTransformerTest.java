package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.KblCavitySeal;
import com.foursoft.harness.kbl.v25.KblCavitySealOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCavitySealRole;
import com.foursoft.harness.vec.v2x.VecCavitySealSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CavitySealRoleTransformerTest {

    @Test
    void should_transformCavitySealRole() {
        // Given
        final CavitySealRoleTransformer transformer = new CavitySealRoleTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCavitySealOccurrence source = new KblCavitySealOccurrence();
        source.setId("TestId");

        final KblCavitySeal part = new KblCavitySeal();
        source.setPart(part);

        final VecCavitySealSpecification vecCavitySealSpecification = new VecCavitySealSpecification();

        orchestrator.addMockMapping(part, vecCavitySealSpecification);

        // When
        final VecCavitySealRole result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result)
                .isNotNull()
                .returns("TestId", VecCavitySealRole::getIdentification)
                .returns(vecCavitySealSpecification, VecCavitySealRole::getCavitySealSpecification);
    }
}
