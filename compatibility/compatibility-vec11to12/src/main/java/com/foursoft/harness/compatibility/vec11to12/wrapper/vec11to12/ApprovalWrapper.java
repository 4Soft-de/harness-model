package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12;

import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.compatibility.core.wrapper.Wraps;
import com.foursoft.harness.vec.v113.VecApproval;

@Wraps(VecApproval.class)
public class ApprovalWrapper extends ReflectionBasedWrapper {
    /**
     * Creates a wrapper for the given {@link Context} and target object.
     *
     * @param context Context for the wrapper.
     * @param target  Target object to adjust.
     */
    public ApprovalWrapper(final Context context, final Object target) {
        super(context, target);
        //registerValueProperty("additionalLevelInformation");
    }
}
