package com.foursoft.harness.compatibility.vec11to12.wrapper;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.vec11to12.Vec11XTo12XCompatibilityWrapper;

public abstract class AbstractBaseWrapperTest {

    protected static CompatibilityContext get11To12Context() {
        return new Vec11XTo12XCompatibilityWrapper().getContext();
    }

}
