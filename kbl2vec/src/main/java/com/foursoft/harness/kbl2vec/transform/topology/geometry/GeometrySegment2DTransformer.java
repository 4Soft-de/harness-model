package com.foursoft.harness.kbl2vec.transform.topology.geometry;

import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCartesianVector2D;
import com.foursoft.harness.vec.v2x.VecGeometryNode2D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment2D;

import java.util.List;

public class GeometrySegment2DTransformer implements Transformer<KblSegment, VecGeometrySegment2D> {

    @Override
    public TransformationResult<VecGeometrySegment2D> transform(final TransformationContext context,
                                                                final KblSegment source) {
        final VecGeometrySegment2D destination = new VecGeometrySegment2D();

        return TransformationResult.from(destination)
                .withDownstream(List.class, VecCartesianVector2D.class, Query.of(source.getStartVectors()),
                                VecGeometrySegment2D::setStartVector)
                .withDownstream(List.class, VecCartesianVector2D.class, Query.of(source.getEndVectors()),
                                VecGeometrySegment2D::setEndVector)
                .withLinker(Query.of(source.getStartNode()), VecGeometryNode2D.class,
                            VecGeometrySegment2D::setStartNode)
                .withLinker(Query.of(source.getEndNode()), VecGeometryNode2D.class, VecGeometrySegment2D::setEndNode)
                .build();
    }
}
