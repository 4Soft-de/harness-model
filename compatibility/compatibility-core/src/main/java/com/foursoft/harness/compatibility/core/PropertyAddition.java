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

/**
 * Describes a property that exists in the target API version but has no counterpart in the source
 * version. These additions are configured in the class mapper and are automatically applied to
 * every proxy created for a class that declares (or inherits) them.
 *
 * <ul>
 *   <li>{@link Value} – a scalar or object-reference property with a getter and setter</li>
 *   <li>{@link MutableList} – a {@code List}-returning getter with no setter (JAXB lazy-init)</li>
 *   <li>{@link BackRef} – a read-only {@code Set}-returning back-reference with no setter</li>
 *   <li>{@link Renamed} – a property that exists in the source version under another name</li>
 * </ul>
 */
public sealed interface PropertyAddition {

    String propertyName();

    record Value(String propertyName) implements PropertyAddition {}

    record MutableList(String propertyName) implements PropertyAddition {}

    record BackRef(String propertyName) implements PropertyAddition {}

    /**
     * A property that is read from another property of the source version, e.g. because the property was
     * renamed or became a list (and got the plural name of a list).
     * <p>
     * The value is adapted to the return type of the target version, so a single value read for a list
     * property becomes a list with that value. Only the getter is redirected.
     *
     * @param propertyName       Name of the property in the target version.
     * @param sourcePropertyName Name of the property in the source version which provides the value.
     */
    record Renamed(String propertyName, String sourcePropertyName) implements PropertyAddition {}

    static Value value(final String propertyName) {
        return new Value(propertyName);
    }

    static MutableList list(final String propertyName) {
        return new MutableList(propertyName);
    }

    static BackRef backRef(final String propertyName) {
        return new BackRef(propertyName);
    }

    static Renamed renamed(final String propertyName, final String sourcePropertyName) {
        return new Renamed(propertyName, sourcePropertyName);
    }
}
