package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12;

import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.compatibility.core.wrapper.Wraps;
import com.foursoft.harness.vec.v113.VecWireElementReference;

@Wraps({VecWireElementReference.class})
public class WireElementReferenceWrapper extends DefaultWrapper {

    public WireElementReferenceWrapper(final Context context,
                                       final Object target) {
        super(context, target);
        registerValueProperty("isUnconnected", "setUnconnected");
    }
}
