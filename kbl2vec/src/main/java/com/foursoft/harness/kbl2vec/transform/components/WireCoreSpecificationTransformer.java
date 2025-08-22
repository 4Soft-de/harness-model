package com.foursoft.harness.kbl2vec.transform.components;

import com.foursoft.harness.kbl.v25.KblGeneralWire;
import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCoreSpecification;
import com.foursoft.harness.vec.v2x.VecNumericalValue;

public class WireCoreSpecificationTransformer implements Transformer<KblGeneralWire, VecCoreSpecification> {

    @Override
    public TransformationResult<VecCoreSpecification> transform(final TransformationContext context,
                                                                final KblGeneralWire source) {
        final VecCoreSpecification dest = new VecCoreSpecification();
        dest.setIdentification("WIRE-CORE");
        return TransformationResult.from(dest)
                .withDownstream(KblNumericalValue.class, VecNumericalValue.class,
                                Query.of(source::getCrossSectionArea), VecCoreSpecification::setCrossSectionArea)
                .build();
    }
}
