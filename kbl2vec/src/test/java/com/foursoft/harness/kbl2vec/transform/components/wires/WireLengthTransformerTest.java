package com.foursoft.harness.kbl2vec.transform.components.wires;

import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl.v25.KblWireLength;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecWireLength;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WireLengthTransformerTest {

    @Test
    void should_transformWireLength() {
        // Given
        final WireLengthTransformer transformer = new WireLengthTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblWireLength source = new KblWireLength();
        source.setLengthType("TestLengthType");

        final KblNumericalValue numericalValue = new KblNumericalValue();
        source.setLengthValue(numericalValue);

        final VecNumericalValue vecNumericalValue = new VecNumericalValue();
        orchestrator.addMockMapping(numericalValue, vecNumericalValue);

        // When
        final VecWireLength result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestLengthType", VecWireLength::getLengthType)
                .returns(vecNumericalValue, VecWireLength::getLengthValue);
    }
}
