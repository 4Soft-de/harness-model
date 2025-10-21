package com.foursoft.harness.kbl2vec.transform.components.wires.single_cores;

import com.foursoft.harness.kbl.v25.KblGeneralWire;
import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCoreSpecification;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CoreSpecificationTransformerTest {

    @Test
    void should_transformCoreSpecification() {
        // Given
        final CoreSpecificationTransformer transformer = new CoreSpecificationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblGeneralWire source = new KblGeneralWire();

        final KblNumericalValue kblNumericalValue = new KblNumericalValue();
        source.setCrossSectionArea(kblNumericalValue);

        final VecNumericalValue vecNumericalValue = new VecNumericalValue();
        orchestrator.addMockMapping(kblNumericalValue, vecNumericalValue);

        // When
        final VecCoreSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(vecNumericalValue, VecCoreSpecification::getCrossSectionArea);
    }
}
