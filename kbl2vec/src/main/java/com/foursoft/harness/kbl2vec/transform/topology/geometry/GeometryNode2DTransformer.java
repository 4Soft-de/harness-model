package com.foursoft.harness.kbl2vec.transform.topology.geometry;

import com.foursoft.harness.kbl.v25.KblNode;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCartesianPoint2D;
import com.foursoft.harness.vec.v2x.VecGeometryNode2D;

public class GeometryNode2DTransformer implements Transformer<KblNode, VecGeometryNode2D> {

    @Override
    public TransformationResult<VecGeometryNode2D> transform(final TransformationContext context,
                                                             final KblNode source) {
        final VecGeometryNode2D destination = new VecGeometryNode2D();

        return TransformationResult.from(destination)
                .withLinker(Query.of(source.getCartesianPoint()), VecCartesianPoint2D.class,
                            VecGeometryNode2D::setCartesianPoint)
                .build();
    }
}
