/*-
 * ========================LICENSE_START=================================
 * compatibility-core
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
package com.foursoft.harness.compatibility.core.wrapper;

import com.foursoft.harness.compatibility.core.MethodCache;
import com.foursoft.harness.compatibility.core.exception.WrapperException;
import com.foursoft.harness.compatibility.core.util.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Helper for the {@link CompatibilityWrapper}. Internal use only.
 */
public final class WrapperHelper {

    private final CompatibilityWrapper wrapper;

    WrapperHelper(final CompatibilityWrapper wrapper) {
        this.wrapper = wrapper;
    }

    /**
     * Helper method to invoke a method on an object.
     *
     * @param methodName  Name of the method to invoke.
     * @param target      Target object used for the method invocation.
     * @param targetClass The class which the result of the method invocation should have.
     * @param args        Additional method arguments.
     * @param <T>         Target type.
     * @return Possibly-empty Optional containing the result of the method invocation.
     */
    public <T> Optional<T> getResultObject(final String methodName, final Object target,
                                           final Class<T> targetClass, final Object... args) {
        final Class<?> nonProxyTargetClass = ClassUtils.getNonProxyClass(target.getClass());
        return MethodCache.get(nonProxyTargetClass, methodName)
                .map(method -> {
                    try {
                        return method.invoke(target, args);
                    } catch (final InvocationTargetException | IllegalAccessException e) {
                        final String errorMsg = String.format("Could not invoke method %s on class %s.",
                                                              methodName, nonProxyTargetClass.getSimpleName());
                        throw new WrapperException(errorMsg, e);
                    }
                })
                .filter(targetClass::isInstance)
                .map(targetClass::cast);
    }

    /**
     * Helper method to invoke a method on an object.
     *
     * @param methodName  Name of the method to invoke. The return type of that method needs to be a List.
     * @param target      Target object used for the method invocation.
     * @param targetClass The class which the result of the method invocation should have.
     * @param args        Additional method arguments.
     * @param <T>         Target type.
     * @return Possibly-empty Optional containing the result of the method invocation.
     */
    public <T> List<T> getResultList(final String methodName, final Object target,
                                     final Class<T> targetClass, final Object... args) {
        return invokeListMethod(false, methodName, target, targetClass, args);
    }

    /**
     * Helper method for methods which return a list.
     * This method creates a proxy for every list element.
     *
     * @param methodName  Name of the method to invoke. The return type of that method needs to be a List.
     * @param target      Target object used for the method invocation.
     * @param targetClass The target class the list elements should have after a proxy was created.
     * @param args        Additional method arguments.
     * @param <T>         Target type of the list.
     * @return Possibly-empty list with proxy elements of the given target class.
     */
    public <T> List<T> wrapList(final String methodName, final Object target,
                                final Class<T> targetClass, final Object... args) {
        return invokeListMethod(true, methodName, target, targetClass, args);
    }

    /**
     * Helper method for methods which return a list.
     * This method will get the first element of the list and creates a proxy for it.
     *
     * @param methodName  Name of the method to invoke. The return type of that method needs to be a List.
     * @param target      Target object used for the method invocation.
     * @param targetClass The target class the Optional should have after a proxy was created.
     * @param args        Additional method arguments.
     * @param <T>         Target type of the Optional.
     * @return Possibly-empty Optional containing a proxy element of the given target class.
     */
    public <T> Optional<T> wrapListToSingleElement(final String methodName, final Object target,
                                                   final Class<T> targetClass, final Object... args) {
        return getResultObject(methodName, target, List.class, args)
                .filter(Predicate.not(List::isEmpty))
                .map(list -> list.get(0))
                .map(wrapper.getContext().getWrapperProxyFactory()::createProxy)
                .filter(targetClass::isInstance)
                .map(targetClass::cast);
    }

    /**
     * Returns the given object if not {@code null}, else
     * the given method will be invoked and the first element will be returned.
     *
     * @param object     Object to check whether it is {@code null} or not.
     * @param methodName Name of the method to invoke. The return type of that method needs to be a List.
     * @param target     Target object used for the method invocation.
     * @param args       Additional method arguments.
     * @param <T>        Input and Output Type.
     * @return The given object if not {@code null}, else the first proxy element of the invoked method.
     */
    public <T> T wrapListToSingleElementIfNull(final T object,
                                               final String methodName, final Object target,
                                               final Object... args) {
        if (object != null) {
            return object;
        }

        // Use Object.class to avoid filtering.
        return (T) wrapListToSingleElement(methodName, target, Object.class, args).orElse(null);
    }

    private <T> List<T> invokeListMethod(final boolean createProxy,
                                         final String methodName, final Object target,
                                         final Class<T> targetClass, final Object... args) {
        final List<?> list = getResultObject(methodName, target, List.class, args)
                .orElseGet(Collections::emptyList);
        return list.stream()
                .map(elem -> createProxy
                        ? wrapper.getContext().getWrapperProxyFactory().createProxy(elem)
                        : elem)
                .filter(targetClass::isInstance)
                .map(targetClass::cast)
                .collect(Collectors.toList());
    }

}
