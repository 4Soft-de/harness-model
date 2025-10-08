package com.foursoft.harness.kbl2vec.transform.topology.geometry;

import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCartesianVector3D;
import com.foursoft.harness.vec.v2x.VecGeometryNode3D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment3D;

import java.util.List;

public class GeometrySegment3DTransformer implements Transformer<KblSegment, VecGeometrySegment3D> {

    @Override
    public TransformationResult<VecGeometrySegment3D> transform(final TransformationContext context,
                                                                final KblSegment source) {
        final VecGeometrySegment3D destination = new VecGeometrySegment3D();

        return TransformationResult.from(destination)
                .withDownstream(List.class, VecCartesianVector3D.class, Query.of(source.getStartVectors()),
                                VecGeometrySegment3D::setStartVector)
                .withDownstream(List.class, VecCartesianVector3D.class, Query.of(source.getEndVectors()),
                                VecGeometrySegment3D::setEndVector)
                .withLinker(Query.of(source.getStartNode()), VecGeometryNode3D.class,
                            VecGeometrySegment3D::setStartNode)
                .withLinker(Query.of(source.getEndNode()), VecGeometryNode3D.class, VecGeometrySegment3D::setEndNode)
                .build();
    }
}
