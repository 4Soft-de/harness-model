package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecWireElement}
 * to {@link com.foursoft.harness.vec.v113.VecWireElement}.
 */
public class Vec12To11WireElementWrapper extends ReflectionBasedWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11WireElementWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

}
