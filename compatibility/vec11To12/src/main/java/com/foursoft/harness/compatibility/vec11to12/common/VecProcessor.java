package com.foursoft.harness.compatibility.vec11to12.common;

import com.foursoft.harness.vec.common.HasVecVersion;

/**
 * Interface defining creating a {@code VecContent}.
 */
public interface VecProcessor {

    /**
     * Creates a VecContent with the given class by using the {@link VecProcessTask}.
     *
     * @param vecProcessTask VEC Process Task which holds all information of what to convert.
     * @param targetClass    The class which should be returned, should always be a VecContent.
     * @param <T>            Generic type allowing for different VecContents to be created.
     * @return A VecContent with the given class.
     */
    <T extends HasVecVersion> T createContent(VecProcessTask vecProcessTask, Class<T> targetClass);

}
