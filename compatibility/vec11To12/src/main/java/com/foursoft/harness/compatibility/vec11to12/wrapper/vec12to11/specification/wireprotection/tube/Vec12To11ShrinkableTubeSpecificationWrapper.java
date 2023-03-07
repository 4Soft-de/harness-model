package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.wireprotection.tube;

import com.foursoft.harness.compatibility.core.CompatibilityContext;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecShrinkableTubeSpecification}
 * to {@link com.foursoft.harness.vec.v113.VecShrinkableTubeSpecification}.
 */
public class Vec12To11ShrinkableTubeSpecificationWrapper extends Vec12To11TubeSpecificationWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11ShrinkableTubeSpecificationWrapper(final CompatibilityContext context,
                                                       final Object target) {
        super(context, target);
    }

}
