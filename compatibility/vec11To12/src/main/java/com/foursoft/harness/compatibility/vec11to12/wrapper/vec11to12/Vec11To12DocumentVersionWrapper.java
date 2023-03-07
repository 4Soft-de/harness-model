package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;

import java.lang.reflect.Method;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecDocumentVersion}
 * to {@link com.foursoft.harness.vec.v12x.VecDocumentVersion}.
 */
public class Vec11To12DocumentVersionWrapper extends ReflectionBasedWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12DocumentVersionWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getSpecificationsWithType".equals(method.getName())) {
            final Class<?> vec12xClass = (Class<?>) allArguments[0];
            final Class<?> vec11xClass = getContext().getClassMapper().map(vec12xClass);
            return wrapList("getSpecificationsWithType", vec12xClass, vec11xClass);
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
