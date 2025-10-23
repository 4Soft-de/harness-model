package com.foursoft.harness.kbl2vec.transform.geometry.geo_2d;

import com.foursoft.harness.kbl.v25.KblTransformation;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.kbl2vec.transform.geometry.GeometryDimensionDetector;
import com.foursoft.harness.vec.v2x.VecCartesianPoint2D;
import com.foursoft.harness.vec.v2x.VecTransformation2D;

import java.util.List;

public class Transformation2DTransformer implements Transformer<KblTransformation, VecTransformation2D> {
    @Override
    public TransformationResult<VecTransformation2D> transform(final TransformationContext context,
                                                               final KblTransformation source) {
        final VecTransformation2D destination = new VecTransformation2D();

        final List<Double> u = source.getUS();
        final List<Double> v = source.getVS();

        if (!GeometryDimensionDetector.hasDimensions(u, GeometryDimensionDetector.GEO_2D)) {
            context.getLogger().warn(
                    "Unexpected format for U vector of KblTransformation (xmlId: {}). Expected 2 coordinates but " +
                            "found {}: {}.", source.getXmlId(), u.size(), u);
        }

        if (!GeometryDimensionDetector.hasDimensions(v, GeometryDimensionDetector.GEO_2D)) {
            context.getLogger().warn(
                    "Unexpected format for V vector of KblTransformation (xmlId: {}). Expected 2 coordinates but " +
                            "found {}: {}.", source.getXmlId(), v.size(), v);
        }

        destination.setA11(u.get(0));
        destination.setA12(u.get(1));
        destination.setA21(v.get(0));
        destination.setA22(v.get(1));

        return TransformationResult.from(destination)
                .withLinker(Query.of(source.getCartesianPoint()), VecCartesianPoint2D.class,
                            VecTransformation2D::setOrigin)
                .build();
    }
}
