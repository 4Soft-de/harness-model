package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.KblConnectorHousing;
import com.foursoft.harness.kbl.v25.KblConnectorOccurrence;
import com.foursoft.harness.kbl.v25.KblSlotOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecConnectorHousingRole;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v2x.VecSlotReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConnectorHousingRoleTransformerTest {

    @Test
    public void should_transformConnectorHousingRole() {
        // Given
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();
        final ConnectorHousingRoleTransformer transformer = new ConnectorHousingRoleTransformer();

        final KblConnectorOccurrence source = new KblConnectorOccurrence();
        source.setId("TestConnectorOccurrenceId");

        final KblConnectorHousing part = new KblConnectorHousing();
        source.setPart(part);

        final VecConnectorHousingSpecification connectorHousingSpecification = new VecConnectorHousingSpecification();

        final KblSlotOccurrence kblSlotOccurrence = new KblSlotOccurrence();
        final VecSlotReference vecSlotReference = new VecSlotReference();

        source.getSlots().add(kblSlotOccurrence);

        orchestrator.addMockMapping(kblSlotOccurrence, vecSlotReference);
        orchestrator.addMockMapping(part, connectorHousingSpecification);

        // When
        final VecConnectorHousingRole result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestConnectorOccurrenceId", VecConnectorHousingRole::getIdentification)
                .returns(connectorHousingSpecification, VecConnectorHousingRole::getConnectorHousingSpecification)
                .satisfies(
                        c -> assertThat(c.getSlotReferences()).contains(vecSlotReference)
                );
    }
}
