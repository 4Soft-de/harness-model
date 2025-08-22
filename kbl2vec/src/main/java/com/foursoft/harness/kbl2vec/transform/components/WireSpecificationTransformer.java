package com.foursoft.harness.kbl2vec.transform.components;

import com.foursoft.harness.kbl.v25.KblGeneralWire;
import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.kbl2vec.transform.Fragments;
import com.foursoft.harness.vec.v2x.VecWireElement;
import com.foursoft.harness.vec.v2x.VecWireSpecification;

public class WireSpecificationTransformer implements Transformer<KblPart, VecWireSpecification> {
    @Override
    public TransformationResult<VecWireSpecification> transform(final TransformationContext context,
                                                                final KblPart source) {
        if (source instanceof final KblGeneralWire sourceWire) {
            final VecWireSpecification dest = new VecWireSpecification();

            return TransformationResult
                    .from(dest)
                    .withFragment(Fragments.commonSpecificationAttributes(source))
                    .withDownstream(KblGeneralWire.class, VecWireElement.class, Query.of(sourceWire),
                                    VecWireSpecification::setWireElement)
                    .build();
        }

        return TransformationResult.noResult();
    }
}
