package com.foursoft.harness.kbl2vec.transform.components.wires.multi_cores;

import com.foursoft.harness.kbl.v25.KblCore;
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

        final KblCore source = new KblCore();
        source.setId("TestId");

        final KblNumericalValue crossSectionArea = new KblNumericalValue();
        source.setCrossSectionArea(crossSectionArea);

        final VecNumericalValue vecCrossSectionArea = new VecNumericalValue();
        orchestrator.addMockMapping(crossSectionArea, vecCrossSectionArea);

        // When
        final VecCoreSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestId", VecCoreSpecification::getIdentification)
                .returns(vecCrossSectionArea, VecCoreSpecification::getCrossSectionArea);
    }
}
