/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.transform.geometry;

import com.foursoft.harness.kbl.v25.KblCartesianPoint;

import java.util.List;

public class GeometryDimensionDetector {
    public static final int GEO_2D = 2;
    public static final int GEO_3D = 3;

    private GeometryDimensionDetector() {
    }

    public static boolean hasDimensions(final KblCartesianPoint vector, final int numOfDimensions) {
        return hasDimensions(vector.getCoordinates(), numOfDimensions);
    }

    public static boolean hasDimensions(final List<?> vectors, final int numOfDimensions) {
        if (vectors == null || vectors.isEmpty()) {
            return false;
        }
        final Object first = vectors.get(0);
        if (first instanceof Double) {
            return vectors.size() == numOfDimensions;
        } else if (first instanceof final KblCartesianPoint cartesianPoint) {
            return hasDimensions(cartesianPoint, numOfDimensions);
        }
        return false;
    }
}
