package com.foursoft.harness.vec.scripting.topology;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.v2x.VecTopologyNode;
import com.foursoft.harness.vec.v2x.VecTopologySegment;

public class TopologySegmentBuilder implements Builder<VecTopologySegment> {

    private final VecTopologySegment segment = new VecTopologySegment();

    TopologySegmentBuilder(String identification, VecTopologyNode startNode, VecTopologyNode endNode) {
        segment.setIdentification(identification);
        segment.setStartNode(startNode);
        segment.setEndNode(endNode);
    }

    @Override public VecTopologySegment build() {
        return segment;
    }
}
