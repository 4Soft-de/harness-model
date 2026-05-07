package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12;

import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.compatibility.core.wrapper.Wraps;
import com.foursoft.harness.vec.v113.VecGeneralTechnicalPartSpecification;

@Wraps({VecGeneralTechnicalPartSpecification.class})
public class GeneralTechnicalPartSpecificationWrapper extends DefaultWrapper {

    public GeneralTechnicalPartSpecificationWrapper(final Context context,
                                                    final Object target) {
        super(context, target);
        registerValueProperty("isUnspecifiedAccessoryPermitted", "setUnspecifiedAccessoryPermitted");
    }
}
