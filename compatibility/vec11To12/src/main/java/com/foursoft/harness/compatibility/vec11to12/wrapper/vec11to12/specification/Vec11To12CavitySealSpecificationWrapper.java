package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.vec11to12.Constants;
import com.foursoft.harness.vec.v12x.VecNumericalValue;

import java.lang.reflect.Method;
// wrapper for new VecCavitySealSpecification().getCavityDimensions() .add().add()

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecCavitySealSpecification}
 * to {@link com.foursoft.harness.vec.v12x.VecCavitySealSpecification}.
 * <p>
 * Extending {@link Vec11To12CavityPartSpecificationWrapper} to convert {@link Constants#GET_CAVITY_DIMENSIONS}.
 */
public class Vec11To12CavitySealSpecificationWrapper extends Vec11To12CavityPartSpecificationWrapper {

    private VecNumericalValue insideDiameter;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12CavitySealSpecificationWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        final String methodName = method.getName();

        if ("getInsideDiameter".equals(methodName)) {
            return insideDiameter;
        }
        if ("setInsideDiameter".equals(methodName) && allArguments.length == 1) {
            insideDiameter = (VecNumericalValue) allArguments[0];
            return null;
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
