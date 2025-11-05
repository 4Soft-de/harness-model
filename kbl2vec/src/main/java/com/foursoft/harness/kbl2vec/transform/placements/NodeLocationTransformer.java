package com.foursoft.harness.kbl2vec.transform.placements;

import com.foursoft.harness.kbl.v25.LocatedComponent;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecNodeLocation;
import com.foursoft.harness.vec.v2x.VecTopologyNode;

public class NodeLocationTransformer implements Transformer<LocatedComponent, VecNodeLocation> {

    @Override
    public TransformationResult<VecNodeLocation> transform(final TransformationContext context,
                                                           final LocatedComponent source) {
        final VecNodeLocation destination = new VecNodeLocation();
        destination.setIdentification("NODE_LOCATION");

        return TransformationResult.from(destination)
                .withLinker(Query.fromLists(source.getRefNode().stream().toList()), VecTopologyNode.class,
                            VecNodeLocation::setReferencedNode)
                .build();
    }
}
