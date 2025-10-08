package com.foursoft.harness.kbl2vec.transform.topology.routing;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblRouting;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecRouting;
import com.foursoft.harness.vec.v2x.VecRoutingSpecification;

import static com.foursoft.harness.kbl2vec.transform.Fragments.abbreviatedClassName;

public class RoutingSpecificationTransformer implements Transformer<KblHarness, VecRoutingSpecification> {

    @Override
    public TransformationResult<VecRoutingSpecification> transform(final TransformationContext context,
                                                                   final KblHarness source) {
        final VecRoutingSpecification destination = new VecRoutingSpecification();
        destination.setIdentification(abbreviatedClassName(destination.getClass()) + "-" + source.getPartNumber());

        return TransformationResult.from(destination)
                .withDownstream(KblRouting.class, VecRouting.class,
                                Query.fromLists(source.getParentKBLContainer().getRoutings()),
                                VecRoutingSpecification::getRoutings)
                .build();
    }
}
