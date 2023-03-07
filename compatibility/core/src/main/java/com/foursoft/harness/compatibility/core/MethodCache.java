package com.foursoft.harness.compatibility.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class responsible for caching methods of classes.
 */
public class MethodCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodCache.class);

    private static final Map<Class<?>, Map<String, Method>> CLASS_TO_METHODS_MAP = new HashMap<>();

    private MethodCache() {
    }

    /**
     * Populates the cache with the given class.
     *
     * @param clazz Class to cache methods for.
     */
    public static void initClassCache(final Class<?> clazz) {
        synchronized (CLASS_TO_METHODS_MAP) {
            if (CLASS_TO_METHODS_MAP.containsKey(clazz)) {
                return;
            }

            final Map<String, Method> innerMap = Stream.of(clazz.getMethods())
                    .collect(Collectors.toMap(Method::getName, Function.identity(),
                                              // Override if the same method occurs another time.
                                              (m1, m2) -> m2));
            CLASS_TO_METHODS_MAP.put(clazz, innerMap);
        }
    }

    /**
     * Tries to get the method by its name with the given class.
     *
     * @param clazz      Class to lookup in the cache to get its methods from.
     * @param methodName Name of the method to get a {@link Method} object from.
     * @return Possibly-empty Optional if either the class or the method are not cached.
     */
    public static Optional<Method> get(final Class<?> clazz, final String methodName) {
        final String className = clazz.getName();

        synchronized (CLASS_TO_METHODS_MAP) {
            final Map<String, Method> methodNameToMethodMap = CLASS_TO_METHODS_MAP.get(clazz);
            if (methodNameToMethodMap == null) {
                LOGGER.error("Cannot find the method cache for class {}.", className);
                return Optional.empty();
            }

            final Method method = methodNameToMethodMap.get(methodName);
            if (method == null) {
                LOGGER.error("Cannot find the method {} in cache for class {}.", methodName, className);
                return Optional.empty();
            }

            return Optional.of(method);
        }
    }

}
