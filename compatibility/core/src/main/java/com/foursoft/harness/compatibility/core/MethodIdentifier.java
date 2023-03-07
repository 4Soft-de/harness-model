package com.foursoft.harness.compatibility.core;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Helper class to better identify a method. Holds the name of a method and the class it belongs to.
 */
public class MethodIdentifier {

    private final String className;
    private final String methodName;

    /**
     * Standard constructor to create a new MethodIdentifier.
     *
     * @param className  The simple name of the class used by the called method.
     * @param methodName The name of the method which should be called.
     * @see #of(Method)
     */
    public MethodIdentifier(final String className, final String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "MethodIdentifier{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                '}';
    }

    /**
     * Returns the class name of the method.
     *
     * @return The class name of the method.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Returns the method name of the method.
     *
     * @return The method name of the method.
     */
    public String getMethodName() {
        return methodName;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MethodIdentifier that = (MethodIdentifier) obj;
        return Objects.equals(className, that.className) &&
                Objects.equals(methodName, that.methodName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, methodName);
    }

    /**
     * Factory method to easily create a {@link MethodIdentifier}.
     *
     * @param method Method to create a MethodIdentifier for. Has to be non-{@code null}.
     * @return A new MethodIdentifier for the given method.
     */
    public static MethodIdentifier of(final Method method) {
        Objects.requireNonNull(method);
        final String className = method.getDeclaringClass().getSimpleName();
        final String methodName = method.getName();
        return new MethodIdentifier(className, methodName);
    }

}
