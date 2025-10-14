package com.foursoft.harness.kbl2vec.transform.harness;

import com.foursoft.harness.kbl.v25.KblContactPoint;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecContactPoint;

public class ContactPointTransformer implements Transformer<KblContactPoint, VecContactPoint> {

    @Override
    public TransformationResult<VecContactPoint> transform(final TransformationContext context,
                                                           final KblContactPoint source) {
        final VecContactPoint destination = new VecContactPoint();
        destination.setIdentification(source.getId());
        return TransformationResult.of(destination);
    }
}
