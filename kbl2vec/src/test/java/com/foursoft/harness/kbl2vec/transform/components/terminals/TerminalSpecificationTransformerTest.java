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
package com.foursoft.harness.kbl2vec.transform.components.terminals;

import com.foursoft.harness.kbl.v25.KblGeneralTerminal;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TerminalSpecificationTransformerTest {

    @Test
    void should_transformTerminalSpecification() {
        // Given
        final TerminalSpecificationTransformer transformer = new TerminalSpecificationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblGeneralTerminal source = new KblGeneralTerminal();
        source.setTerminalType("TestTerminalType");

        final VecPartVersion vecPartVersion = new VecPartVersion();
        orchestrator.addMockMapping(source, vecPartVersion);

        final VecWireReception vecWireReception = new VecWireReception();
        final VecTerminalReception vecTerminalReception = new VecTerminalReception();
        final VecInternalTerminalConnection vecInternalTerminalConnection = new VecInternalTerminalConnection();
        orchestrator.addMockMapping(source, vecWireReception);
        orchestrator.addMockMapping(source, vecTerminalReception);
        orchestrator.addMockMapping(source, vecInternalTerminalConnection);

        // When
        final VecTerminalSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestTerminalType", VecTerminalSpecification::getSpecialPartType)
                .satisfies(v -> assertThat(v.getDescribedPart()).containsExactly(vecPartVersion))
                .satisfies(v -> assertThat(v.getTerminalReceptions()).containsExactly(vecTerminalReception))
                .satisfies(v -> assertThat(v.getWireReceptions()).containsExactly(vecWireReception))
                .satisfies(v -> assertThat(v.getInternalTerminalConnections()).containsExactly(
                        vecInternalTerminalConnection));
    }
}
