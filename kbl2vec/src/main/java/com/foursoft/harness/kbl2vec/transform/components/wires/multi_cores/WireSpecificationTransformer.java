package com.foursoft.harness.kbl2vec.transform.components.wires.multi_cores;

import com.foursoft.harness.kbl.v25.KblCore;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecPartOrUsageRelatedSpecification;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecWireElement;
import com.foursoft.harness.vec.v2x.VecWireSpecification;

public class WireSpecificationTransformer implements Transformer<KblCore, VecWireSpecification> {

    @Override
    public TransformationResult<VecWireSpecification> transform(final TransformationContext context,
                                                                final KblCore source) {
        final VecWireSpecification destination = new VecWireSpecification();
        destination.setIdentification(source.getId());

        return TransformationResult
                .from(destination)
                .withDownstream(KblCore.class, VecWireElement.class, Query.of(source),
                                VecWireSpecification::setWireElement)
                .withLinker(Query.of(source), VecPartVersion.class,
                            VecPartOrUsageRelatedSpecification::getDescribedPart)
                .build();
    }
}
