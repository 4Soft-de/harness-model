package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.compatibility.vec11to12.common.VecVersion;

import java.lang.reflect.Method;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecContent}
 * to {@link com.foursoft.harness.vec.v12x.VecContent}.
 */
public class Vec11To12ContentWrapper extends ReflectionBasedWrapper {

    private String vecVersion = VecVersion.VEC12X.getCurrentVersion();

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12ContentWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getVecVersion".equals(method.getName())) {
            return vecVersion;
        }

        if ("setVecVersion".equals(method.getName()) && allArguments.length == 1) {
            getResultObject("setVecVersion", Void.class, allArguments);
            vecVersion = (String) allArguments[0];
            return null;
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
