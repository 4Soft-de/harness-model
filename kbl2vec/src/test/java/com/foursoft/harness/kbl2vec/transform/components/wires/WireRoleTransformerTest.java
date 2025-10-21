package com.foursoft.harness.kbl2vec.transform.components.wires;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecWireElementReference;
import com.foursoft.harness.vec.v2x.VecWireRole;
import com.foursoft.harness.vec.v2x.VecWireSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WireRoleTransformerTest {

    @Test
    void should_transformWireRoleSingleCore() {
        // Given
        final WireRoleTransformer transformer = new WireRoleTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblWireOccurrence source = new KblWireOccurrence();
        source.setWireNumber("TestWireNumber");

        final KblGeneralWire part = new KblGeneralWire();
        source.setPart(part);

        final VecWireElementReference wireElementReference = new VecWireElementReference();
        orchestrator.addMockMapping(source, wireElementReference);

        final VecWireSpecification wireSpecification = new VecWireSpecification();
        orchestrator.addMockMapping(part, wireSpecification);

        // When
        final VecWireRole result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestWireNumber", VecWireRole::getIdentification)
                .returns(wireSpecification, VecWireRole::getWireSpecification)
                .satisfies(v -> assertThat(v.getWireElementReferences()).containsExactly(wireElementReference));
    }

    @Test
    void should_transformWireRoleMultiCore() {
        // Given
        final WireRoleTransformer transformer = new WireRoleTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblSpecialWireOccurrence source = new KblSpecialWireOccurrence();

        final KblGeneralWire part = new KblGeneralWire();
        source.setPart(part);

        final VecWireSpecification wireSpecification = new VecWireSpecification();
        orchestrator.addMockMapping(part, wireSpecification);

        final VecWireElementReference wireElementReference = new VecWireElementReference();
        orchestrator.addMockMapping(source, wireElementReference);

        final KblCoreOccurrence coreOccurrence = new KblCoreOccurrence();
        source.getCoreOccurrences().add(coreOccurrence);

        final VecWireElementReference coreWireElementReference = new VecWireElementReference();
        orchestrator.addMockMapping(coreOccurrence, coreWireElementReference);

        // When
        final VecWireRole result = orchestrator.transform(transformer, source);

        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getWireElementReferences()).containsExactlyInAnyOrder(wireElementReference,
                                                                                                   coreWireElementReference));
    }
}
