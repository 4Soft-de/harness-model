package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.vec.v12x.VecWireReceptionAddOn;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecWireReceptionSpecification}
 * to {@link com.foursoft.harness.vec.v12x.VecWireReceptionSpecification}.
 */
public class Vec11To12WireReceptionSpecificationWrapper extends ReflectionBasedWrapper {

    private final List<VecWireReceptionAddOn> addons = new ArrayList<>();

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12WireReceptionSpecificationWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getAddOns".equals(method.getName())) {
            return addons;
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
