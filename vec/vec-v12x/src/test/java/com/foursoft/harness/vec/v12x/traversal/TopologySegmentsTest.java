/*-
 * ========================LICENSE_START=================================
 * VEC 1.2.X
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
package com.foursoft.harness.vec.v12x.traversal;

import com.foursoft.harness.vec.v12x.VecDocumentVersion;
import com.foursoft.harness.vec.v12x.VecNumericalValue;
import com.foursoft.harness.vec.v12x.VecSegmentCrossSectionArea;
import com.foursoft.harness.vec.v12x.VecSegmentLength;
import com.foursoft.harness.vec.v12x.VecTopologySegment;
import com.foursoft.harness.vec.v12x.VecTopologySpecification;
import com.foursoft.harness.vec.v12x.navigations.SegmentNavs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TopologySegmentsTest {

    private static final String DESIGNED = "Designed";
    private static final String RESERVED = "Reserved";

    private final VecTopologySegment segment = new VecTopologySegment();
    private final VecDocumentVersion documentVersion = new VecDocumentVersion();

    private static VecNumericalValue value(final double valueComponent) {
        final VecNumericalValue numericalValue = new VecNumericalValue();
        numericalValue.setValueComponent(valueComponent);
        return numericalValue;
    }

    @BeforeEach
    void setUp() {
        documentVersion.setDocumentNumber("DOC-1");
        final VecTopologySpecification topology = new VecTopologySpecification();
        topology.setParentDocumentVersion(documentVersion);
        segment.setParentTopologySpecification(topology);

        final VecSegmentLength length = new VecSegmentLength();
        length.setClassification(DESIGNED);
        length.setLength(value(1500.0));
        segment.getLengthInformations().add(length);

        final VecSegmentCrossSectionArea area = new VecSegmentCrossSectionArea();
        area.setCrossSectionAreaType(RESERVED);
        area.setArea(value(2.5));
        segment.getCrossSectionAreaInformations().add(area);
    }

    @Test
    void navigatesToTheParentDocumentNumberOfASegment() {
        assertThat(TopologySegments.toParentDocumentNumber().from(segment)).contains("DOC-1");
        assertThat(TopologySegments.toParentDocumentNumber().from(new VecTopologySegment())).isEmpty();
    }

    @Test
    void navigatesToTheLengthAndCrossSectionAreaInformations() {
        assertThat(TopologySegments.toLengthInformations().listFrom(segment)).hasSize(1);
        assertThat(TopologySegments.toCrossSectionAreaInformations().listFrom(segment)).hasSize(1);
    }

    @Test
    void navigatesToTheLengthOfTheRequestedClassification() {
        assertThat(TopologySegments.lengthBy(DESIGNED).from(segment)).contains(1500.0);
        assertThat(TopologySegments.lengthBy("Adapted").from(segment)).isEmpty();
    }

    @Test
    void navigatesToTheCrossSectionAreaOfTheRequestedType() {
        assertThat(TopologySegments.crossSectionAreaBy(RESERVED).from(segment)).contains(2.5);
        assertThat(TopologySegments.crossSectionAreaBy("Real").from(segment)).isEmpty();
    }

    @Test
    void navigatesToTheCrossSectionAreaWithoutAType() {
        final VecSegmentCrossSectionArea untyped = new VecSegmentCrossSectionArea();
        untyped.setArea(value(4.0));
        final VecTopologySegment untypedSegment = new VecTopologySegment();
        untypedSegment.getCrossSectionAreaInformations().add(untyped);

        assertThat(TopologySegments.crossSectionAreaBy(null).from(untypedSegment)).contains(4.0);
    }

    /**
     * Characterisation test for the deprecated {@link SegmentNavs}, which delegates to
     * {@link TopologySegments}. Can be removed together with {@link SegmentNavs}.
     */
    @Test
    @SuppressWarnings({"deprecation", "removal"})
    void deprecatedNavigationsBehaveLikeTheirReplacement() {
        assertThat(SegmentNavs.parentDocumentNumber().apply(segment))
                .isEqualTo(TopologySegments.toParentDocumentNumber().orElseNull(segment));
        assertThat(SegmentNavs.lengthBy(DESIGNED).apply(segment))
                .isEqualTo(TopologySegments.lengthBy(DESIGNED).from(segment));
        assertThat(SegmentNavs.crossSectionAreaBy(RESERVED).apply(segment))
                .isEqualTo(TopologySegments.crossSectionAreaBy(RESERVED).from(segment));
    }

}
