package com.foursoft.harness.kbl2vec.transform.topology.geometry;

import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCartesianVector2D;

import java.util.List;

public class CartesianVector2DTransformer implements Transformer<List, VecCartesianVector2D> {
    @Override
    public TransformationResult<VecCartesianVector2D> transform(final TransformationContext context,
                                                                final List source) {
        final VecCartesianVector2D destination = new VecCartesianVector2D();

        if (!source.isEmpty()) {
            destination.setX((Double) source.get(0));
        }

        if (source.size() > 1) {
            destination.setY((Double) source.get(1));
        }

        return TransformationResult.of(destination);

    }
}
