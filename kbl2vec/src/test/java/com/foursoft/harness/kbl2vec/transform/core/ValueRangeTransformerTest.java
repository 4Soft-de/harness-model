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
package com.foursoft.harness.kbl2vec.transform.core;

import com.foursoft.harness.kbl.v25.KblUnit;
import com.foursoft.harness.kbl.v25.KblValueRange;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecOtherUnit;
import com.foursoft.harness.vec.v2x.VecUnit;
import com.foursoft.harness.vec.v2x.VecValueRange;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValueRangeTransformerTest {

    @Test
    void should_transformValueRange() {
        // Given
        final ValueRangeTransformer transformer = new ValueRangeTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblValueRange source = new KblValueRange();

        final double expectedMinimum = 1.0;
        final double expectedMaximum = 2.0;
        source.setMinimum(expectedMinimum);
        source.setMaximum(expectedMaximum);

        final KblUnit kblUnit = new KblUnit();
        source.setUnitComponent(kblUnit);

        final VecUnit vecUnit = new VecOtherUnit();
        orchestrator.addMockMapping(kblUnit, vecUnit);

        // When
        final VecValueRange result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result)
                .isNotNull()
                .returns(expectedMinimum, VecValueRange::getMinimum)
                .returns(expectedMaximum, VecValueRange::getMaximum)
                .returns(vecUnit, VecValueRange::getUnitComponent);
    }
}
