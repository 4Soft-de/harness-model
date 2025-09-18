package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.KblConnectorHousing;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecGeneralTechnicalPartSpecification;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConnectorDocumentVersionTransformerTest {

    @Test
    public void should_transformConnectorDocumentVersion() {
        // Given
        final ConnectorDocumentVersionTransformer transformer = new ConnectorDocumentVersionTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblConnectorHousing source = new KblConnectorHousing();
        source.setHousingCode("TestHousingCode");
        source.setCompanyName("TestCompanyName");
        source.setPartNumber("TestPartNumber");
        source.setVersion("TestVersion");

        final VecConnectorHousingSpecification vecConnectorHousingSpecification =
                new VecConnectorHousingSpecification();
        final VecGeneralTechnicalPartSpecification vecGeneralTechnicalPartSpecification =
                new VecGeneralTechnicalPartSpecification();
        final VecPartVersion vecPartVersion = new VecPartVersion();

        orchestrator.addMockMapping(source, vecConnectorHousingSpecification);
        orchestrator.addMockMapping(source, vecGeneralTechnicalPartSpecification);
        orchestrator.addMockMapping(source, vecPartVersion);

        // When
        final VecDocumentVersion result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("PartMaster", VecDocumentVersion::getDocumentType)
                .returns("TestCompanyName", VecDocumentVersion::getCompanyName)
                .returns("TestPartNumber", VecDocumentVersion::getDocumentNumber)
                .returns("TestVersion", VecDocumentVersion::getDocumentVersion)
                .satisfies(vecDocumentVersion -> assertThat(vecDocumentVersion.getSpecifications())
                        .contains(vecConnectorHousingSpecification)
                )
                .satisfies(vecDocumentVersion -> assertThat(vecDocumentVersion.getSpecifications())
                        .contains(vecGeneralTechnicalPartSpecification)
                );
    }
}
