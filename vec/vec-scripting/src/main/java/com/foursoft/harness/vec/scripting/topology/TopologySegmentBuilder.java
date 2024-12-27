package com.foursoft.harness.vec.scripting.topology;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.Locator;
import com.foursoft.harness.vec.scripting.variants.ConfigurableElementBuilder;
import com.foursoft.harness.vec.v2x.VecConfigurationConstraint;
import com.foursoft.harness.vec.v2x.VecTopologyNode;
import com.foursoft.harness.vec.v2x.VecTopologySegment;

public class TopologySegmentBuilder extends ConfigurableElementBuilder<TopologySegmentBuilder, VecTopologySegment>
        implements Builder<VecTopologySegment> {

    private final VecTopologySegment segment = new VecTopologySegment();

    TopologySegmentBuilder(Locator<VecConfigurationConstraint> configurationConstraintLocator, String identification,
                           VecTopologyNode startNode, VecTopologyNode endNode) {
        super(configurationConstraintLocator);
        segment.setIdentification(identification);
        segment.setStartNode(startNode);
        segment.setEndNode(endNode);
    }

    @Override
    public VecTopologySegment build() {
        return segment;
    }

    @Override
    protected VecTopologySegment element() {
        return segment;
    }
}
