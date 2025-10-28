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
            builder.withLinker(Query.of(protectionArea.getParentSegment()), VecTopologySegment.class,
                               VecSegmentLocation::setReferencedSegment);

            if (locationData.absoluteLocation != null) {
                return builder
                        .withDownstream(KblNumericalValue.class, VecNumericalValue.class,
                                        Query.of(protectionArea.getAbsoluteStartLocation()),
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
