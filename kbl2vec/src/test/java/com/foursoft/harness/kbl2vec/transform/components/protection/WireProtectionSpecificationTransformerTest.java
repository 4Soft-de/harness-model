package com.foursoft.harness.kbl2vec.transform.components.protection;

import com.foursoft.harness.kbl.v25.KblWireProtection;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecWireProtectionSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WireProtectionSpecificationTransformerTest {

    @Test
    void should_transformWireProtectionSpecification() {
        final WireProtectionSpecificationTransformer transformer = new WireProtectionSpecificationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblWireProtection source = new KblWireProtection();
        source.setProtectionType("TestProtectionType");
        source.setPartNumber("TestPartNumber");
        final String expectedIdentification = "WPS-TestPartNumber";

        final VecPartVersion vecPartVersion = new VecPartVersion();
        orchestrator.addMockMapping(source, vecPartVersion);

        final VecWireProtectionSpecification result = orchestrator.transform(transformer, source);

        assertThat(result).isNotNull()
                .returns("TestProtectionType", VecWireProtectionSpecification::getSpecialPartType)
                .returns(expectedIdentification, VecWireProtectionSpecification::getIdentification)
                .satisfies(v -> assertThat(v.getDescribedPart()).containsExactly(vecPartVersion));
    }
}
