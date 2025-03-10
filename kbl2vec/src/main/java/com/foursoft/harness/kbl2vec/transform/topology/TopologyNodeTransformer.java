package com.foursoft.harness.kbl2vec.transform.topology;

import com.foursoft.harness.kbl.v25.KblAliasIdentification;
import com.foursoft.harness.kbl.v25.KblNode;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecAliasIdentification;
import com.foursoft.harness.vec.v2x.VecTopologyNode;

public class TopologyNodeTransformer implements Transformer<KblNode, VecTopologyNode> {
    @Override
    public TransformationResult<VecTopologyNode> transform(final TransformationContext context, final KblNode source) {
        final VecTopologyNode topologyNode = new VecTopologyNode();
        topologyNode.setIdentification(source.getId());

        return TransformationResult.from(topologyNode)
                .downstreamTransformation(KblAliasIdentification.class, VecAliasIdentification.class,
                                          source::getAliasIds, topologyNode::getAliasIds)
                .build();
    }
}
