package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponentBox;
import com.foursoft.harness.kbl.v25.KblComponentBoxConnection;
import com.foursoft.harness.kbl.v25.KblComponentBoxConnector;
import com.foursoft.harness.kbl.v25.KblComponentSlot;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EEComponentSpecificationTransformerTest {

    @Test
    void should_transformEEComponentSpecification() {
        // Given
        final EEComponentSpecificationTransformer transformer = new EEComponentSpecificationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblComponentBox source = new KblComponentBox();

        final VecPartVersion vecPartVersion = new VecPartVersion();
        final VecPartOrUsageRelatedSpecification vecPartOrUsageRelatedSpecification =
                new VecPartOrUsageRelatedSpecification();
        orchestrator.addMockMapping(source, vecPartVersion);
        orchestrator.addMockMapping(source, vecPartOrUsageRelatedSpecification);

        final KblComponentSlot slot = new KblComponentSlot();
        source.getComponentSlots().add(slot);

        final VecHousingComponent vecHousingComponentFromComponentSlot = new VecHousingComponent();
        orchestrator.addMockMapping(slot, vecHousingComponentFromComponentSlot);

        final KblComponentBoxConnection kblComponentBoxConnection = new KblComponentBoxConnection();
        source.getConnections().add(kblComponentBoxConnection);

        final VecInternalComponentConnection vecInternalComponentConnection = new VecInternalComponentConnection();
        orchestrator.addMockMapping(kblComponentBoxConnection, vecInternalComponentConnection);

        final KblComponentBoxConnector vecComponentBoxConnector = new KblComponentBoxConnector();
        source.getComponentBoxConnectors().add(vecComponentBoxConnector);

        final VecHousingComponent vecHousingComponentFromComponentBoxConnector = new VecHousingComponent();
        orchestrator.addMockMapping(vecComponentBoxConnector, vecHousingComponentFromComponentBoxConnector);

        // When
        final VecEEComponentSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getHousingComponents()).containsExactlyInAnyOrder(
                        vecHousingComponentFromComponentSlot,
                        vecHousingComponentFromComponentBoxConnector))
                .satisfies(v -> assertThat(v.getConnections()).containsExactly(vecInternalComponentConnection));
    }
}
