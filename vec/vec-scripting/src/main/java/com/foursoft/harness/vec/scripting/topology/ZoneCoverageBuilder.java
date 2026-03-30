/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2025 4Soft GmbH
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
package com.foursoft.harness.vec.scripting.topology;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.factories.NumericalValueFactory;
import com.foursoft.harness.vec.v2x.*;

public class ZoneCoverageBuilder implements Builder<VecZoneCoverage> {

    private final VecSession session;
    private final VecTopologySegment referenceSegment;
    private final VecZoneCoverage zoneCoverage;

    ZoneCoverageBuilder(final VecSession session, final VecTopologySegment referenceSegment) {
        this.session = session;
        this.referenceSegment = referenceSegment;
        this.zoneCoverage = new VecZoneCoverage();
    }

    public ZoneCoverageBuilder from(final String identification, final VecAnchorType anchorNode) {
        final VecNodeLocation location = createNodeLocation(identification, anchorNode);
        zoneCoverage.setFirstLocation(location);
        return this;
    }

    public ZoneCoverageBuilder from(final String identification, final VecAnchorType anchorNode, final double offset) {
        final VecSegmentLocation location = createSegmentLocation(identification, anchorNode,
                                                                  offset);
        zoneCoverage.setFirstLocation(location);
        return this;
    }

    public ZoneCoverageBuilder to(final String identification, final VecAnchorType anchorNode) {
        final VecNodeLocation location = createNodeLocation(identification, anchorNode);
        zoneCoverage.setSecondLocation(location);
        return this;
    }

    public ZoneCoverageBuilder to(final String identification, final VecAnchorType anchorNode, final double offset) {
        final VecSegmentLocation location = createSegmentLocation(identification, anchorNode,
                                                                  offset);
        zoneCoverage.setSecondLocation(location);
        return this;
    }

    private VecSegmentLocation createSegmentLocation(final String identification, final VecAnchorType anchorNode,
                                                     final double offset) {
        final VecSegmentLocation location = new VecSegmentLocation();
        location.setIdentification(identification);
        location.setAnchor(anchorNode);
        location.setOffset(NumericalValueFactory.value(offset, session.mm()));
        location.setReferencedSegment(referenceSegment);
        return location;
    }

    private VecNodeLocation createNodeLocation(final String identification, final VecAnchorType anchorNode) {
        final VecNodeLocation location = new VecNodeLocation();
        location.setIdentification(identification);
        if (anchorNode == VecAnchorType.FROM_START_NODE) {
            location.setReferencedNode(referenceSegment.getStartNode());
        } else {
            location.setReferencedNode(referenceSegment.getEndNode());
        }
        return location;
    }

    @Override
    public VecZoneCoverage build() {
        return this.zoneCoverage;
    }
}
