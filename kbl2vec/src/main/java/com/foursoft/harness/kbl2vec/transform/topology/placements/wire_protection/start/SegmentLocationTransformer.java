package com.foursoft.harness.kbl2vec.transform.topology.placements.wire_protection.start;

import com.foursoft.harness.kbl.v25.KblProtectionArea;
import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl.v25.KblUnit;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.kbl2vec.transform.topology.placements.AbstractSegmentLocationTransformer;
import com.foursoft.harness.vec.v2x.VecSegmentLocation;

public class SegmentLocationTransformer extends AbstractSegmentLocationTransformer<KblProtectionArea>
        implements Transformer<KblProtectionArea, VecSegmentLocation> {

    @Override
    protected LocationData extractLocationData(final KblProtectionArea source) {
        return new LocationData(source.getStartLocation(), source.getAbsoluteStartLocation(), extractUnit(source),
                                "START");
    }

    private KblUnit extractUnit(final KblProtectionArea source) {
        final KblSegment parentSegment = source.getParentSegment();

        if (source.getAbsoluteStartLocation() != null) {
            return source.getAbsoluteStartLocation().getUnitComponent();
        }

        if (parentSegment.getPhysicalLength() != null) {
            return parentSegment.getPhysicalLength().getUnitComponent();
        }

        if (parentSegment.getVirtualLength() != null) {
            return parentSegment.getVirtualLength().getUnitComponent();
        }

        return new KblUnit();
    }
}
