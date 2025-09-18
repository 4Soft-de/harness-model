package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.KblConnectorHousing;
import com.foursoft.harness.kbl.v25.KblSlot;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecSlot;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConnectorHousingSpecificationTransformerTest {

    @Test
    public void should_transformConnectorHousingSpecification() {
        final ConnectorHousingSpecificationTransformer transformer = new ConnectorHousingSpecificationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblConnectorHousing source = new KblConnectorHousing();
        source.setHousingType("TestHousingType");
        source.setHousingCode("TestHousingCode");

        final KblSlot kblSlot = new KblSlot();
        final VecSlot vecSlot = new VecSlot();

        source.getSlots().add(kblSlot);

        final VecPartVersion vecPartVersion = new VecPartVersion();

        orchestrator.addMockMapping(kblSlot, vecSlot);
        orchestrator.addMockMapping(source, vecPartVersion);

        final VecConnectorHousingSpecification result = orchestrator.transform(transformer, source);

        assertThat(result).isNotNull()
                .returns("TestHousingType", VecConnectorHousingSpecification::getSpecialPartType)
                .satisfies(
                        v -> assertThat(v.getDescribedPart()).contains(vecPartVersion)
                )
                .satisfies(
                        v -> assertThat(v.getSlots()).contains(vecSlot)
                )
                .satisfies(
                        v -> {
                            assertThat(v.getCoding().getCoding()).isEqualTo("TestHousingCode");
                        }
                );
    }
}
