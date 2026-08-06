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
package com.foursoft.harness.vec.v12x.navigations;

import com.foursoft.harness.vec.common.annotations.RequiresBackReferences;
import com.foursoft.harness.vec.v12x.VecTopologySegment;
import com.foursoft.harness.vec.v12x.traversal.TopologySegments;

import java.util.Optional;
import java.util.function.Function;

/**
 * Navigation methods for segments such as the {@link VecTopologySegment}.
 *
 * @deprecated Use {@link TopologySegments} instead.
 */
@Deprecated(forRemoval = true)
public final class SegmentNavs {

    private SegmentNavs() {
        // hide default constructor
    }

    /**
     * @deprecated Use {@link TopologySegments#toParentDocumentNumber()} instead.
     */
    @Deprecated(forRemoval = true)
    @RequiresBackReferences
    public static Function<VecTopologySegment, String> parentDocumentNumber() {
        return TopologySegments.toParentDocumentNumber()::orElseNull;
    }

    /**
     * @deprecated Use {@link TopologySegments#lengthBy(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecTopologySegment, Optional<Double>> lengthBy(final String segmentLengthClassification) {
        return TopologySegments.lengthBy(segmentLengthClassification);
    }

    /**
     * @deprecated Use {@link TopologySegments#crossSectionAreaBy(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecTopologySegment, Optional<Double>> crossSectionAreaBy(final String areaType) {
        return TopologySegments.crossSectionAreaBy(areaType);
    }

}
