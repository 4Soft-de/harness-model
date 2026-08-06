/*-
 * ========================LICENSE_START=================================
 * VEC 2.X
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
package com.foursoft.harness.vec.v2x.traversal;

import com.foursoft.harness.vec.common.annotations.RequiresBackReferences;
import com.foursoft.harness.vec.common.traversal.MultiNavigation;
import com.foursoft.harness.vec.common.traversal.Navigations;
import com.foursoft.harness.vec.common.traversal.SingleNavigation;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecSegmentCrossSectionArea;
import com.foursoft.harness.vec.v2x.VecSegmentLength;
import com.foursoft.harness.vec.v2x.VecTopologySegment;
import com.foursoft.harness.vec.v2x.VecTopologySpecification;

import java.util.Objects;

/**
 * Navigations starting at a {@link VecTopologySegment}.
 * <p>
 * A segment carries its length and cross section area several times over, once per classification, which is
 * why the navigations to them are named after the value they select rather than after a plain step.
 */
public final class TopologySegments {

    private TopologySegments() {
        // hide default constructor
    }

    /**
     * Navigates to the document number of the {@link com.foursoft.harness.vec.v2x.VecDocumentVersion} the
     * {@link VecTopologySpecification} of a segment belongs to.
     *
     * @return A navigation to the parent document number of a segment.
     */
    @RequiresBackReferences
    public static SingleNavigation<VecTopologySegment, String> toParentDocumentNumber() {
        return Navigations.nullable(VecTopologySegment::getParentTopologySpecification)
                .then(Specifications.toParentDocumentNumber());
    }

    /**
     * Navigates to the length informations of a segment.
     *
     * @return A navigation to the length informations of a segment.
     */
    public static MultiNavigation<VecTopologySegment, VecSegmentLength> toLengthInformations() {
        return Navigations.<VecTopologySegment, VecSegmentLength>collection(VecTopologySegment::getLengthInformations)
                .filter(Objects::nonNull);
    }

    /**
     * Navigates to the cross section area informations of a segment.
     *
     * @return A navigation to the cross section area informations of a segment.
     */
    public static MultiNavigation<VecTopologySegment, VecSegmentCrossSectionArea> toCrossSectionAreaInformations() {
        return Navigations.<VecTopologySegment, VecSegmentCrossSectionArea>collection(
                        VecTopologySegment::getCrossSectionAreaInformations)
                .filter(Objects::nonNull);
    }

    /**
     * Navigates to the length of a segment with the given classification.
     *
     * @param segmentLengthClassification Classification of the length to navigate to.
     * @return A navigation to the length value of the given classification.
     */
    public static SingleNavigation<VecTopologySegment, Double> lengthBy(final String segmentLengthClassification) {
        return toLengthInformations()
                .filter(length -> segmentLengthClassification.equals(length.getClassification()))
                .atMostOne()
                .then(Navigations.nullable(VecSegmentLength::getLength))
                .then(Navigations.nullable(VecNumericalValue::getValueComponent));
    }

    /**
     * Navigates to the cross section area of a segment of the given type.
     *
     * @param areaType Type of the cross section area to navigate to, which may be {@code null}.
     * @return A navigation to the cross section area value of the given type.
     */
    public static SingleNavigation<VecTopologySegment, Double> crossSectionAreaBy(final String areaType) {
        return toCrossSectionAreaInformations()
                .filter(area -> Objects.equals(areaType, area.getCrossSectionAreaType()))
                .atMostOne()
                .then(Navigations.nullable(VecSegmentCrossSectionArea::getArea))
                .then(Navigations.nullable(VecNumericalValue::getValueComponent));
    }

}
