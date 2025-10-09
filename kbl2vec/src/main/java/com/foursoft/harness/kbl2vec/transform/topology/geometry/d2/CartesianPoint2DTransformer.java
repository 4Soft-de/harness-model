package com.foursoft.harness.kbl2vec.transform.topology.geometry.d2;

import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCartesianPoint2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.foursoft.harness.kbl2vec.utils.CoordinateGenerator.getCoordinateOrDefault;

public class CartesianPoint2DTransformer implements Transformer<KblCartesianPoint, VecCartesianPoint2D> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartesianPoint2DTransformer.class);
    private static final byte DIMENSIONS = 2;

    @Override
    public TransformationResult<VecCartesianPoint2D> transform(final TransformationContext context,
                                                               final KblCartesianPoint source) {
        if (source.getCoordinates().size() != DIMENSIONS) {
            LOGGER.warn("Wrong number of coordinates provided for the transformation. Expected {} but found {} ",
                        DIMENSIONS, source.getCoordinates().size());
        }

        if (source.getCoordinates().isEmpty()) {
            return TransformationResult.noResult();
        }

        final VecCartesianPoint2D destination = new VecCartesianPoint2D();
        destination.setX(source.getCoordinates().get(0));
        destination.setY(getCoordinateOrDefault(source.getCoordinates(), 1));

        return TransformationResult.of(destination);
    }
}
