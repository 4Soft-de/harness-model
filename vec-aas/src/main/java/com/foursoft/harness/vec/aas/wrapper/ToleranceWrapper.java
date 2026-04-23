package com.foursoft.harness.vec.aas.wrapper;

import com.foursoft.harness.vec.aas.AasConversionException;

import java.util.Set;

public class ToleranceWrapper extends GenericVecObjectWrapper {

    private static final Set<String> TOLERANCE_TYPES = Set.of("VecTolerance");

    private ToleranceWrapper(final Object contextObject) {
        super(contextObject);
        if (!isToleranceType(contextObject.getClass())) {
            throw new AasConversionException("Can not wrap " + contextObject + " into a Tolerance.");
        }
    }

    public double getLowerBoundary() {
        return (double) getFieldValue("lowerBoundary");
    }

    public double getUpperBoundary() {
        return (double) getFieldValue("upperBoundary");
    }

    public static boolean isToleranceType(final Class<?> clazz) {
        return TOLERANCE_TYPES.contains(clazz.getSimpleName());
    }

    public static ToleranceWrapper wrap(final Object contextObject) {
        return new ToleranceWrapper(contextObject);
    }

}
