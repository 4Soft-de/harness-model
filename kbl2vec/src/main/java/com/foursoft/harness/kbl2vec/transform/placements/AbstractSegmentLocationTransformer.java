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
import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl2vec.core.*;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecSegmentLocation;
import com.foursoft.harness.vec.v2x.VecTopologySegment;
import com.foursoft.harness.vec.v2x.VecUnit;

public abstract class AbstractSegmentLocationTransformer<S> implements Transformer<S, VecSegmentLocation> {

    protected abstract LocationData extractLocationData(S source);

    @Override
    public TransformationResult<VecSegmentLocation> transform(final TransformationContext context, final S source) {
        final VecSegmentLocation destination = new VecSegmentLocation();

        final LocationData locationData = extractLocationData(source);
        destination.setIdentification(locationData.identification);

        final TransformationResult.Builder<VecSegmentLocation> builder = TransformationResult.from(destination)
                .withLinker(Query.of(locationData.segment), VecTopologySegment.class,
                            VecSegmentLocation::setReferencedSegment);

        if (locationData.absoluteLocation != null) {
            return builder
                    .withDownstream(KblNumericalValue.class, VecNumericalValue.class,
                                    Query.of(locationData.absoluteLocation),
                                    VecSegmentLocation::setOffset)
                    .build();
        }

        return builder.withFragment(deriveAbsoluteLocation(locationData))
                .build();
    }

    private static TransformationFragment<VecSegmentLocation, TransformationResult.Builder<VecSegmentLocation>> deriveAbsoluteLocation(
            final LocationData locationData) {
        return (location, builder) -> {
            final VecNumericalValue absoluteLocation = new VecNumericalValue();
            absoluteLocation.setValueComponent(Double.NaN);
            location.setOffset(absoluteLocation);

            final KblNumericalValue segmentLength = resolveSegmentLength(locationData.segment());
            if (segmentLength != null) {
                builder.withLinker(Query.of(segmentLength.getUnitComponent()), VecUnit.class,
                                   (dest, unit) -> dest.getOffset().setUnitComponent(unit));

                if (!Double.isNaN(locationData.relativeLocation())) {
                    absoluteLocation.setValueComponent(
                            locationData.relativeLocation() * segmentLength.getValueComponent());
                }
            }
        };
    }

    private static KblNumericalValue resolveSegmentLength(final KblSegment segment) {
        if (segment.getPhysicalLength() != null) {
            return segment.getPhysicalLength();
        }
        if (segment.getVirtualLength() != null) {
            return segment.getVirtualLength();
        }
        return null;
    }

    public record LocationData(double relativeLocation, KblNumericalValue absoluteLocation, String identification,
                               KblSegment segment) {
    }
}
