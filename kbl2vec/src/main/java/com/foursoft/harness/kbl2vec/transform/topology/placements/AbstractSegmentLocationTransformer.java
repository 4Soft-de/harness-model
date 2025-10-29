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
package com.foursoft.harness.kbl2vec.transform.topology.placements;

import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl.v25.KblProtectionArea;
import com.foursoft.harness.kbl.v25.KblUnit;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecSegmentLocation;
import com.foursoft.harness.vec.v2x.VecTopologySegment;
import com.foursoft.harness.vec.v2x.VecUnit;

public abstract class AbstractSegmentLocationTransformer<S> implements Transformer<S, VecSegmentLocation> {

    @Override
    public TransformationResult<VecSegmentLocation> transform(final TransformationContext context, final S source) {
        final VecSegmentLocation destination = new VecSegmentLocation();

        final LocationData locationData = extractLocationData(source);
        destination.setIdentification(locationData.identification);

        final TransformationResult.Builder<VecSegmentLocation> builder = TransformationResult.from(destination);

        if (source instanceof final KblProtectionArea protectionArea) {
            builder.withLinker(Query.of(protectionArea::getParentSegment), VecTopologySegment.class,
                               VecSegmentLocation::setReferencedSegment);

            if (locationData.absoluteLocation != null) {
                return builder
                        .withDownstream(KblNumericalValue.class, VecNumericalValue.class,
                                        Query.of(locationData.absoluteLocation),
                                        VecSegmentLocation::setOffset)
                        .build();
            }

            destination.setOffset(calculateAbsoluteLocation(source, locationData));
            return builder
                    .withLinker(Query.of(locationData::baseUnit), VecUnit.class,
                                (dest, unit) -> dest.getOffset().setUnitComponent(unit))
                    .build();
        }
        return TransformationResult.noResult();
    }

    private VecNumericalValue calculateAbsoluteLocation(final S source, final LocationData locationData) {
        final VecNumericalValue absoluteLocation = new VecNumericalValue();

        if (Double.isNaN(locationData.relativeLocation)) {
            return absoluteLocation;
        }

        if (source instanceof final KblProtectionArea protectionArea) {

            if (protectionArea.getParentSegment().getPhysicalLength() != null) {
                absoluteLocation.setValueComponent(
                        protectionArea.getParentSegment().getPhysicalLength().getValueComponent() *
                                locationData.relativeLocation);
            } else if (protectionArea.getParentSegment().getVirtualLength() != null) {
                absoluteLocation.setValueComponent(
                        protectionArea.getParentSegment().getVirtualLength().getValueComponent() *
                                locationData.relativeLocation);
            }
        }
        return absoluteLocation;
    }

    protected abstract LocationData extractLocationData(S source);

    public record LocationData(double relativeLocation, KblNumericalValue absoluteLocation, KblUnit baseUnit,
                               String identification) {
    }
}
