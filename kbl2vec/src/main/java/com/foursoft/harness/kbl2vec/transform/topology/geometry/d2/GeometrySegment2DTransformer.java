package com.foursoft.harness.kbl2vec.transform.topology.geometry.d2;

import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl2vec.convert.DoublesToCartesianVector2DConverter;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecGeometryNode2D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeometrySegment2DTransformer implements Transformer<KblSegment, VecGeometrySegment2D> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeometrySegment2DTransformer.class);
    private static final byte DIMENSIONS = 2;

    @Override
    public TransformationResult<VecGeometrySegment2D> transform(final TransformationContext context,
                                                                final KblSegment source) {
        final VecGeometrySegment2D destination = new VecGeometrySegment2D();

        if (source.getStartVectors().size() != DIMENSIONS) {
            LOGGER.warn(
                    "Wrong number of coordinates provided for the transformation of start vectors. Expected {} but " +
                            "found {} ",
                    DIMENSIONS, source.getStartVectors().size());
        }

        if (source.getEndVectors().size() != DIMENSIONS) {
            LOGGER.warn(
                    "Wrong number of coordinates provided for the transformation of end vectors. Expected {} but " +
                            "found {} ",
                    DIMENSIONS, source.getStartVectors().size());
        }

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
