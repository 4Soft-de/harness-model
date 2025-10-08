package com.foursoft.harness.kbl2vec.transform.topology.routing;

import com.foursoft.harness.kbl.v25.KblRouting;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecPath;
import com.foursoft.harness.vec.v2x.VecTopologySegment;

public class PathTransformer implements Transformer<KblRouting, VecPath> {

    @Override
    public TransformationResult<VecPath> transform(final TransformationContext context, final KblRouting source) {
        final VecPath destination = new VecPath();

        return TransformationResult.from(destination)
                .withLinker(Query.fromLists(source.getParentKBLContainer().getSegments()), VecTopologySegment.class,
                            VecPath::getSegment)
                .build();
    }
}
