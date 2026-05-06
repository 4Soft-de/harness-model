package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12;

import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.compatibility.core.wrapper.Wraps;
import com.foursoft.harness.vec.v113.VecAliasIdentification;

@Wraps(VecAliasIdentification.class)
public class AliasIdentificationWrapper extends ReflectionBasedWrapper {
    /**
     * Creates a wrapper for the given {@link Context} and target object.
     *
     * @param context Context for the wrapper.
     * @param target  Target object to adjust.
     */
    public AliasIdentificationWrapper(final Context context, final Object target) {
        super(context, target);
        registerValueProperty("type");
    }
}
