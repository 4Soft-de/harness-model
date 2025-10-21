package com.foursoft.harness.kbl2vec.transform.components.wires.multi_cores;

import com.foursoft.harness.kbl.v25.KblCore;
import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCoreSpecification;
import com.foursoft.harness.vec.v2x.VecInsulationSpecification;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecWireElementSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WireElementSpecificationTransformerTest {

    @Test
    void should_transformWireElementSpecification() {
        // Given
        final WireElementSpecificationTransformer transformer = new WireElementSpecificationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCore source = new KblCore();
        source.setId("TestId");
        source.setWireType("TestWireType");

        final KblNumericalValue outsideDiameter = new KblNumericalValue();
        source.setOutsideDiameter(outsideDiameter);

        final KblNumericalValue bendRadius = new KblNumericalValue();
        source.setBendRadius(bendRadius);

        final VecNumericalValue vecBendRadius = new VecNumericalValue();
        final VecNumericalValue vecOutsideDiameter = new VecNumericalValue();
        orchestrator.addMockMapping(outsideDiameter, vecOutsideDiameter);
        orchestrator.addMockMapping(bendRadius, vecBendRadius);

        final VecInsulationSpecification insulationSpecification = new VecInsulationSpecification();
        orchestrator.addMockMapping(source, insulationSpecification);

        final VecCoreSpecification coreSpecification = new VecCoreSpecification();
        orchestrator.addMockMapping(source, coreSpecification);

        // When
        final VecWireElementSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestId", VecWireElementSpecification::getIdentification)
                .returns(vecOutsideDiameter, VecWireElementSpecification::getOutsideDiameter)
                .returns(vecBendRadius, VecWireElementSpecification::getMinBendRadiusStatic)
                .returns(coreSpecification, VecWireElementSpecification::getConductorSpecification)
                .returns(insulationSpecification, VecWireElementSpecification::getInsulationSpecification)
                .satisfies(v -> assertThat(v.getTypes().get(0).getType()).isEqualTo("TestWireType"));
    }
}
