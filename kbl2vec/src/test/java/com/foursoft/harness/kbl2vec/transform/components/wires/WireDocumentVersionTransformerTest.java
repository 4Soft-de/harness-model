/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
package com.foursoft.harness.kbl2vec.transform.components.wires;

import com.foursoft.harness.kbl.v25.KblCore;
import com.foursoft.harness.kbl.v25.KblGeneralWire;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WireDocumentVersionTransformerTest {

    @Test
    void should_transformWireDocumentVersionSingleCore() {
        // Given
        final WireDocumentVersionTransformer transformer = new WireDocumentVersionTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblGeneralWire source = new KblGeneralWire();

        final VecGeneralTechnicalPartSpecification vecGeneralTechnicalPartSpecification =
                new VecGeneralTechnicalPartSpecification();
        orchestrator.addMockMapping(source, vecGeneralTechnicalPartSpecification);

        final VecPartVersion vecPartVersion = new VecPartVersion();
        orchestrator.addMockMapping(source, vecPartVersion);

        final VecWireElementSpecification vecWireElementSpecification = new VecWireElementSpecification();
        final VecInsulationSpecification vecInsulationSpecification = new VecInsulationSpecification();
        final VecWireSpecification vecWireSpecification = new VecWireSpecification();
        orchestrator.addMockMapping(source, vecWireElementSpecification);
        orchestrator.addMockMapping(source, vecInsulationSpecification);
        orchestrator.addMockMapping(source, vecWireSpecification);

        final VecCoreSpecification vecCoreSpecification = new VecCoreSpecification();
        orchestrator.addMockMapping(source, vecCoreSpecification);

        // When
        final VecDocumentVersion result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getReferencedPart()).contains(vecPartVersion))
                .satisfies(v -> assertThat(v.getSpecifications()).containsExactlyInAnyOrder(vecWireElementSpecification,
                                                                                            vecInsulationSpecification,
                                                                                            vecWireSpecification,
                                                                                            vecGeneralTechnicalPartSpecification,
                                                                                            vecCoreSpecification));
    }

    @Test
    void should_transformWireDocumentVersionMultiCore() {
        // Given
        final WireDocumentVersionTransformer transformer = new WireDocumentVersionTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblGeneralWire source = new KblGeneralWire();

        final VecGeneralTechnicalPartSpecification vecGeneralTechnicalPartSpecification =
                new VecGeneralTechnicalPartSpecification();
        orchestrator.addMockMapping(source, vecGeneralTechnicalPartSpecification);

        final VecPartVersion vecPartVersion = new VecPartVersion();
        orchestrator.addMockMapping(source, vecPartVersion);

        final VecWireElementSpecification vecWireElementSpecification = new VecWireElementSpecification();
        final VecInsulationSpecification vecInsulationSpecification = new VecInsulationSpecification();
        final VecWireSpecification vecWireSpecification = new VecWireSpecification();
        orchestrator.addMockMapping(source, vecWireElementSpecification);
        orchestrator.addMockMapping(source, vecInsulationSpecification);
        orchestrator.addMockMapping(source, vecWireSpecification);

        final KblCore core = new KblCore();
        source.getCores().add(core);

        final VecWireElementSpecification coreWireElementSpecification = new VecWireElementSpecification();
        final VecInsulationSpecification coreInsulationSpecification = new VecInsulationSpecification();
        final VecCoreSpecification coreCoreSpecification = new VecCoreSpecification();
        orchestrator.addMockMapping(core, coreWireElementSpecification);
        orchestrator.addMockMapping(core, coreInsulationSpecification);
        orchestrator.addMockMapping(core, coreCoreSpecification);

        // When
        final VecDocumentVersion result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getReferencedPart()).contains(vecPartVersion))
                .satisfies(v -> assertThat(v.getSpecifications()).containsExactlyInAnyOrder(vecWireElementSpecification,
                                                                                            vecInsulationSpecification,
                                                                                            vecWireSpecification,
                                                                                            vecGeneralTechnicalPartSpecification,
                                                                                            coreCoreSpecification,
                                                                                            coreWireElementSpecification,
                                                                                            coreInsulationSpecification));
    }
}
