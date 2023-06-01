package com.foursoft.harness.vec.scripting.factories;

import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecTolerance;
import com.foursoft.harness.vec.v2x.VecUnit;

public final class NumericalValueFactory {

    private NumericalValueFactory() {
        throw new AssertionError();
    }

    public static VecNumericalValue valueWithTolerance(final double value, final double lowerTolerance,
                                                       final double upperTolerance, VecUnit unit) {
        VecNumericalValue numericalValue = value(value, unit);
        VecTolerance tolerance = new VecTolerance();
        tolerance.setLowerBoundary(lowerTolerance);
        tolerance.setUpperBoundary(upperTolerance);

        numericalValue.setTolerance(tolerance);
        return numericalValue;
    }

    public static VecNumericalValue value(final double value, VecUnit unit) {
        VecNumericalValue numericalValue = new VecNumericalValue();
        numericalValue.setValueComponent(value);
        numericalValue.setUnitComponent(unit);
        return numericalValue;
    }

}
