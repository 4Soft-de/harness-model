package com.foursoft.harness.kbl2vec.transform.topology.routing;

import com.foursoft.harness.kbl.v25.KblRouting;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecPath;
import com.foursoft.harness.vec.v2x.VecRouting;
import com.foursoft.harness.vec.v2x.VecTopologySegment;
import com.foursoft.harness.vec.v2x.VecWireElementReference;

public class RoutingTransformer implements Transformer<KblRouting, VecRouting> {

    @Override
    public TransformationResult<VecRouting> transform(final TransformationContext context, final KblRouting source) {
        final VecRouting destination = new VecRouting();
        destination.setIdentification(source.getId());

        return TransformationResult.from(destination)
                .withDownstream(KblRouting.class, VecPath.class, Query.of(source), VecRouting::setPath)
                .withLinker(Query.fromLists(source.getParentKBLContainer().getSegments()), VecTopologySegment.class,
                            VecRouting::getMandatorySegment)
                .withLinker(Query.of(source.getRoutedWire().getWire()), VecWireElementReference.class,
                            VecRouting::setRoutedElement)
                .build();
    }
}
