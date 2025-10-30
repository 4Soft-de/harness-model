package com.foursoft.harness.kbl2vec.transform.placements;

import com.foursoft.harness.kbl.v25.KblFixingAssignment;
import com.foursoft.harness.kbl.v25.KblNumericalValue;
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

        final KblFixingAssignment source = new KblFixingAssignment();

        final KblSegment segment = new KblSegment();
        source.setParentSegment(segment);

        final VecTopologySegment topologySegment = new VecTopologySegment();
        orchestrator.addMockMapping(segment, topologySegment);

        final KblNumericalValue absoluteLocation = new KblNumericalValue();
        source.setAbsoluteLocation(absoluteLocation);

        final VecNumericalValue vecAbsoluteLocation = new VecNumericalValue();
        orchestrator.addMockMapping(absoluteLocation, vecAbsoluteLocation);

        // When
        final VecSegmentLocation result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(Constants.FIXING_LOCATION_ID, VecSegmentLocation::getIdentification)
                .returns(topologySegment, VecSegmentLocation::getReferencedSegment)
                .returns(vecAbsoluteLocation, VecSegmentLocation::getOffset);
    }

    @Test
    void should_transformSegmentLocationFromRelativeLocationAndPhysicalLength() {
        // Given
        final SegmentLocationTransformer transformer = new SegmentLocationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblFixingAssignment source = new KblFixingAssignment();
        source.setLocation(1.0);

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

        final KblFixingAssignment source = new KblFixingAssignment();
        source.setLocation(1.0);

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
