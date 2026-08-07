/*-
 * ========================LICENSE_START=================================
 * VEC Common
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
package com.foursoft.harness.vec.common.openenum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry of the open enumeration literals contributed by {@link OpenEnumLiteralProvider}s.
 *
 * <p>
 * This class is consulted by the generated {@code of(String)} factories and is rarely used directly.
 * Providers are loaded once, on first use, and the result is cached per open enumeration.
 * </p>
 *
 * @see OpenEnumLiteralProvider
 */
public final class OpenEnumLiterals {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenEnumLiterals.class);

    private static final Object LOCK = new Object();

    private static final Map<Class<?>, Map<String, OpenEnumLiteral>> INDEX = new ConcurrentHashMap<>();

    private static volatile List<OpenEnumLiteral> contributedLiterals;

    private OpenEnumLiterals() {
        throw new AssertionError("OpenEnumLiterals must not be instantiated.");
    }

    /**
     * Returns the contributed literal of the given open enumeration with the given value.
     *
     * @param type  The interface of the open enumeration, for example
     *              {@code VecDocumentTypeLiteral.class}.
     * @param value The literal as it appears in the XML.
     * @param <T>   The type of the open enumeration.
     * @return The contributed literal, or {@code null} if no provider contributed one with that
     * value. Never throws.
     */
    public static <T extends OpenEnumLiteral> T resolve(final Class<T> type, final String value) {
        if (type == null || value == null) {
            return null;
        }
        return type.cast(indexFor(type).get(value));
    }

    /**
     * Discards the loaded providers and the cached literals, so that the next lookup loads them
     * again. Intended for tests that install a provider after the registry has been used.
     */
    public static void reload() {
        synchronized (LOCK) {
            contributedLiterals = null;
            INDEX.clear();
        }
    }

    private static Map<String, OpenEnumLiteral> indexFor(final Class<? extends OpenEnumLiteral> type) {
        return INDEX.computeIfAbsent(type, OpenEnumLiterals::buildIndex);
    }

    private static Map<String, OpenEnumLiteral> buildIndex(final Class<?> type) {
        final Map<String, OpenEnumLiteral> index = new HashMap<>();
        for (final OpenEnumLiteral literal : contributedLiterals()) {
            if (!type.isInstance(literal)) {
                continue;
            }
            final OpenEnumLiteral previous = index.putIfAbsent(literal.value(), literal);
            if (previous != null && previous != literal) {
                LOGGER.warn("Literal '{}' of {} is contributed by {} and {}. Keeping the first one.",
                            literal.value(), type.getName(), previous.getClass()
                                    .getName(), literal.getClass()
                                    .getName());
            }
        }
        return Map.copyOf(index);
    }

    private static List<OpenEnumLiteral> contributedLiterals() {
        List<OpenEnumLiteral> literals = contributedLiterals;
        if (literals == null) {
            synchronized (LOCK) {
                literals = contributedLiterals;
                if (literals == null) {
                    literals = loadLiterals();
                    contributedLiterals = literals;
                }
            }
        }
        return literals;
    }

    private static List<OpenEnumLiteral> loadLiterals() {
        final List<OpenEnumLiteral> literals = new ArrayList<>();
        for (final OpenEnumLiteralProvider provider : loadProviders().values()) {
            try {
                final Collection<? extends OpenEnumLiteral> contributed = provider.literals();
                if (contributed != null) {
                    literals.addAll(contributed);
                }
            } catch (final RuntimeException e) {
                LOGGER.warn("Provider {} failed to contribute open enumeration literals. Ignoring it.",
                            provider.getClass()
                                    .getName(), e);
            }
        }
        return List.copyOf(literals);
    }

    /**
     * Loads the providers visible to the context class loader and to this class' own class loader,
     * keyed by provider class so that a provider visible to both is only used once.
     */
    private static Map<Class<?>, OpenEnumLiteralProvider> loadProviders() {
        final Map<Class<?>, OpenEnumLiteralProvider> providers = new LinkedHashMap<>();
        for (final ClassLoader classLoader : classLoaders()) {
            try {
                for (final OpenEnumLiteralProvider provider :
                        ServiceLoader.load(OpenEnumLiteralProvider.class, classLoader)) {
                    providers.putIfAbsent(provider.getClass(), provider);
                }
            } catch (final ServiceConfigurationError e) {
                LOGGER.warn("Loading open enumeration literal providers failed. Ignoring them.", e);
            }
        }
        return providers;
    }

    private static List<ClassLoader> classLoaders() {
        final ClassLoader own = OpenEnumLiterals.class.getClassLoader();
        final ClassLoader context = Thread.currentThread()
                .getContextClassLoader();
        if (context == null || context == own) {
            return List.of(own);
        }
        return List.of(context, own);
    }

}
