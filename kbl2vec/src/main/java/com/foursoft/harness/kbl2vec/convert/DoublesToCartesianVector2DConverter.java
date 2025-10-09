package com.foursoft.harness.kbl2vec.convert;

import com.foursoft.harness.vec.v2x.VecCartesianVector2D;

import java.util.List;
import java.util.Optional;

import static com.foursoft.harness.kbl2vec.utils.CoordinateGenerator.getCoordinateOrDefault;

public class DoublesToCartesianVector2DConverter implements Converter<List<Double>, Optional<VecCartesianVector2D>> {

    @Override
    public Optional<VecCartesianVector2D> convert(final List<Double> source) {
        if (source == null || source.isEmpty()) {
            return Optional.empty();
        }
        final VecCartesianVector2D destination = new VecCartesianVector2D();
        destination.setX(source.get(0));
        destination.setY(getCoordinateOrDefault(source, 1));
        return Optional.of(destination);
    }
}
