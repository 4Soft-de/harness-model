package com.foursoft.harness.kbl2vec.transform.components.protection;

import com.foursoft.harness.kbl.v25.KblWireProtection;
import com.foursoft.harness.kbl.v25.KblWireProtectionOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecWireProtectionRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WireProtectionOccurrenceTransformerTest {

    @Test
    void should_transformWireProtectionOccurrence() {
        final WireProtectionOccurrenceTransformer transformer = new WireProtectionOccurrenceTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblWireProtectionOccurrence source = new KblWireProtectionOccurrence();
        final KblWireProtection part = new KblWireProtection();
        source.setId("TestId");
        source.setPart(part);

        final VecPartVersion vecPartVersion = new VecPartVersion();
        final VecWireProtectionRole vecWireProtectionRole = new VecWireProtectionRole();
        orchestrator.addMockMapping(source, vecWireProtectionRole);
        orchestrator.addMockMapping(part, vecPartVersion);

        final VecPartOccurrence result = orchestrator.transform(transformer, source);

        assertThat(result).isNotNull()
                .returns("TestId", VecPartOccurrence::getIdentification)
                .returns(vecPartVersion, VecPartOccurrence::getPart)
                .satisfies(v -> assertThat(v.getRoles()).contains(vecWireProtectionRole));
    }
}
