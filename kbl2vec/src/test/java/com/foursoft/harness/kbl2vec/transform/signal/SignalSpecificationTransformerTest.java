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
package com.foursoft.harness.kbl2vec.transform.signal;

import com.foursoft.harness.kbl.v25.KblConnection;
import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecSignal;
import com.foursoft.harness.vec.v2x.VecSignalSpecification;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SignalSpecificationTransformerTest {

    @Test
    void should_createSignalPerConnectionWithNonBlankName() {
        // Given
        final SignalSpecificationTransformer transformer = new SignalSpecificationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblHarness source = new KblHarness();
        final KblConnection namedConnection = connection("SIG_A");
        // Connections without a (usable) signal name must be filtered out and therefore need no mock mapping.
        source.getConnections().addAll(List.of(namedConnection, connection(""), connection(null)));

        final VecSignal vecSignal = new VecSignal();
        orchestrator.addMockMapping(namedConnection, vecSignal);

        // When
        final VecSignalSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("Signals", VecSignalSpecification::getIdentification);
        assertThat(result.getSignals()).containsExactly(vecSignal);
    }

    private static KblConnection connection(final String signalName) {
        final KblConnection connection = new KblConnection();
        connection.setSignalName(signalName);
        return connection;
    }
}
