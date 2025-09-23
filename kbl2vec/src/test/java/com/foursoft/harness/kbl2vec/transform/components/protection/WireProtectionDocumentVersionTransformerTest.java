package com.foursoft.harness.kbl2vec.transform.components.protection;

import com.foursoft.harness.kbl.v25.KblWireProtection;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecGeneralTechnicalPartSpecification;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecWireProtectionSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WireProtectionDocumentVersionTransformerTest {

    @Test
    void should_transformWireProtectionDocumentVersion() {
        // Given
        final WireProtectionDocumentVersionTransformer transformer = new WireProtectionDocumentVersionTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblWireProtection source = new KblWireProtection();
        source.setCompanyName("TestCompany");
        source.setVersion("TestVersion");

        final VecGeneralTechnicalPartSpecification vecGeneralSpecification = new VecGeneralTechnicalPartSpecification();
        final VecWireProtectionSpecification vecWireProtectionSpecification = new VecWireProtectionSpecification();

        orchestrator.addMockMapping(source, vecGeneralSpecification);
        orchestrator.addMockMapping(source, vecWireProtectionSpecification);

        final VecPartVersion vecPartVersion = new VecPartVersion();

        orchestrator.addMockMapping(source, vecPartVersion);

        // When
        final VecDocumentVersion result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result)
                .isNotNull()
                .returns("TestCompany", VecDocumentVersion::getCompanyName)
                .returns("TestVersion", VecDocumentVersion::getDocumentVersion)
                .satisfies(v -> assertThat(v.getSpecifications()).containsExactlyInAnyOrder(vecGeneralSpecification,
                                                                                            vecWireProtectionSpecification))
                .satisfies(v -> assertThat(v.getReferencedPart()).containsExactly(vecPartVersion));
    }
}
