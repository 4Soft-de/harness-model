package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.vec11to12.Constants;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecCavityPlugSpecification}
 * to {@link com.foursoft.harness.vec.v12x.VecCavityPlugSpecification}.
 * <p>
 * Extending {@link Vec11To12CavityPartSpecificationWrapper} to convert {@link Constants#GET_CAVITY_DIMENSIONS}.
 */
public class Vec11To12CavityPlugSpecificationWrapper extends Vec11To12CavityPartSpecificationWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12CavityPlugSpecificationWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

}
