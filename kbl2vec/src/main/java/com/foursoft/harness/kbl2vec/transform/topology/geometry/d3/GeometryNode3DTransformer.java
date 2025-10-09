package com.foursoft.harness.kbl2vec.transform.topology.geometry.d3;

import com.foursoft.harness.kbl.v25.KblNode;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCartesianPoint3D;
import com.foursoft.harness.vec.v2x.VecGeometryNode3D;

public class GeometryNode3DTransformer implements Transformer<KblNode, VecGeometryNode3D> {

    @Override
    public TransformationResult<VecGeometryNode3D> transform(final TransformationContext context,
                                                             final KblNode source) {
        final VecGeometryNode3D destination = new VecGeometryNode3D();

        return TransformationResult.from(destination)
                .withLinker(Query.of(source.getCartesianPoint()), VecCartesianPoint3D.class,
                            VecGeometryNode3D::setCartesianPoint)
                .build();
    }
}
