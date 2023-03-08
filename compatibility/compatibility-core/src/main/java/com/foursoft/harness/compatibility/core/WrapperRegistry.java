/*-
 * ========================LICENSE_START=================================
 * Compatibility Core
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
package com.foursoft.harness.compatibility.core;

import com.foursoft.harness.compatibility.core.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Class responsible for registering wrapper functions.
 */
public class WrapperRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(WrapperRegistry.class);

    private final Function<Object, InvocationHandler> defaultWrapper;
    private final Map<Class<?>, Function<Object, InvocationHandler>> wrapperFunctionByClass = new HashMap<>();

    public WrapperRegistry(final Function<Object, InvocationHandler> defaultWrapper) {
        this.defaultWrapper = defaultWrapper;
    }

    /**
     * Finds or creates an {@link InvocationHandler} for the given object.
     * Will use the default wrapper this {@code WrapperRegistry} was created with if
     * no own function was registered beforehand.
     *
     * @param target Target object to create an {@link InvocationHandler} for.
     * @return {@code InvocationHandler} for the given target object.
     * @see #register(Class, Function)
     */
    public InvocationHandler findOrCreate(final Object target) {
        final Class<?> aClass = ClassUtils.getNonProxyClass(target.getClass());
        return wrapperFunctionByClass.computeIfAbsent(aClass, d -> defaultWrapper).apply(target);
    }

    /**
     * Registers the given class with the given function.
     * This will be used when calling {@link #findOrCreate(Object)}.
     *
     * @param clazz    Class to register.
     * @param function Function to register for the given class.
     * @return The updated {@code WrapperRegistry}, useful for chaining.
     */
    public WrapperRegistry register(final Class<?> clazz, final Function<Object, InvocationHandler> function) {
        checkClassCollision(clazz);
        wrapperFunctionByClass.put(clazz, function);
        return this;
    }

    private void checkClassCollision(final Class<?> clazz) {
        if (wrapperFunctionByClass.containsKey(clazz)) {
            LOGGER.warn("The class {} already has a Function registered. It will be overwritten now.",
                        clazz.getCanonicalName());
        }
    }

}
