/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 - 2026 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.transform.coupling;

import com.foursoft.harness.kbl.v25.KblComponentBoxOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentSlotOccurrence;
import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCouplingPoint;
import com.foursoft.harness.vec.v2x.VecCouplingSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CouplingSpecificationTransformerTest {

    private final CouplingSpecificationTransformer transformer = new CouplingSpecificationTransformer();

    @Test
    void should_coupleComponentsMountedOnAComponentSlot() {
        // Given
        final KblComponentOccurrence mounted = new KblComponentOccurrence();
        mounted.getMounting().add(new KblComponentSlotOccurrence());
        final KblComponentOccurrence notMountedOnASlot = new KblComponentOccurrence();
        notMountedOnASlot.getMounting().add(new KblComponentBoxOccurrence());
        final KblHarness source = new KblHarness();
        source.setPartNumber("H-001");
        source.getComponentOccurrences().add(mounted);
        source.getComponentOccurrences().add(notMountedOnASlot);

        final VecCouplingPoint couplingPoint = new VecCouplingPoint();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(mounted, couplingPoint);

        // When
        final VecCouplingSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("CS-H-001", VecCouplingSpecification::getIdentification)
                .satisfies(v -> assertThat(v.getCouplingPoints()).containsExactly(couplingPoint));
    }

    @Test
    void should_notTransform_whenNoComponentIsMountedOnASlot() {
        // Given
        final KblHarness source = new KblHarness();
        source.getComponentOccurrences().add(new KblComponentOccurrence());

        // When
        final VecCouplingSpecification result = new TestConversionOrchestrator().transform(transformer, source);

        // Then
        assertThat(result).isNull();
    }
}
