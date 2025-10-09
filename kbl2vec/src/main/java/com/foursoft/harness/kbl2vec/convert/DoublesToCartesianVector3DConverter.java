package com.foursoft.harness.kbl2vec.convert;

import com.foursoft.harness.vec.v2x.VecCartesianVector3D;

import java.util.List;
import java.util.Optional;

public class DoublesToCartesianVector3DConverter implements Converter<List<Double>, Optional<VecCartesianVector3D>> {
    @Override
    public Optional<VecCartesianVector3D> convert(final List<Double> source) {
        if (source == null || source.isEmpty()) {
            return Optional.empty();
        }
        final VecCartesianVector3D destination = new VecCartesianVector3D();
        destination.setX(source.get(0));
        if (source.size() > 1) {
            destination.setY(source.get(1));
        }
        if (source.size() > 2) {
            destination.setZ(source.get(2));
        }
        return Optional.of(destination);
    }
}
