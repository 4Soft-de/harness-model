package com.foursoft.harness.kbl2vec.transform.components.special_terminals;

import com.foursoft.harness.kbl.v25.KblGeneralTerminal;
import com.foursoft.harness.kbl.v25.KblSpecialTerminalOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecTerminalRole;
import com.foursoft.harness.vec.v2x.VecTerminalSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TerminalRoleTransformerTest {

    @Test
    void should_transformTerminalRole() {
        // Given
        final TerminalRoleTransformer transformer = new TerminalRoleTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblSpecialTerminalOccurrence source = new KblSpecialTerminalOccurrence();
        source.setId("TestId");

        final KblGeneralTerminal part = new KblGeneralTerminal();
        source.setPart(part);

        final VecTerminalSpecification specification = new VecTerminalSpecification();
        orchestrator.addMockMapping(part, specification);

        // When
        final VecTerminalRole result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestId", VecTerminalRole::getIdentification)
                .returns(specification, VecTerminalRole::getTerminalSpecification);
    }
}
