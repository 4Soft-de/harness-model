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
 * </ul>
 */
public sealed interface PropertyAddition {

    String propertyName();

    record Value(String propertyName) implements PropertyAddition {}

    record MutableList(String propertyName) implements PropertyAddition {}

    record BackRef(String propertyName) implements PropertyAddition {}

    static Value value(final String propertyName) {
        return new Value(propertyName);
    }

    static MutableList list(final String propertyName) {
        return new MutableList(propertyName);
    }

    static BackRef backRef(final String propertyName) {
        return new BackRef(propertyName);
    }
}
