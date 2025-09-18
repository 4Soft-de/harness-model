package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.KblCavitySeal;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecGeneralTechnicalPartSpecification;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CavitySealDocumentVersionTransformerTest {

    @Test
    void should_transformCavitySealDocumentVersion() {
        // Given
        final CavitySealDocumentVersionTransformer transformer = new CavitySealDocumentVersionTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCavitySeal source = new KblCavitySeal();
        source.setCompanyName("TestCompany");
        source.setPartNumber("TestPartNumber");
        source.setVersion("TestVersion");

        final VecPartVersion vecPartVersion = new VecPartVersion();
        final VecGeneralTechnicalPartSpecification vecGeneralTechnicalPartSpecification = new VecGeneralTechnicalPartSpecification();

        orchestrator.addMockMapping(source, vecPartVersion);
        orchestrator.addMockMapping(source, vecGeneralTechnicalPartSpecification);

        // When
        final VecDocumentVersion result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestCompany", VecDocumentVersion::getCompanyName)
                .returns("TestPartNumber", VecDocumentVersion::getDocumentNumber)
                .returns("TestVersion", VecDocumentVersion::getDocumentVersion)
                .satisfies(v -> assertThat(v.getReferencedPart()).containsExactly(vecPartVersion));
    }
}
