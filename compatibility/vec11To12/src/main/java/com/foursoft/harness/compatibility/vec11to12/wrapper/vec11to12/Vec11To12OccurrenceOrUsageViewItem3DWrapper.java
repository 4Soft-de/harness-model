package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.vec.v113.VecOccurrenceOrUsage;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecOccurrenceOrUsageViewItem3D}
 * to {@link com.foursoft.harness.vec.v12x.VecOccurrenceOrUsageViewItem3D}.
 */
public class Vec11To12OccurrenceOrUsageViewItem3DWrapper extends ReflectionBasedWrapper {

    // Object so no cast is needed.
    // VEC 1.2.X has a list instead of a single object.
    private final List<Object> occurrenceOrUsages = new ArrayList<>(1);

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12OccurrenceOrUsageViewItem3DWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getOccurrenceOrUsage".equals(method.getName())) {
            if (occurrenceOrUsages.isEmpty()) {
                getResultObject("getOccurrenceOrUsage", VecOccurrenceOrUsage.class)
                        .map(getContext().getWrapperProxyFactory()::createProxy)
                        .ifPresent(occurrenceOrUsages::add);
            }

            return occurrenceOrUsages;
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
