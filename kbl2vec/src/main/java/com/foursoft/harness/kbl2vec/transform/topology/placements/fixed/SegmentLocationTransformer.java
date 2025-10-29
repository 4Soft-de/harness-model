package com.foursoft.harness.kbl2vec.transform.topology.placements.fixed;

import com.foursoft.harness.kbl.v25.KblFixingAssignment;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.kbl2vec.transform.topology.placements.AbstractSegmentLocationTransformer;
import com.foursoft.harness.vec.v2x.VecSegmentLocation;

public class SegmentLocationTransformer extends AbstractSegmentLocationTransformer<KblFixingAssignment>
        implements Transformer<KblFixingAssignment, VecSegmentLocation> {

    @Override
    protected LocationData extractLocationData(final KblFixingAssignment source) {
        return new LocationData(source.getLocation(), source.getAbsoluteLocation(), "FIXING");
    }
}
