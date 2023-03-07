package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.vec.v113.VecPartUsage;

import java.lang.reflect.Method;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecPartOccurrence}
 * to {@link com.foursoft.harness.vec.v113.VecPartOccurrence}.
 */
public class Vec12To11PartOccurrenceWrapper extends Vec12To11OccurrenceOrUsageWrapper {

    private VecPartUsage realizedPartUsage;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11PartOccurrenceWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getRealizedPartUsage".equals(method.getName())) {
            wrapListToSingleElement("getRealizedPartUsage", VecPartUsage.class)
                    .ifPresent(partUsage -> realizedPartUsage = partUsage);

            return realizedPartUsage;
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
