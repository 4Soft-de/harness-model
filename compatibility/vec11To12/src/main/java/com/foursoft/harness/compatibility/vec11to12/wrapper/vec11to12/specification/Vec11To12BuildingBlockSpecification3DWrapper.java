package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.vec.v113.VecZone;

import java.lang.reflect.Method;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecBuildingBlockSpecification3D}
 * to {@link com.foursoft.harness.vec.v12x.VecBuildingBlockSpecification3D}.
 */
public class Vec11To12BuildingBlockSpecification3DWrapper extends ReflectionBasedWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12BuildingBlockSpecification3DWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getTopologyZone".equals(method.getName())) {
            return getResultObject("getZone", VecZone.class, allArguments)
                    .map(getContext().getWrapperProxyFactory()::createProxy)
                    .orElse(null);
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
