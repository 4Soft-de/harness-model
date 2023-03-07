package com.foursoft.harness.compatibility.core.wrapper;

import com.foursoft.harness.compatibility.core.Context;

/**
 * Interface for a Compatibility Wrapper which holds a {@link Context}.
 */
public interface CompatibilityWrapper {

    /**
     * Returns the {@link Context} for the wrapper.
     *
     * @return The {@link Context} for the wrapper.
     */
    Context getContext();

}
