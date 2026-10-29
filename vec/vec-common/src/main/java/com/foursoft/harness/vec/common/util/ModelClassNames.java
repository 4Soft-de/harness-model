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
package com.foursoft.harness.vec.common.util;

/**
 * Derives the names of the classes of the VEC model from the classes of the generated Java API.
 * <p>
 * The generated Java classes carry the prefix {@code Vec} ({@code VecConnectorHousingRole}), the classes of the
 * model do not ({@code ConnectorHousingRole}). The root class {@code VecContent} is the exception, its model name
 * contains the prefix already.
 */
public final class ModelClassNames {

    private static final String JAVA_API_PREFIX = "Vec";
    private static final String VEC_CONTENT = "VecContent";

    private ModelClassNames() {
        // hide default constructor
    }

    /**
     * Returns the name of the model class represented by the given class of the Java API, e.g.
     * {@code CartesianVector3D} for {@code VecCartesianVector3D}.
     *
     * @param clazz Class of the Java API.
     * @return The name of the model class.
     */
    public static String modelClassName(final Class<?> clazz) {
        final String name = clazz.getSimpleName();
        if (VEC_CONTENT.equals(name) || !name.startsWith(JAVA_API_PREFIX)) {
            return name;
        }
        return name.substring(JAVA_API_PREFIX.length());
    }

    /**
     * Returns the abbreviation of the model class represented by the given class of the Java API, made of the
     * capital letters of the {@linkplain #modelClassName(Class) model class name}, e.g. {@code PSS} for
     * {@code VecPartStructureSpecification}.
     *
     * @param clazz Class of the Java API.
     * @return The abbreviated name of the model class.
     */
    public static String abbreviatedModelClassName(final Class<?> clazz) {
        return modelClassName(clazz).replaceAll("[^A-Z]", "");
    }

}
