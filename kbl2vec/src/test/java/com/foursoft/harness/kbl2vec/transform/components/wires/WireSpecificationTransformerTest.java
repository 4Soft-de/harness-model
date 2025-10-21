package com.foursoft.harness.kbl2vec.transform.components.wires;

import com.foursoft.harness.kbl.v25.KblGeneralWire;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecWireElement;
import com.foursoft.harness.vec.v2x.VecWireSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WireSpecificationTransformerTest {

    @Test
    void should_transformWireSpecification() {
        // Given
        final WireSpecificationTransformer transformer = new WireSpecificationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblGeneralWire source = new KblGeneralWire();

        final VecWireElement vecWireElement = new VecWireElement();
        orchestrator.addMockMapping(source, vecWireElement);

        final VecPartVersion vecPartVersion = new VecPartVersion();
        orchestrator.addMockMapping(source, vecPartVersion);

        // When
        final VecWireSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getDescribedPart()).containsExactly(vecPartVersion))
                .returns(vecWireElement, VecWireSpecification::getWireElement);
    }
}
