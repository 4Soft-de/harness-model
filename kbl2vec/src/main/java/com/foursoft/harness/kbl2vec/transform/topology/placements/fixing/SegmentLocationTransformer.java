package com.foursoft.harness.kbl2vec.transform.topology.placements.fixing;

import com.foursoft.harness.kbl.v25.KblFixingAssignment;
import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecSegmentLocation;
import com.foursoft.harness.vec.v2x.VecTopologySegment;

public class SegmentLocationTransformer implements Transformer<KblFixingAssignment, VecSegmentLocation> {

    @Override
    public TransformationResult<VecSegmentLocation> transform(final TransformationContext context,
                                                              final KblFixingAssignment source) {
        final VecSegmentLocation destination = new VecSegmentLocation();
        destination.setIdentification(source.getId());

        final TransformationResult.Builder<VecSegmentLocation> builder = TransformationResult.from(destination)
                .withLinker(Query.of(source::getParentSegment), VecTopologySegment.class,
                            VecSegmentLocation::setReferencedSegment);

        if (source.getAbsoluteLocation() != null) {
            return builder
                    .withDownstream(KblNumericalValue.class, VecNumericalValue.class,
                                    Query.of(source.getAbsoluteLocation()), VecSegmentLocation::setOffset)
                    .build();

        }

        final VecNumericalValue location = calculateAbsoluteLocation(source);
        destination.setOffset(location);
        return builder.build();
    }

    private static VecNumericalValue calculateAbsoluteLocation(final KblFixingAssignment source) {
        double absoluteLength = 0;
        if (!Double.isNaN(source.getLocation())) {
            if (source.getParentSegment().getPhysicalLength() != null) {
                absoluteLength =
                        source.getParentSegment().getPhysicalLength().getValueComponent() * source.getLocation();
            } else if (source.getParentSegment().getVirtualLength() != null) {
                absoluteLength =
                        source.getParentSegment().getVirtualLength().getValueComponent() * source.getLocation();
            }
        }
        final VecNumericalValue location = new VecNumericalValue();
        location.setValueComponent(absoluteLength);
        return location;
    }
}
