/*-
 * ========================LICENSE_START=================================
 * compatibility-core
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
