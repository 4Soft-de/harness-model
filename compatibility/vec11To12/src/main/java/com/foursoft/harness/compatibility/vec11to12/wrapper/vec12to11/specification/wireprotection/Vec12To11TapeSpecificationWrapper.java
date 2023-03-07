package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.wireprotection;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.vec.v12x.VecNumericalValue;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecTapeSpecification}
 * to {@link com.foursoft.harness.vec.v113.VecTapeSpecification}.
 */
public class Vec12To11TapeSpecificationWrapper extends Vec12To11WireProtectionSpecificationWrapper {

    protected List<com.foursoft.harness.vec.v113.VecNumericalValue> widths;
    protected List<com.foursoft.harness.vec.v113.VecNumericalValue> thicknesses;
    protected List<com.foursoft.harness.vec.v113.VecNumericalValue> coilCoreDiameters;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11TapeSpecificationWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {

        switch (method.getName()) {
            case "getWidths":
                return getWidths();
            case "getThicknesses":
                return getThicknesses();
            case "getCoilCoreDiameters":
                return getCoilCoreDiameters();
            case "setWidths":
                return setWidths((List<com.foursoft.harness.vec.v113.VecNumericalValue>) allArguments[0]);
            case "setThicknesses":
                return setThicknesses((List<com.foursoft.harness.vec.v113.VecNumericalValue>) allArguments[0]);
            case "setCoilCoreDiameters":
                return setCoilCoreDiameters((List<com.foursoft.harness.vec.v113.VecNumericalValue>) allArguments[0]);
            default:
                return super.wrapObject(obj, method, allArguments);
        }

    }

    private Object setCoilCoreDiameters(final List<com.foursoft.harness.vec.v113.VecNumericalValue> allArgument) {
        coilCoreDiameters = allArgument;
        return null;
    }

    private Object setThicknesses(final List<com.foursoft.harness.vec.v113.VecNumericalValue> allArgument) {
        thicknesses = allArgument;
        return null;
    }

    private Object setWidths(final List<com.foursoft.harness.vec.v113.VecNumericalValue> allArgument) {
        widths = allArgument;
        return null;
    }

    private List<com.foursoft.harness.vec.v113.VecNumericalValue> getCoilCoreDiameters() {
        coilCoreDiameters = getValueList("getCoilCoreDiameter");
        return coilCoreDiameters;
    }

    private List<com.foursoft.harness.vec.v113.VecNumericalValue> getThicknesses() {
        thicknesses = getValueList("getThickness");
        return thicknesses;
    }

    private List<com.foursoft.harness.vec.v113.VecNumericalValue> getWidths() {
        widths = getValueList("getWidth");
        return widths;
    }

    private List<com.foursoft.harness.vec.v113.VecNumericalValue> getValueList(final String methodName) {

        final com.foursoft.harness.vec.v113.VecNumericalValue numericalValue = getResultObject(methodName,
                                                                                               VecNumericalValue.class)
                .map(getContext().getWrapperProxyFactory()::createProxy)
                .map(com.foursoft.harness.vec.v113.VecNumericalValue.class::cast)
                .orElse(null);

        return numericalValue == null ? null : List.of(numericalValue);
    }

}
