package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.field.Vec11To12CavityDimensionWrapper;
import com.foursoft.harness.vec.v12x.VecSize;

import java.lang.reflect.Method;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecCavitySpecification}
 * to {@link com.foursoft.harness.vec.v12x.VecCavitySpecification}.
 */
public class Vec11To12CavitySpecificationWrapper extends ReflectionBasedWrapper {

    private final Vec11To12CavityDimensionWrapper cavityDimensionWrapper;

    private VecSize vecSize;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12CavitySpecificationWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
        cavityDimensionWrapper = new Vec11To12CavityDimensionWrapper(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        final String methodName = method.getName();

        if ("getCavityDimension".equals(methodName)) {
            if (vecSize == null) {
                vecSize = cavityDimensionWrapper.getCavityDimension();
            }
            return vecSize;
        }

        if ("setCavityDimension".equals(methodName) && allArguments.length == 1) {
            vecSize = (VecSize) allArguments[0];
            cavityDimensionWrapper.setCavityDimension(vecSize);
            return null;
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
