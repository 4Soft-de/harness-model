package com.foursoft.harness.kbl2vec.transform.topology.geometry.d3;

import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl2vec.convert.DoublesToCartesianVector3DConverter;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecGeometryNode3D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeometrySegment3DTransformer implements Transformer<KblSegment, VecGeometrySegment3D> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeometrySegment3DTransformer.class);
    private static final byte DIMENSIONS = 3;

    @Override
    public TransformationResult<VecGeometrySegment3D> transform(final TransformationContext context,
                                                                final KblSegment source) {
        final VecGeometrySegment3D destination = new VecGeometrySegment3D();

        if (source.getStartVectors().size() != DIMENSIONS) {
            LOGGER.warn(
                    "Wrong number of coordinates provided for start vectors. Expected {} but found {} ",
                    DIMENSIONS, source.getStartVectors().size());
        }

        if (source.getEndVectors().size() != DIMENSIONS) {
            LOGGER.warn(
                    "Wrong number of coordinates provided for end vectors. Expected {} but found {} ",
                    DIMENSIONS, source.getEndVectors().size());
        }

        final DoublesToCartesianVector3DConverter converter =
                context.getConverterRegistry().getDoublesToCartesianVector3DConverter();
        converter.convert(source.getStartVectors()).ifPresent(destination::setStartVector);
        converter.convert(source.getEndVectors()).ifPresent(destination::setEndVector);

        return TransformationResult.from(destination)
                .withLinker(Query.of(source.getStartNode()), VecGeometryNode3D.class,
                            VecGeometrySegment3D::setStartNode)
                .withLinker(Query.of(source.getEndNode()), VecGeometryNode3D.class, VecGeometrySegment3D::setEndNode)
                .build();
    }
}
