package com.foursoft.harness.kbl2vec.transform.connectivity;

import com.foursoft.harness.kbl.v25.KblCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblCavityPlugOccurrence;
import com.foursoft.harness.kbl.v25.KblContactPoint;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCavityMounting;
import com.foursoft.harness.vec.v2x.VecCavityPlugRole;
import com.foursoft.harness.vec.v2x.VecCavityReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CavityMountingTransformerTest {

    @Test
    void should_transformCavityMounting() {
        // Given
        final CavityMountingTransformer transformer = new CavityMountingTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblContactPoint source = new KblContactPoint();

        final KblCavityPlugOccurrence cavityPlugOccurrence = new KblCavityPlugOccurrence();
        final KblCavityOccurrence cavityOccurrence = new KblCavityOccurrence();
        cavityOccurrence.setAssociatedPlug(cavityPlugOccurrence);
        source.getContactedCavity().add(cavityOccurrence);

        final VecCavityPlugRole vecCavityPlugRole = new VecCavityPlugRole();
        orchestrator.addMockMapping(cavityPlugOccurrence, vecCavityPlugRole);

        final VecCavityReference vecCavityReference = new VecCavityReference();
        orchestrator.addMockMapping(cavityPlugOccurrence, vecCavityReference);

        // When
        final VecCavityMounting result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getReplacedPlug()).containsExactly(vecCavityPlugRole))
                .satisfies(v -> assertThat(v.getEquippedCavityRef()).containsExactly(vecCavityReference));
    }
}
