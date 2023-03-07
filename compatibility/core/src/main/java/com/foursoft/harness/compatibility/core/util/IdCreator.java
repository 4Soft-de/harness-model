package com.foursoft.harness.compatibility.core.util;

import java.util.UUID;

/**
 * Utility class which create ids.
 */
public final class IdCreator {

    private IdCreator() {
        // hide constructor
    }

    /**
     * Generate an id for the given object.
     * Will use its class for id creation.
     *
     * @param object Object to get an id for.
     * @return Never-null String containing an id for this object.
     */
    public static String generateXmlId(final Object object) {
        return generateXmlId(object.getClass());
    }

    /**
     * Generate an id for the given class.
     *
     * @param clazz Class to get an id for.
     * @return Never-null String containing an id for this Class.
     */
    public static String generateXmlId(final Class<?> clazz) {
        return String.join("_", "new", clazz.getSimpleName(), UUID.randomUUID().toString());
    }

}
