package com.foursoft.harness.kbl2vec.transform.components.wires.multi_cores;

import com.foursoft.harness.kbl.v25.KblCore;
import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCoreSpecification;
import com.foursoft.harness.vec.v2x.VecNumericalValue;

public class CoreSpecification implements Transformer<KblCore, VecCoreSpecification> {

    @Override
    public TransformationResult<VecCoreSpecification> transform(final TransformationContext context,
                                                                final KblCore source) {
        final VecCoreSpecification destination = new VecCoreSpecification();
        destination.setIdentification(source.getId());

        return TransformationResult.from(destination)
                .withDownstream(KblNumericalValue.class, VecNumericalValue.class,
                                Query.of(source::getCrossSectionArea), VecCoreSpecification::setCrossSectionArea)
                .build();
    }
}
