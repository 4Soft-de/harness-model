package com.foursoft.harness.kbl2vec.transform.geometry.geo_3d;

import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCartesianPoint3D;
import com.foursoft.harness.vec.v2x.VecNURBSControlPoint;

public class NurbsControlPointTransformer implements Transformer<KblCartesianPoint, VecNURBSControlPoint> {

    @Override
    public TransformationResult<VecNURBSControlPoint> transform(final TransformationContext context,
                                                                final KblCartesianPoint source) {
        final VecNURBSControlPoint destination = new VecNURBSControlPoint();

        return TransformationResult.from(destination)
                .withLinker(Query.of(source), VecCartesianPoint3D.class, VecNURBSControlPoint::setCartesianPoint3D)
                .build();
    }
}
