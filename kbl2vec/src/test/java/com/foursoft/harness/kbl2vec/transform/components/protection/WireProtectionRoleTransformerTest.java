package com.foursoft.harness.kbl2vec.transform.components.protection;

import com.foursoft.harness.kbl.v25.KblWireProtection;
import com.foursoft.harness.kbl.v25.KblWireProtectionOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecWireProtectionRole;
import com.foursoft.harness.vec.v2x.VecWireProtectionSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WireProtectionRoleTransformerTest {

    @Test
    void should_transformWireProtectionRole() {
        final WireProtectionRoleTransformer transformer = new WireProtectionRoleTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblWireProtectionOccurrence source = new KblWireProtectionOccurrence();
        source.setId("TestId");

        final KblWireProtection part = new KblWireProtection();
        source.setPart(part);

        final VecWireProtectionSpecification vecWireProtectionSpecification = new VecWireProtectionSpecification();
        orchestrator.addMockMapping(part, vecWireProtectionSpecification);

        final VecWireProtectionRole result = orchestrator.transform(transformer, source);

        assertThat(result).isNotNull()
                .returns("TestId", VecWireProtectionRole::getIdentification)
                .returns(vecWireProtectionSpecification, VecWireProtectionRole::getWireProtectionSpecification);
    }
}
