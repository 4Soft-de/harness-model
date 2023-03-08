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
package com.foursoft.harness.compatibility.core.util;

import com.foursoft.harness.compatibility.core.exception.WrapperException;

import java.lang.reflect.Field;

/**
 * Utility class for reflection operations.
 */
public final class ReflectionUtils {

    private ReflectionUtils() {
        // hide constructor
    }

    public static void setParentRelationship(final Object object, final String fieldName, final Object parent) {
        try {
            final Field field = org.reflections.ReflectionUtils
                    .getAllFields(object.getClass(),
                                  f -> f.getName().equals(fieldName))
                    .stream()
                    .findFirst().orElse(null);
            if (field == null) {
                final String errorMsg = String.format("Could not find field %s for class %s!",
                                                      fieldName, object.getClass());
                throw new WrapperException(errorMsg);
            }

            setFieldValue(object, field, parent);
        } catch (final IllegalAccessException e) {
            final String errorMsg = String.format("Could not set parent relation (field %s) between %s and %s!",
                                                  fieldName, object.getClass(), parent.getClass());
            throw new WrapperException(errorMsg, e);
        }
    }

    /**
     * Sets the field value for the field of the given target to the given value.
     *
     * @param target Target object which should be accessed / modified.
     * @param field  Field to set.
     * @param value  Value to set for the given field.
     * @throws IllegalAccessException See {@link Field#set(Object, Object)}.
     */
    public static synchronized void setFieldValue(final Object target, final Field field, final Object value)
            throws IllegalAccessException {
        // Ensure the field accessibility is always set to false in the end.
        try {
            field.setAccessible(true);
            field.set(target, value);
        } finally {
            field.setAccessible(false);
        }
    }

}
