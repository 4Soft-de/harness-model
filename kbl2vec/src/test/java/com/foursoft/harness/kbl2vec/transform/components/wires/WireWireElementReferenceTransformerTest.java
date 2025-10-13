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
import com.foursoft.harness.vec.v2x.VecWireElement;
import com.foursoft.harness.vec.v2x.VecWireElementReference;
import com.foursoft.harness.vec.v2x.VecWireEnd;
import com.foursoft.harness.vec.v2x.VecWireLength;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WireWireElementReferenceTransformerTest {

    @Test
    void should_transformWireOccurrence() {
        // Given
        final WireWireElementReferenceTransformer transformer = new WireWireElementReferenceTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblWireOccurrence source = new KblWireOccurrence();
        source.setWireNumber("TestWireNumber");

        final KblGeneralWire part = new KblGeneralWire();
        source.setPart(part);

        final VecWireElement vecWireElement = new VecWireElement();
        orchestrator.addMockMapping(part, vecWireElement);

        final KblConnection connection = new KblConnection();
        final KblExtremity extremity = new KblExtremity();
        connection.getExtremities().add(extremity);
        source.getRefConnection().add(connection);

        final VecWireEnd vecWireEnd = new VecWireEnd();
        orchestrator.addMockMapping(extremity, vecWireEnd);

        final KblWireLength wireLength = new KblWireLength();
        source.getLengthInformations().add(wireLength);

        final VecWireLength vecWireLength = new VecWireLength();
        orchestrator.addMockMapping(wireLength, vecWireLength);

        // When
        final VecWireElementReference result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestWireNumber", VecWireElementReference::getIdentification)
                .returns(vecWireElement, VecWireElementReference::getReferencedWireElement)
                .satisfies(v -> assertThat(v.getWireLengths()).containsExactly(vecWireLength))
                .satisfies(v -> assertThat(v.getWireEnds()).containsExactly(vecWireEnd));
    }
}
