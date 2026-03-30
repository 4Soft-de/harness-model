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
package com.foursoft.harness.kbl2vec.transform.placements;

import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl.v25.KblProtectionArea;
import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl.v25.KblUnit;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StartSegmentLocationTransformerTest {

    @Test
    void should_transformSegmentLocationFromAbsoluteLocation() {
        // Given
        final StartSegmentLocationTransformer transformer = new StartSegmentLocationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblProtectionArea source = new KblProtectionArea();

        final KblSegment segment = new KblSegment();
        source.setParentSegment(segment);

        final VecTopologySegment topologySegment = new VecTopologySegment();
        orchestrator.addMockMapping(segment, topologySegment);

        final KblNumericalValue absoluteLocation = new KblNumericalValue();
        final KblUnit unit = new KblUnit();
        absoluteLocation.setUnitComponent(unit);
        source.setAbsoluteStartLocation(absoluteLocation);

        final VecNumericalValue offset = new VecNumericalValue();
        orchestrator.addMockMapping(absoluteLocation, offset);

        // When
        final VecSegmentLocation result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(topologySegment, VecSegmentLocation::getReferencedSegment)
                .returns(offset, VecSegmentLocation::getOffset);
    }

    @Test
    void should_transformSegmentLocationFromRelativeLocationAndPhysicalLength() {
        // Given
        final StartSegmentLocationTransformer transformer = new StartSegmentLocationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblProtectionArea source = new KblProtectionArea();
        source.setStartLocation(1.0);

        final KblSegment segment = new KblSegment();
        source.setParentSegment(segment);

        final VecTopologySegment topologySegment = new VecTopologySegment();
        orchestrator.addMockMapping(segment, topologySegment);

        final KblNumericalValue physicalLength = new KblNumericalValue();
        physicalLength.setValueComponent(1.0);
        segment.setPhysicalLength(physicalLength);

        final KblUnit unit = new KblUnit();
        physicalLength.setUnitComponent(unit);

        final VecUnit vecUnit = new VecCustomUnit();
        orchestrator.addMockMapping(unit, vecUnit);

        // When
        final VecSegmentLocation result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(topologySegment, VecSegmentLocation::getReferencedSegment)
                .satisfies(v -> assertThat(v.getOffset().getUnitComponent()).isEqualTo(vecUnit))
                .satisfies(v -> assertThat(v.getOffset().getValueComponent()).isEqualTo(1.0));
    }

    @Test
    void should_transformSegmentLocationFromRelativeLocationAndVirtualLength() {
        // Given
        final StartSegmentLocationTransformer transformer = new StartSegmentLocationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblProtectionArea source = new KblProtectionArea();
        source.setStartLocation(1.0);

        final KblSegment segment = new KblSegment();
        source.setParentSegment(segment);

        final VecTopologySegment topologySegment = new VecTopologySegment();
        orchestrator.addMockMapping(segment, topologySegment);

        final KblNumericalValue virtualLength = new KblNumericalValue();
        virtualLength.setValueComponent(1.0);
        segment.setVirtualLength(virtualLength);

        final KblUnit unit = new KblUnit();
        virtualLength.setUnitComponent(unit);

        final VecUnit vecUnit = new VecCustomUnit();
        orchestrator.addMockMapping(unit, vecUnit);

        // When
        final VecSegmentLocation result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(topologySegment, VecSegmentLocation::getReferencedSegment)
                .satisfies(v -> assertThat(v.getOffset().getUnitComponent()).isEqualTo(vecUnit))
                .satisfies(v -> assertThat(v.getOffset().getValueComponent()).isEqualTo(1.0));
    }
}
