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
