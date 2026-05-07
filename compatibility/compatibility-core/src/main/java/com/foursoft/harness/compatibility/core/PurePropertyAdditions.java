/*-
 * ========================LICENSE_START=================================
 * Compatibility Core
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
package com.foursoft.harness.compatibility.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry of {@link PropertyAddition}s keyed by source-version class.
 *
 * <p>The lookup respects inheritance: {@link #getAdditions(Class)} walks from the given class up
 * through all super-classes (excluding {@code Object}), collects every registered addition, and
 * returns the union as an immutable list. Results are cached after the first request.
 */
public final class PurePropertyAdditions {

    private static final PurePropertyAdditions EMPTY = new PurePropertyAdditions();

    private final Map<Class<?>, List<PropertyAddition>> additionsPerClass = new HashMap<>();
    private final Map<Class<?>, List<PropertyAddition>> cache = new ConcurrentHashMap<>();

    /**
     * Returns the shared empty registry (no allocations).
     */
    public static PurePropertyAdditions empty() {
        return EMPTY;
    }

    /**
     * Registers one or more additions for the given class. Returns {@code this} for chaining.
     */
    public PurePropertyAdditions register(final Class<?> clazz, final PropertyAddition... additions) {
        additionsPerClass.computeIfAbsent(clazz, k -> new ArrayList<>())
                .addAll(Arrays.asList(additions));
        return this;
    }

    /**
     * Returns all additions applicable to {@code clazz}, collected from the class itself and every
     * super-class up to (but not including) {@code Object}. The result is cached.
     */
    public List<PropertyAddition> getAdditions(final Class<?> clazz) {
        return cache.computeIfAbsent(clazz, this::computeAdditions);
    }

    private List<PropertyAddition> computeAdditions(final Class<?> clazz) {
        final List<PropertyAddition> result = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            final List<PropertyAddition> own = additionsPerClass.get(current);
            if (own != null) {
                result.addAll(own);
            }
            current = current.getSuperclass();
        }
        return Collections.unmodifiableList(result);
    }
}
