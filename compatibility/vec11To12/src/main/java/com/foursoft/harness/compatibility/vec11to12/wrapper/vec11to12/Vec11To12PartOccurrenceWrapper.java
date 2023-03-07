package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.vec.v113.VecPartUsage;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecPartOccurrence}
 * to {@link com.foursoft.harness.vec.v12x.VecPartOccurrence}.
 */
public class Vec11To12PartOccurrenceWrapper extends Vec11To12OccurrenceOrUsageWrapper {

    // Object so no cast is needed.
    // VEC 1.2.X has a List instead of a single object.
    private final List<Object> partUsages = new ArrayList<>(1);

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12PartOccurrenceWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getRealizedPartUsage".equals(method.getName())) {
            if (partUsages.isEmpty()) {
                getResultObject("getRealizedPartUsage", VecPartUsage.class)
                        .map(getContext().getWrapperProxyFactory()::createProxy)
                        .ifPresent(partUsages::add);
            }
            return partUsages;
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
