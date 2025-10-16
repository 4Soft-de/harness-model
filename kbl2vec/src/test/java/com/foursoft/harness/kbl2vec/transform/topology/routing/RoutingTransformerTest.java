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
package com.foursoft.harness.kbl2vec.transform.topology.routing;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecPath;
import com.foursoft.harness.vec.v2x.VecRouting;
import com.foursoft.harness.vec.v2x.VecTopologySegment;
import com.foursoft.harness.vec.v2x.VecWireElementReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoutingTransformerTest {

    @Test
    void should_transformRouting() {
        // Given
        final RoutingTransformer transformer = new RoutingTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblRouting source = new KblRouting();
        source.setId("TestId");

        final KblSegment kblSegment = new KblSegment();
        source.getMandatorySegments().add(kblSegment);

        final VecTopologySegment vecTopologySegment = new VecTopologySegment();
        orchestrator.addMockMapping(kblSegment, vecTopologySegment);

        final VecPath vecPath = new VecPath();
        orchestrator.addMockMapping(source, vecPath);

        final KblConnection kblConnection = new KblConnection();
        final KblWireOccurrence kblWireOccurrence = new KblWireOccurrence();
        kblConnection.setWire(kblWireOccurrence);
        source.setRoutedWire(kblConnection);

        final VecWireElementReference vecWireElementReference = new VecWireElementReference();
        orchestrator.addMockMapping(kblWireOccurrence, vecWireElementReference);

        // When
        final VecRouting result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestId", VecRouting::getIdentification)
                .returns(vecPath, VecRouting::getPath)
                .returns(vecWireElementReference, VecRouting::getRoutedElement)
                .satisfies(v -> assertThat(v.getMandatorySegment()).containsExactly(vecTopologySegment));
    }
}
