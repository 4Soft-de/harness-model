package com.foursoft.harness.kbl2vec.convert;

import com.foursoft.harness.vec.v2x.VecCartesianVector3D;

import java.util.List;
import java.util.Optional;

import static com.foursoft.harness.kbl2vec.utils.CoordinateGenerator.getCoordinateOrDefault;

public class DoublesToCartesianVector3DConverter implements Converter<List<Double>, Optional<VecCartesianVector3D>> {

    @Override
    public Optional<VecCartesianVector3D> convert(final List<Double> source) {
        if (source == null || source.isEmpty()) {
            return Optional.empty();
        }
        final VecCartesianVector3D destination = new VecCartesianVector3D();
        destination.setX(source.get(0));
        destination.setY(getCoordinateOrDefault(source, 1));
        destination.setZ(getCoordinateOrDefault(source, 2));
        return Optional.of(destination);
    }
}
