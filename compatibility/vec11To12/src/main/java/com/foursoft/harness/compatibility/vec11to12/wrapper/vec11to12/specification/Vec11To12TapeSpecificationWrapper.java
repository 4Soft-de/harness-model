package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.vec.v12x.VecNumericalValue;

import java.lang.reflect.Method;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecTapeSpecification}
 * to {@link com.foursoft.harness.vec.v12x.VecTapeSpecification}.
 */
public class Vec11To12TapeSpecificationWrapper extends ReflectionBasedWrapper {

    private VecNumericalValue width;
    private VecNumericalValue thickness;
    private VecNumericalValue coilCoreDiameter;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12TapeSpecificationWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        switch (method.getName()) {
            case "getWidth":
                return getWidth();
            case "getThickness":
                return getThickness();
            case "getCoilCoreDiameter":
                return getCoilCoreDiameter();
            case "setWidth":
                return setWidth((VecNumericalValue) allArguments[0]);
            case "setThickness":
                return setThickness((VecNumericalValue) allArguments[0]);
            case "setCoilCoreDiameter":
                return setCoilCoreDiameter((VecNumericalValue) allArguments[0]);
            default:
                return super.wrapObject(obj, method, allArguments);
        }
    }

    private Object setCoilCoreDiameter(final VecNumericalValue arg) {
        coilCoreDiameter = arg;
        return null;
    }

    private Object setThickness(final VecNumericalValue arg) {
        thickness = arg;
        return null;
    }

    private Object setWidth(final VecNumericalValue arg) {
        width = arg;
        return null;
    }

    private VecNumericalValue getCoilCoreDiameter() {
        coilCoreDiameter = wrapListToSingleElementIfNull(coilCoreDiameter, "getCoilCoreDiameters");
        return coilCoreDiameter;
    }

    private VecNumericalValue getThickness() {
        thickness = wrapListToSingleElementIfNull(thickness, "getThicknesses");
        return thickness;
    }

    private VecNumericalValue getWidth() {
        width = wrapListToSingleElementIfNull(width, "getWidths");
        return width;
    }

}
