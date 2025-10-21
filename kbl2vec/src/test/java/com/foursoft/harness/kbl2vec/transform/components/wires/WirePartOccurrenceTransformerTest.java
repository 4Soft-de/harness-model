package com.foursoft.harness.kbl2vec.transform.components.wires;

import com.foursoft.harness.kbl.v25.KblWireOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecWireRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WirePartOccurrenceTransformerTest {

    @Test
    void should_transformWirePartOccurrence() {
        // Given
        final WirePartOccurrenceTransformer transformer = new WirePartOccurrenceTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblWireOccurrence source = new KblWireOccurrence();
        source.setWireNumber("TestWireNumber");

        final VecWireRole role = new VecWireRole();
        orchestrator.addMockMapping(source, role);

        // When
        final VecPartOccurrence result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestWireNumber", VecPartOccurrence::getIdentification)
                .satisfies(v -> assertThat(v.getRoles()).containsExactly(role));
    }
}
