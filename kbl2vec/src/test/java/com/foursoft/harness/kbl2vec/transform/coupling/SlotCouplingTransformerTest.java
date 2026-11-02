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

import com.foursoft.harness.kbl.v25.KblCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblSlotOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.kbl2vec.transform.coupling.Matings.CavityMating;
import com.foursoft.harness.kbl2vec.transform.coupling.Matings.SlotMating;
import com.foursoft.harness.vec.v2x.VecCavityCoupling;
import com.foursoft.harness.vec.v2x.VecSlotCoupling;
import com.foursoft.harness.vec.v2x.VecSlotReference;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SlotCouplingTransformerTest {

    @Test
    void should_coupleTheMatedSlots() {
        // Given
        final KblSlotOccurrence first = new KblSlotOccurrence();
        final KblSlotOccurrence second = new KblSlotOccurrence();
        final CavityMating cavityMating = new CavityMating(new KblCavityOccurrence(), new KblCavityOccurrence());
        final SlotMating source = new SlotMating(first, second, List.of(cavityMating));

        final VecSlotReference firstReference = new VecSlotReference();
        final VecSlotReference secondReference = new VecSlotReference();
        final VecCavityCoupling cavityCoupling = new VecCavityCoupling();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(first, firstReference)
                .addMockMapping(second, secondReference)
                .addMockMapping(cavityMating, cavityCoupling);

        // When
        final VecSlotCoupling result = orchestrator.transform(new SlotCouplingTransformer(), source);

        // Then
        assertThat(result).isNotNull()
                .returns(firstReference, VecSlotCoupling::getFirstSlot)
                .returns(secondReference, VecSlotCoupling::getSecondSlot)
                .satisfies(v -> assertThat(v.getCavityCouplings()).containsExactly(cavityCoupling));
    }
}
