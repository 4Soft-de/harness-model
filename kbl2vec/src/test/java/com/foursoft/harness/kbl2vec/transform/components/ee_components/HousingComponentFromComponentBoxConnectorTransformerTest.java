package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponentBoxConnector;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecHousingComponent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HousingComponentFromComponentBoxConnectorTransformerTest {

    @Test
    void should_transformHousingComponentFromComponentBoxConnector() {
        // Given
        final HousingComponentFromComponentBoxConnectorTransformer transformer =
                new HousingComponentFromComponentBoxConnectorTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblComponentBoxConnector source = new KblComponentBoxConnector();
        source.setId("TestId");

        // When
        final VecHousingComponent result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestId", VecHousingComponent::getIdentification);
    }
}
