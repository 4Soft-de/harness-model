package com.foursoft.harness.kbl2vec.transform.components.wires.multi_cores;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecWireElement;
import com.foursoft.harness.vec.v2x.VecWireElementReference;
import com.foursoft.harness.vec.v2x.VecWireEnd;
import com.foursoft.harness.vec.v2x.VecWireLength;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WireElementReferenceTransformerTest {

    @Test
    void should_transformWireElementReference() {
        // Given
        final WireElementReferenceTransformer transformer = new WireElementReferenceTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCoreOccurrence source = new KblCoreOccurrence();
        source.setWireNumber("TestWireNumber");

        final KblCore part = new KblCore();
        source.setPart(part);

        final VecWireElement vecWireElement = new VecWireElement();
        orchestrator.addMockMapping(part, vecWireElement);

        final KblWireLength wireLength = new KblWireLength();
        source.getLengthInformations().add(wireLength);

        final VecWireLength vecWireLength = new VecWireLength();
        orchestrator.addMockMapping(wireLength, vecWireLength);

        final KblExtremity extremity = new KblExtremity();
        final KblConnection connection = new KblConnection();
        connection.getExtremities().add(extremity);
        source.getRefConnection().add(connection);

        final VecWireEnd vecWireEnd = new VecWireEnd();
        orchestrator.addMockMapping(extremity, vecWireEnd);

        // When
        final VecWireElementReference result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestWireNumber", VecWireElementReference::getIdentification)
                .returns(vecWireElement, VecWireElementReference::getReferencedWireElement)
                .satisfies(v -> assertThat(v.getWireLengths()).containsExactly(vecWireLength))
                .satisfies(v -> assertThat(v.getWireEnds()).containsExactly(vecWireEnd));
    }
}
