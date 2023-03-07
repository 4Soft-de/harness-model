package com.foursoft.harness.compatibility.core;

import com.foursoft.harness.compatibility.core.mapping.ClassMapper;

/**
 * Interface for a context.
 */
public interface Context {

    /**
     * Returns the {@link ClassMapper} of this Context.
     *
     * @return The {@link ClassMapper} of this Context.
     */
    ClassMapper getClassMapper();

    /**
     * Returns the {@link WrapperRegistry} of this Context.
     *
     * @return The {@link WrapperRegistry} of this Context.
     */
    WrapperRegistry getWrapperRegistry();

    /**
     * Provides access to the {@link HasUnsupportedMethods unsupported methods} of this Context.
     *
     * @return The {@link HasUnsupportedMethods unsupported methods} of this Context.
     */
    HasUnsupportedMethods checkUnsupportedMethods();

    /**
     * Returns the {@link WrapperProxyFactory} of this Context.
     *
     * @return The {@link WrapperProxyFactory} of this Context.
     */
    WrapperProxyFactory getWrapperProxyFactory();

    /**
     * Returns the target object of this Context.
     *
     * @return The target object of this Context.
     */
    Object getContent();

    /**
     * Sets the {@link ClassMapper} of this Context.
     *
     * @param content The new target object of this Context.
     */
    void setContent(Object content);

}
