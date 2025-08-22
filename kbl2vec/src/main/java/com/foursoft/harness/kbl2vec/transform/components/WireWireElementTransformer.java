package com.foursoft.harness.kbl2vec.transform.components;

import com.foursoft.harness.kbl.v25.KblGeneralWire;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecWireElement;
import com.foursoft.harness.vec.v2x.VecWireElementSpecification;

public class WireWireElementTransformer implements Transformer<KblGeneralWire, VecWireElement> {
    @Override
    public TransformationResult<VecWireElement> transform(final TransformationContext context,
                                                          final KblGeneralWire source) {
        final VecWireElement dest = new VecWireElement();
        dest.setIdentification("WIRE");
        //TODO: Cores!
        return TransformationResult.from(dest)
                .withLinker(Query.of(source), VecWireElementSpecification.class,
                            VecWireElement::setWireElementSpecification)
                .build();
    }
}
