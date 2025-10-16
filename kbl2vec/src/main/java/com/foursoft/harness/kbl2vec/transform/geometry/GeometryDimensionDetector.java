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

    private GeometryDimensionDetector() {
    }

    public enum DIMENSION {
        TWO_D,
        THREE_D,
        NONE
    }

    public static boolean isTwoDimensional(final KblCartesianPoint cartesianPoint) {
        return cartesianPoint.getCoordinates().size() == 2;
    }

    public static boolean isThreeDimensional(final KblCartesianPoint cartesianPoint) {
        return cartesianPoint.getCoordinates().size() >= 3;
    }

    public static boolean isTwoDimensional(final List<Double> vectors) {
        return vectors.size() == 2;
    }

    public static boolean isThreeDimensional(final List<Double> vectors) {
        return vectors.size() >= 3;
    }

    public static DIMENSION getNumberOfDimensions(final List<KblCartesianPoint> cartesianPoints) {
        if (cartesianPoints == null || cartesianPoints.isEmpty()) {
            return DIMENSION.NONE;
        }

        final KblCartesianPoint firstCartesianPoint = cartesianPoints.get(0);
        if (isTwoDimensional(firstCartesianPoint)) {
            return DIMENSION.TWO_D;
        } else if (isThreeDimensional(firstCartesianPoint)) {
            return DIMENSION.THREE_D;
        } else {
            return DIMENSION.NONE;
        }
    }
}
