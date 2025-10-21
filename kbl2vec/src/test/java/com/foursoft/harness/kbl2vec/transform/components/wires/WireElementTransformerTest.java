package com.foursoft.harness.kbl2vec.transform.components.wires;

import com.foursoft.harness.kbl.v25.KblCore;
import com.foursoft.harness.kbl.v25.KblGeneralWire;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecWireElement;
import com.foursoft.harness.vec.v2x.VecWireElementSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WireElementTransformerTest {

    @Test
    void should_transformWireElement() {
        // Given
        final WireElementTransformer transformer = new WireElementTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblGeneralWire source = new KblGeneralWire();

        final KblCore kblCore = new KblCore();
        source.getCores().add(kblCore);

        final VecWireElement subWireElement = new VecWireElement();
        orchestrator.addMockMapping(kblCore, subWireElement);

        final VecWireElementSpecification vecWireElementSpecification = new VecWireElementSpecification();
        orchestrator.addMockMapping(source, vecWireElementSpecification);

        // When
        final VecWireElement result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(vecWireElementSpecification, VecWireElement::getWireElementSpecification)
                .satisfies(v -> assertThat(v.getSubWireElements()).containsExactly(subWireElement));
    }
}
