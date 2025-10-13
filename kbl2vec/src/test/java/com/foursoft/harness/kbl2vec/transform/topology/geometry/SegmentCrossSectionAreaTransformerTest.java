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
package com.foursoft.harness.kbl2vec.transform.topology.geometry;

import com.foursoft.harness.kbl.v25.KblCrossSectionArea;
import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl.v25.KblValueDetermination;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecSegmentCrossSectionArea;
import com.foursoft.harness.vec.v2x.VecValueDetermination;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SegmentCrossSectionAreaTransformerTest {
    @Test
    void should_transformCalculatedSegmentCrossSectionArea() {
        // Given
        final SegmentCrossSectionAreaTransformer transformer = new SegmentCrossSectionAreaTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCrossSectionArea source = new KblCrossSectionArea();
        source.setValueDetermination(KblValueDetermination.CALCULATED);

        final KblNumericalValue areaValue = new KblNumericalValue();
        source.setArea(areaValue);

        final VecNumericalValue vecNumericalValue = new VecNumericalValue();
        orchestrator.addMockMapping(areaValue, vecNumericalValue);

        // When
        final VecSegmentCrossSectionArea result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(vecNumericalValue, VecSegmentCrossSectionArea::getArea)
                .returns("reserved", VecSegmentCrossSectionArea::getCrossSectionAreaType)
                .returns(VecValueDetermination.CALCULATED, VecSegmentCrossSectionArea::getValueDetermination);
    }

    @Test
    void should_transformMeasuredSegmentCrossSectionArea() {
        // Given
        final SegmentCrossSectionAreaTransformer transformer = new SegmentCrossSectionAreaTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCrossSectionArea source = new KblCrossSectionArea();
        source.setValueDetermination(KblValueDetermination.MEASURED);

        final KblNumericalValue areaValue = new KblNumericalValue();
        source.setArea(areaValue);

        final VecNumericalValue vecNumericalValue = new VecNumericalValue();
        orchestrator.addMockMapping(areaValue, vecNumericalValue);

        // When
        final VecSegmentCrossSectionArea result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(vecNumericalValue, VecSegmentCrossSectionArea::getArea)
                .returns("real", VecSegmentCrossSectionArea::getCrossSectionAreaType)
                .returns(VecValueDetermination.MEASURED, VecSegmentCrossSectionArea::getValueDetermination);
    }
}
