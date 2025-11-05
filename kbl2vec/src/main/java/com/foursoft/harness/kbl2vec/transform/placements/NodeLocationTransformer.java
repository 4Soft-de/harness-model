package com.foursoft.harness.kbl2vec.transform.placements;

import com.foursoft.harness.kbl.v25.KblNode;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecNodeLocation;
import com.foursoft.harness.vec.v2x.VecTopologyNode;

public class NodeLocationTransformer implements Transformer<KblNode, VecNodeLocation> {

    @Override
    public TransformationResult<VecNodeLocation> transform(final TransformationContext context,
                                                           final KblNode source) {
        final VecNodeLocation destination = new VecNodeLocation();
        destination.setIdentification(source.getId());

        return TransformationResult.from(destination)
                .withLinker(Query.of(source), VecTopologyNode.class, VecNodeLocation::setReferencedNode)
                .build();
    }
}
