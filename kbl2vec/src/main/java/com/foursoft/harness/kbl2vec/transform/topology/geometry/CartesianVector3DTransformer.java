package com.foursoft.harness.kbl2vec.transform.topology.geometry;

import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCartesianVector3D;

import java.util.List;

public class CartesianVector3DTransformer implements Transformer<List, VecCartesianVector3D> {
    @Override
    public TransformationResult<VecCartesianVector3D> transform(final TransformationContext context,
                                                                final List source) {
        final VecCartesianVector3D destination = new VecCartesianVector3D();

        if (!source.isEmpty()) {
            destination.setX((Double) source.get(0));
        }

        if (source.size() > 1) {
            destination.setY((Double) source.get(1));
        }
        if (source.size() > 2) {
            destination.setZ((Double) source.get(2));
        }

        return TransformationResult.of(destination);
    }
}
