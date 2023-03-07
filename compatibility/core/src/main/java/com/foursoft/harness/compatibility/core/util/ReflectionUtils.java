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
