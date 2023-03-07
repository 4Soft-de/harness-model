package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.wireprotection;

import com.foursoft.harness.compatibility.core.CompatibilityContext;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecFittingSpecification}
 * to {@link com.foursoft.harness.vec.v113.VecFittingSpecification}.
 */
public class Vec12To11FittingSpecificationWrapper extends Vec12To11WireProtectionSpecificationWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11FittingSpecificationWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

}
