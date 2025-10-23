package com.foursoft.harness.kbl2vec.transform.components.special_terminals;

import com.foursoft.harness.kbl.v25.KblAliasIdentification;
import com.foursoft.harness.kbl.v25.KblGeneralTerminal;
import com.foursoft.harness.kbl.v25.KblSpecialTerminalOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecAliasIdentification;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecTerminalRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TerminalOccurrenceTransformerTest {

    @Test
    void should_transformTerminalOccurrence() {
        // Given
        final TerminalOccurrenceTransformer transformer = new TerminalOccurrenceTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblSpecialTerminalOccurrence source = new KblSpecialTerminalOccurrence();

        final KblGeneralTerminal part = new KblGeneralTerminal();
        source.setPart(part);

        final VecPartVersion vecPartVersion = new VecPartVersion();
        orchestrator.addMockMapping(part, vecPartVersion);

        final KblAliasIdentification aliasIdentification = new KblAliasIdentification();
        source.getAliasIds().add(aliasIdentification);

        final VecAliasIdentification vecAliasIdentification = new VecAliasIdentification();
        orchestrator.addMockMapping(aliasIdentification, vecAliasIdentification);

        final VecTerminalRole vecTerminalRole = new VecTerminalRole();
        orchestrator.addMockMapping(source, vecTerminalRole);

        // When
        final VecPartOccurrence result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(vecPartVersion, VecPartOccurrence::getPart)
                .satisfies(v -> assertThat(v.getRoles()).contains(vecTerminalRole))
                .satisfies(v -> assertThat(v.getAliasIds()).contains(vecAliasIdentification));
    }
}
