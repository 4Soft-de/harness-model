package com.foursoft.harness.kbl2vec.transform.topology.geometry.d2;

import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl2vec.convert.DoublesToCartesianVector2DConverter;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecGeometryNode2D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment2D;

public class GeometrySegment2DTransformer implements Transformer<KblSegment, VecGeometrySegment2D> {

    @Override
    public TransformationResult<VecGeometrySegment2D> transform(final TransformationContext context,
                                                                final KblSegment source) {
        final VecGeometrySegment2D destination = new VecGeometrySegment2D();

        final DoublesToCartesianVector2DConverter converter =
                context.getConverterRegistry().getDoublesToCartesianVector2DConverter();
        converter.convert(source.getStartVectors()).ifPresent(destination::setStartVector);
        converter.convert(source.getEndVectors()).ifPresent(destination::setEndVector);

        return TransformationResult.from(destination)
                .withLinker(Query.of(source.getStartNode()), VecGeometryNode2D.class,
                            VecGeometrySegment2D::setStartNode)
                .withLinker(Query.of(source.getEndNode()), VecGeometryNode2D.class, VecGeometrySegment2D::setEndNode)
                .build();
    }
}
