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
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GeometryDimensionDetectorTest {

    @Test
    void should_returnTrueWhenCartesianPointHasExpectedDimensions() {
        // Given
        final KblCartesianPoint point = new KblCartesianPoint();
        point.getCoordinates().add(1.0);
        point.getCoordinates().add(2.0);
        point.getCoordinates().add(3.0);

        // When
        final boolean result = GeometryDimensionDetector.hasDimensions(point, 3);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void should_returnTrueWhenVectorHasExpectedDimensions() {
        // Given
        final List<Double> vectors = new ArrayList<>();
        vectors.add(1.0);
        vectors.add(2.0);
        vectors.add(3.0);

        // When
        final boolean result = GeometryDimensionDetector.hasDimensions(vectors, 3);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void should_returnTrueWhenFirstCartesianPointHasExpectedDimensions() {
        // Given
        final KblCartesianPoint point = new KblCartesianPoint();
        point.getCoordinates().add(1.0);
        point.getCoordinates().add(2.0);

        final List<KblCartesianPoint> vectors = new ArrayList<>();
        vectors.add(point);

        // When
        final boolean result = GeometryDimensionDetector.hasDimensions(vectors, 2);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void should_returnFalseWhenDimensionsAreNotEqual() {
        // Given
        final KblCartesianPoint point = new KblCartesianPoint();
        point.getCoordinates().add(1.0);

        // When
        final boolean result = GeometryDimensionDetector.hasDimensions(point, 2);

        // Then
        assertThat(result).isFalse();
    }
}
