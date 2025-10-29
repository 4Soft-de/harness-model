package com.foursoft.harness.kbl2vec.transform.topology.placements.wire_protection.start;

import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl.v25.KblProtectionArea;
import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl.v25.KblUnit;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SegmentLocationTransformerTest {

    @Test
    void should_transformSegmentLocationFromAbsoluteLocation() {
        // Given
        final SegmentLocationTransformer transformer = new SegmentLocationTransformer();
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
        final SegmentLocationTransformer transformer = new SegmentLocationTransformer();
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
        final SegmentLocationTransformer transformer = new SegmentLocationTransformer();
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
