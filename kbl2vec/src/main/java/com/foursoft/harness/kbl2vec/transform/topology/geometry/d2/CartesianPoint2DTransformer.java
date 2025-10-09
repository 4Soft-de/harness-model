package com.foursoft.harness.kbl2vec.transform.topology.geometry.d2;

import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCartesianPoint2D;

public class CartesianPoint2DTransformer implements Transformer<KblCartesianPoint, VecCartesianPoint2D> {

    @Override
    public TransformationResult<VecCartesianPoint2D> transform(final TransformationContext context,
                                                               final KblCartesianPoint source) {
        final VecCartesianPoint2D destination = new VecCartesianPoint2D();
        destination.setX(source.getCoordinates().get(0));
        destination.setY(source.getCoordinates().get(1));

        return TransformationResult.of(destination);
    }
}
