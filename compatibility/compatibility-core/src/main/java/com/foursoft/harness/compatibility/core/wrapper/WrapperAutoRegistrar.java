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
package com.foursoft.harness.compatibility.core.wrapper;

import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.compatibility.core.WrapperRegistry;
import com.foursoft.harness.compatibility.core.exception.WrapperException;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * Discovers {@link Wraps}-annotated wrapper classes via classpath scanning and registers them
 * with the {@link WrapperRegistry} of a given {@link Context}.
 * <p>
 * Each annotated class must declare a public constructor with the signature
 * {@code (Context context, Object target)}. For every source class listed in
 * {@link Wraps#value()}, a factory function is registered that instantiates the wrapper
 * via that constructor.
 */
public final class WrapperAutoRegistrar {

    private static final Logger LOGGER = LoggerFactory.getLogger(WrapperAutoRegistrar.class);

    private WrapperAutoRegistrar() {
    }

    /**
     * Scans {@code basePackage} for {@link Wraps}-annotated classes and registers them on
     * the {@link WrapperRegistry} of the given context.
     *
     * @param context     Context whose registry will receive the registrations.
     * @param basePackage Package prefix to scan (e.g. {@code "com.foo.compat.wrapper"}).
     */
    public static void registerAll(final Context context, final String basePackage) {
        final Set<Class<?>> wrapperClasses =
                new Reflections(basePackage).getTypesAnnotatedWith(Wraps.class);
        final WrapperRegistry registry = context.getWrapperRegistry();

        int registrations = 0;
        for (final Class<?> wrapperClass : wrapperClasses) {
            final Class<?>[] sourceClasses = wrapperClass.getAnnotation(Wraps.class).value();
            if (sourceClasses.length == 0) {
                throw new WrapperException(
                        "@Wraps on " + wrapperClass.getName() + " must declare at least one source class.");
            }
            final Constructor<?> ctor = resolveConstructor(wrapperClass, context);
            for (final Class<?> sourceClass : sourceClasses) {
                registry.register(sourceClass, target -> instantiate(ctor, context, target));
                registrations++;
            }
        }

        LOGGER.info("Auto-registered {} wrapper(s) for {} source class(es) from package '{}'.",
                    wrapperClasses.size(), registrations, basePackage);
    }

    /**
     * Finds a public 2-argument constructor whose first parameter is assignable from {@code context}
     * and whose second parameter accepts the target. This permits both {@code (Context, Object)} and
     * {@code (CompatibilityContext, Object)} forms.
     */
    private static Constructor<?> resolveConstructor(final Class<?> wrapperClass, final Context context) {
        for (final Constructor<?> ctor : wrapperClass.getConstructors()) {
            final Class<?>[] params = ctor.getParameterTypes();
            if (params.length == 2
                    && params[0].isInstance(context)
                    && params[1].isAssignableFrom(Object.class)) {
                return ctor;
            }
        }
        throw new WrapperException(
                "Wrapper " + wrapperClass.getName()
                        + " annotated with @Wraps must declare a public constructor (Context, Object) "
                        + "compatible with the runtime context type " + context.getClass().getName() + ".");
    }

    private static InvocationHandler instantiate(final Constructor<?> ctor,
                                                 final Context context,
                                                 final Object target) {
        try {
            return (InvocationHandler) ctor.newInstance(context, target);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new WrapperException(
                    "Failed to instantiate wrapper " + ctor.getDeclaringClass().getName()
                            + " for target of type " + (target == null ? "null" : target.getClass().getName()),
                    e);
        }
    }

}
