package com.foursoft.harness.kbl2vec.transform.components;

import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl.v25.KblWireLength;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecWireLength;

public class WireLengthTransformer implements Transformer<KblWireLength, VecWireLength> {
    @Override
    public TransformationResult<VecWireLength> transform(final TransformationContext context,
                                                         final KblWireLength source) {
        final VecWireLength dest = new VecWireLength();
        dest.setLengthType(source.getLengthType());

        return TransformationResult.from(dest)
                .withDownstream(KblNumericalValue.class, VecNumericalValue.class, Query.of(source::getLengthValue),
                                VecWireLength::setLengthValue)
                .build();
    }
}
