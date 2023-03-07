package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.wireprotection.tube;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.wireprotection.Vec12To11WireProtectionSpecificationWrapper;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecTubeSpecification}
 * to {@link com.foursoft.harness.vec.v113.VecTubeSpecification}.
 */
public class Vec12To11TubeSpecificationWrapper extends Vec12To11WireProtectionSpecificationWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11TubeSpecificationWrapper(final CompatibilityContext context,
                                             final Object target) {
        super(context, target);
    }

}
