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
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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

    /**
     * Caches the classpath scan per set of base package classes. The scan result depends on the classpath
     * only, not on the {@link Context} the wrappers are registered for, so it can safely be shared between
     * all contexts of a class loader. Without this cache every wrapper instantiation triggers a full
     * classpath scan, which is both slow and noisy in the log.
     */
    private static final Map<List<Class<?>>, Set<Class<?>>> SCAN_CACHE = new ConcurrentHashMap<>();

    private WrapperAutoRegistrar() {
    }

    /**
     * Scans {@code basePackage} for {@link Wraps}-annotated classes and registers them on
     * the {@link WrapperRegistry} of the given context.
     * <p>
     * Every discovered class must implement {@link InvocationHandler}; registration fails fast
     * with a {@link com.foursoft.harness.compatibility.core.exception.WrapperException} if it does not.
     * Declaring the same source class in more than one {@link Wraps} annotation within the scanned
     * package is also an error and will throw immediately instead of silently overwriting an earlier
     * registration.
     *
     * @param context            Context whose registry will receive the registrations.
     * @param basePackageClasses Classes in packages to scan, type safe way to specify packages.
     */
    public static void registerAll(final Context context, final Class<?>... basePackageClasses) {
        final String[] packages = Arrays.stream(basePackageClasses).map(Class::getPackageName).toArray(String[]::new);
        final Set<Class<?>> wrapperClasses = scanWrapperClasses(basePackageClasses);
        final WrapperRegistry registry = context.getWrapperRegistry();
        final Set<Class<?>> seenSourceClasses = new HashSet<>();

        int registrations = 0;
        for (final Class<?> wrapperClass : wrapperClasses) {
            if (!InvocationHandler.class.isAssignableFrom(wrapperClass)) {
                throw new WrapperException(
                        "Wrapper " + wrapperClass.getName()
                                + " annotated with @Wraps must implement "
                                + InvocationHandler.class.getName() + ".");
            }
            final Class<?>[] sourceClasses = wrapperClass.getAnnotation(Wraps.class).value();
            if (sourceClasses.length == 0) {
                throw new WrapperException(
                        "@Wraps on " + wrapperClass.getName() + " must declare at least one source class.");
            }
            final Constructor<?> ctor = resolveConstructor(wrapperClass, context);
            for (final Class<?> sourceClass : sourceClasses) {
                if (!seenSourceClasses.add(sourceClass)) {
                    throw new WrapperException(
                            "Duplicate @Wraps registration: source class " + sourceClass.getName()
                                    + " is mapped by more than one wrapper in package '"
                                    + Arrays.toString(packages) + "'. Only one wrapper per source class is allowed.");
                }
                registry.register(sourceClass, target -> instantiate(ctor, context, target));
                registrations++;
            }
        }

        LOGGER.trace("Registered {} wrapper(s) for {} source class(es) on a new context from packages '{}'.",
                     wrapperClasses.size(), registrations, packages);
    }

    /**
     * Returns the {@link Wraps}-annotated classes in the packages of {@code basePackageClasses}, scanning
     * the classpath only on the first call for a given set of base package classes.
     *
     * @param basePackageClasses Classes in packages to scan.
     * @return The annotated wrapper classes, never {@code null}.
     */
    static Set<Class<?>> scanWrapperClasses(final Class<?>... basePackageClasses) {
        return SCAN_CACHE.computeIfAbsent(List.of(basePackageClasses), WrapperAutoRegistrar::scanUncached);
    }

    private static Set<Class<?>> scanUncached(final List<Class<?>> basePackageClasses) {
        final String[] packages = basePackageClasses.stream().map(Class::getPackageName).toArray(String[]::new);
        final FilterBuilder filterBuilder = new FilterBuilder();
        for (final String packageName : packages) {
            filterBuilder.includePackage(packageName);
        }
        final URL[] urls = basePackageClasses.stream().map(
                p -> p.getProtectionDomain().getCodeSource().getLocation()).toArray(URL[]::new);

        final Set<Class<?>> wrapperClasses = Set.copyOf(
                new Reflections(new ConfigurationBuilder().setUrls(urls)
                                        .filterInputsBy(filterBuilder)
                                        .forPackages(packages).addScanners(
                                Scanners.TypesAnnotated)).getTypesAnnotatedWith(Wraps.class));
        LOGGER.debug("Found {} @Wraps annotated wrapper(s) in packages '{}'.", wrapperClasses.size(), packages);
        return wrapperClasses;
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
