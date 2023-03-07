package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.wireprotection;

import com.foursoft.harness.compatibility.core.CompatibilityContext;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecStripeSpecification}
 * to {@link com.foursoft.harness.vec.v113.VecStripeSpecification}.
 */
public class Vec12To11StripeSpecificationWrapper extends Vec12To11WireProtectionSpecificationWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11StripeSpecificationWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

}
