package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.vec.v113.VecOccurrenceOrUsage;

import java.lang.reflect.Method;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecOccurrenceOrUsageViewItem3D}
 * to {@link com.foursoft.harness.vec.v113.VecOccurrenceOrUsageViewItem3D}.
 */
public class Vec12To11OccurrenceOrUsageViewItem3DWrapper extends ReflectionBasedWrapper {

    private VecOccurrenceOrUsage occurrenceOrUsage;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11OccurrenceOrUsageViewItem3DWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getOccurrenceOrUsage".equals(method.getName())) {
            wrapListToSingleElement("getOccurrenceOrUsage", VecOccurrenceOrUsage.class)
                    .ifPresent(occUrUsage -> occurrenceOrUsage = occUrUsage);

            return occurrenceOrUsage;
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
