package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;

import java.lang.reflect.Method;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecOccurrenceOrUsage}
 * to {@link com.foursoft.harness.vec.v113.VecOccurrenceOrUsage}.
 */
public class Vec12To11OccurrenceOrUsageWrapper extends ReflectionBasedWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11OccurrenceOrUsageWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getRolesWithType".equals(method.getName())) {
            final Class<?> vec11xClass = (Class<?>) allArguments[0];
            final Class<?> vec12xClass = getContext().getClassMapper().map(vec11xClass);
            return wrapList("getRolesWithType", vec11xClass, vec12xClass);
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
