package com.foursoft.harness.kbl2vec.transform.topology.placements.wire_protection;

import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl.v25.KblProtectionArea;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecSegmentLocation;
import com.foursoft.harness.vec.v2x.VecTopologySegment;

public class SegmentLocationTransformer implements Transformer<KblProtectionArea, VecSegmentLocation> {

    @Override
    public TransformationResult<VecSegmentLocation> transform(final TransformationContext context,
                                                              final KblProtectionArea source) {
        final VecSegmentLocation destination = new VecSegmentLocation();
        final TransformationResult.Builder<VecSegmentLocation> builder = TransformationResult.from(destination)
                .withLinker(Query.of(source.getParentSegment()), VecTopologySegment.class,
                            VecSegmentLocation::setReferencedSegment);

        if (source.getAbsoluteStartLocation() != null) {
            return builder
                    .withDownstream(KblNumericalValue.class, VecNumericalValue.class,
                                    Query.of(source.getAbsoluteStartLocation()), VecSegmentLocation::setOffset)
                    .build();
        }

        if (source.getAbsoluteEndLocation() != null) {
            return builder
                    .withDownstream(KblNumericalValue.class, VecNumericalValue.class,
                                    Query.of(source.getAbsoluteEndLocation()), VecSegmentLocation::setOffset)
                    .build();
        }

        final double relativeStartLocation = source.getStartLocation();
        final double relativeEndLocation = source.getEndLocation();
        return builder
                .withDownstream(KblNumericalValue.class, VecNumericalValue.class,
                                Query.of(calculateAbsoluteLocation(source, relativeStartLocation)),
                                VecSegmentLocation::setOffset)
                .withDownstream(KblNumericalValue.class, VecNumericalValue.class,
                                Query.of(calculateAbsoluteLocation(source, relativeEndLocation)),
                                VecSegmentLocation::setOffset)
                .build();
    }

    private KblNumericalValue calculateAbsoluteLocation(final KblProtectionArea source,
                                                        final double relativeStartLocation) {
        final KblNumericalValue absoluteStartLocation = new KblNumericalValue();

        if (source.getParentSegment().getPhysicalLength() != null) {
            absoluteStartLocation.setValueComponent(
                    source.getParentSegment().getPhysicalLength().getValueComponent() * relativeStartLocation
            );
            absoluteStartLocation.setUnitComponent(source.getParentSegment().getPhysicalLength().getUnitComponent());
            return absoluteStartLocation;
        }

        if (source.getParentSegment().getVirtualLength() != null) {
            absoluteStartLocation.setValueComponent(
                    source.getParentSegment().getVirtualLength().getValueComponent() * relativeStartLocation
            );
            absoluteStartLocation.setUnitComponent(source.getParentSegment().getVirtualLength().getUnitComponent());
        }

        return absoluteStartLocation;
    }
}
