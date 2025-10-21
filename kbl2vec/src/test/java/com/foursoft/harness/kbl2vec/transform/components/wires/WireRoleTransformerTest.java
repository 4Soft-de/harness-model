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
