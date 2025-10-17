package com.foursoft.harness.kbl2vec.transform.components.wires.multi_cores;

import com.foursoft.harness.kbl.v25.KblCore;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecWireElement;
import com.foursoft.harness.vec.v2x.VecWireElementSpecification;

public class WireElementTransformer implements Transformer<KblCore, VecWireElement> {

    @Override
    public TransformationResult<VecWireElement> transform(final TransformationContext context,
                                                          final KblCore source) {
        final VecWireElement destination = new VecWireElement();
        destination.setIdentification(source.getId());

        return TransformationResult.from(destination)
                .withLinker(Query.of(source), VecWireElementSpecification.class,
                            VecWireElement::setWireElementSpecification)
                .build();
    }
}