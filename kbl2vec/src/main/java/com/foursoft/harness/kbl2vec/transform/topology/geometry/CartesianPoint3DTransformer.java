package com.foursoft.harness.kbl2vec.transform.topology.geometry;

import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCartesianPoint3D;

public class CartesianPoint3DTransformer implements Transformer<KblCartesianPoint, VecCartesianPoint3D> {

    @Override
    public TransformationResult<VecCartesianPoint3D> transform(final TransformationContext context,
                                                               final KblCartesianPoint source) {
        final VecCartesianPoint3D destination = new VecCartesianPoint3D();
        destination.setX(source.getCoordinates().get(0));
        destination.setY(source.getCoordinates().get(1));
        destination.setZ(source.getCoordinates().get(2));

        return TransformationResult.of(destination);
    }
}
