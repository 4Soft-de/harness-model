package com.foursoft.harness.vec.aas.wrapper;

import com.foursoft.harness.vec.aas.AasConversionException;

import java.util.Set;

public class NumericalValueWrapper extends GenericVecObjectWrapper {

    private static final Set<String> NUMERICAL_VALUE_TYPES = Set.of("VecNumericalValue");

    private final UnitWrapper unitComponent;
    private final ToleranceWrapper tolerance;

    private NumericalValueWrapper(final Object contextObject) {
        super(contextObject);
        if (!isNumericalValueType(contextObject.getClass())) {
            throw new AasConversionException("Can not wrap " + contextObject + " into a NumericalValue.");
        }
        unitComponent = UnitWrapper.wrap(getFieldValue("unitComponent"));
        final Object toleranceObject = getFieldValue("tolerance");
        tolerance = toleranceObject == null ? null : ToleranceWrapper.wrap(toleranceObject);
    }

    public UnitWrapper getUnitComponent() {
        return unitComponent;
    }

    public ToleranceWrapper getTolerance() {
        return tolerance;
    }

    public double getValueComponent() {
        return (double) getFieldValue("valueComponent");
    }

    public static NumericalValueWrapper wrap(final Object contextObject) {
        return new NumericalValueWrapper(contextObject);
    }

    public static boolean isNumericalValueType(final Class<?> clazz) {
        return NUMERICAL_VALUE_TYPES.contains(clazz.getSimpleName());
    }

}
