package com.foursoft.harness.kbl2vec.transform.components.wires;

import com.foursoft.harness.kbl.v25.KblExtremity;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecWireEnd;

public class WireEndTransformer implements Transformer<KblExtremity, VecWireEnd> {

    @Override
    public TransformationResult<VecWireEnd> transform(final TransformationContext context, final KblExtremity source) {
        final VecWireEnd destination = new VecWireEnd();
        destination.setPositionOnWire(source.getPositionOnWire());

        return TransformationResult.of(destination);
    }
}
