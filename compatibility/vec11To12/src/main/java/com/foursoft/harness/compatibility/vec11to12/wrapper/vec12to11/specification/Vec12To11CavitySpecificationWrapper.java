package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.field.Vec12To11CavityDiameterWrapper;
import com.foursoft.harness.vec.v113.VecNumericalValue;

import java.lang.reflect.Method;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecCavitySpecification}
 * to {@link com.foursoft.harness.vec.v113.VecCavitySpecification}.
 */
public class Vec12To11CavitySpecificationWrapper extends ReflectionBasedWrapper {

    private final Vec12To11CavityDiameterWrapper cavityDiameterWrapper;
    private VecNumericalValue numericalValue;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11CavitySpecificationWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
        cavityDiameterWrapper = new Vec12To11CavityDiameterWrapper(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {

        final String methodName = method.getName();

        if ("getCavityDiameter".equals(methodName)) {
            if (numericalValue == null) {
                numericalValue = cavityDiameterWrapper.getCavityDiameter();
            }
            return numericalValue;
        }

        if ("setCavityDiameter".equals(methodName) && allArguments.length == 1) {
            numericalValue = (VecNumericalValue) allArguments[0];
            cavityDiameterWrapper.setCavityDiameter(numericalValue);
            return null;
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
