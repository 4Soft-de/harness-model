package com.foursoft.harness.kbl2vec.transform.topology.geometry.d3;

import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCartesianPoint3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.foursoft.harness.kbl2vec.utils.CoordinateGenerator.getCoordinateOrDefault;

public class CartesianPoint3DTransformer implements Transformer<KblCartesianPoint, VecCartesianPoint3D> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartesianPoint3DTransformer.class);
    private static final byte DIMENSIONS = 3;

    @Override
    public TransformationResult<VecCartesianPoint3D> transform(final TransformationContext context,
                                                               final KblCartesianPoint source) {
        if (source.getCoordinates().size() != DIMENSIONS) {
            LOGGER.warn("Wrong number of coordinates provided for the transformation. Expected {} but found {} ",
                        DIMENSIONS, source.getCoordinates().size());
        }

        if (source.getCoordinates().isEmpty()) {
            return TransformationResult.noResult();
        }

        final List<Double> coordinates = source.getCoordinates();
        final VecCartesianPoint3D destination = new VecCartesianPoint3D();

        destination.setX(source.getCoordinates().get(0));
        destination.setY(getCoordinateOrDefault(coordinates, 1));
        destination.setZ(getCoordinateOrDefault(coordinates, 2));

        return TransformationResult.of(destination);
    }
}
