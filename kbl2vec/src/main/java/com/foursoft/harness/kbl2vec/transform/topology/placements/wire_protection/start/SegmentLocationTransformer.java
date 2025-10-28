package com.foursoft.harness.kbl2vec.transform.topology.placements.wire_protection.start;

import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl.v25.KblProtectionArea;
import com.foursoft.harness.kbl.v25.KblUnit;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.kbl2vec.transform.topology.placements.AbstractSegmentLocationTransformer;
import com.foursoft.harness.vec.v2x.VecSegmentLocation;

public class SegmentLocationTransformer extends AbstractSegmentLocationTransformer<KblProtectionArea>
        implements Transformer<KblProtectionArea, VecSegmentLocation> {

    @Override
    public TransformationResult<VecSegmentLocation> transform(final TransformationContext context,
                                                              final KblProtectionArea source) {
        return super.transform(context, source);
    }

    @Override
    protected LocationData extractLocationData(final KblProtectionArea source) {
        final KblNumericalValue absoluteStartLocation = source.getAbsoluteStartLocation();
        final double relativeStartLocation = source.getStartLocation();
        final KblUnit baseUnit;

        if (source.getParentSegment().getPhysicalLength() != null) {
            baseUnit = source.getParentSegment().getPhysicalLength().getUnitComponent();
        } else if (source.getParentSegment().getVirtualLength() != null) {
            baseUnit = source.getParentSegment().getVirtualLength().getUnitComponent();
        } else {
            baseUnit = new KblUnit();
        }
        return new LocationData(relativeStartLocation, absoluteStartLocation, baseUnit, "START");
    }
}
