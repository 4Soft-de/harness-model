package com.foursoft.harness.compatibility.core;

/**
 * An interface which allows checking whether a method of a class is supported or not.
 * This is done by using a {@link MethodIdentifier}.
 */
@FunctionalInterface
public interface HasUnsupportedMethods {

    /**
     * @return {@code true} if the method of the class is not supported, else {@code false}.
     */
    boolean isNotSupported(MethodIdentifier method);

}
