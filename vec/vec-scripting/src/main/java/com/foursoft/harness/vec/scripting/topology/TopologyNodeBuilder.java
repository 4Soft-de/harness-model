package com.foursoft.harness.vec.scripting.topology;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.v2x.VecTopologyNode;

public class TopologyNodeBuilder implements Builder<VecTopologyNode> {

    private final VecTopologyNode topologyNode = new VecTopologyNode();

    TopologyNodeBuilder(String identification) {
        topologyNode.setIdentification(identification);
    }

    @Override public VecTopologyNode build() {
        return topologyNode;
    }
}
