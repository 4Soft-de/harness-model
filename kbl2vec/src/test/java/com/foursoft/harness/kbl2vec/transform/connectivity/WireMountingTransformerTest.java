package com.foursoft.harness.kbl2vec.transform.connectivity;

import com.foursoft.harness.kbl.v25.KblCavitySealOccurrence;
import com.foursoft.harness.kbl.v25.KblContactPoint;
import com.foursoft.harness.kbl.v25.KblExtremity;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCavitySealRole;
import com.foursoft.harness.vec.v2x.VecWireEnd;
import com.foursoft.harness.vec.v2x.VecWireMounting;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WireMountingTransformerTest {

    @Test
    void should_transformWireMounting() {
        // Given
        final WireMountingTransformer transformer = new WireMountingTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblContactPoint source = new KblContactPoint();

        final KblCavitySealOccurrence cavitySeal = new KblCavitySealOccurrence();
        source.getAssociatedParts().add(cavitySeal);

        final VecCavitySealRole vecCavitySealRole = new VecCavitySealRole();
        orchestrator.addMockMapping(cavitySeal, vecCavitySealRole);

        final KblExtremity extremity = new KblExtremity();
        source.getRefExtremity().add(extremity);

        final VecWireEnd vecWireEnd = new VecWireEnd();
        orchestrator.addMockMapping(extremity, vecWireEnd);

        // When
        final VecWireMounting result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(vecCavitySealRole, VecWireMounting::getMountedCavitySeal)
                .satisfies(v -> assertThat(v.getReferencedWireEnd()).containsExactly(vecWireEnd));
    }
}
