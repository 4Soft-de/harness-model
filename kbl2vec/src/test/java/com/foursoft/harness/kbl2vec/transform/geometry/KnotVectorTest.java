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

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class KnotVectorTest {

    @Test
    void should_returnClampedUniformKnots() {
        // Given
        final int degree = 6;
        final int order = 7;
        final int numberOfControlPoints = 7;
        final int expectedKnotVectorSize = order + numberOfControlPoints;

        final KnotVector knotVector = new KnotVector(degree, order, numberOfControlPoints, Constants.CLAMPED);

        // When
        final List<Double> result = knotVector.getKnots();

        // Then
        assertThat(result).isNotNull()
                .hasSize(expectedKnotVectorSize)
                .containsExactly(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
    }

    @Test
    void should_returnUnclampedUniformKnots() {
        // Given
        final int degree = 6;
        final int order = 7;
        final int numberOfControlPoints = 7;
        final int expectedKnotVectorSize = order + numberOfControlPoints;

        final KnotVector knotVector = new KnotVector(degree, order, numberOfControlPoints, Constants.UNCLAMPED);

        // When
        final List<Double> result = knotVector.getKnots();

        // Then
        assertThat(result).isNotNull()
                .hasSize(expectedKnotVectorSize)
                .containsExactly(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0);
    }

    @Test
    void should_returnClampedKnotsWithMiddleVector() {
        // Given
        final int degree = 2;
        final int order = 3;
        final int numberOfControlPoints = 7;
        final int expectedKnotVectorSize = order + numberOfControlPoints;

        final KnotVector knotVector = new KnotVector(degree, order, numberOfControlPoints, Constants.CLAMPED);

        // When
        final List<Double> result = knotVector.getKnots();

        // Then
        assertThat(result).isNotNull()
                .hasSize(expectedKnotVectorSize)
                .containsExactly(0.0, 0.0, 0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 5.0, 5.0);
    }

    @Test
    void should_returnEmptyList_whenNumberOfControlPointsNotGreaterThanOrder() {
        // Given
        final int degree = 6;
        final int order = 7;
        final int numberOfControlPoints = 5;

        final KnotVector knotVector = new KnotVector(degree, order, numberOfControlPoints, Constants.CLAMPED);

        // When
        final List<Double> result = knotVector.getKnots();

        // Then
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void should_returnEmptyList_whenOrderIsNotDegreePlusOne() {
        // Given
        final int degree = 6;
        final int oder = 10;
        final int numberOfControlPoints = 7;

        final KnotVector knotVector = new KnotVector(degree, oder, numberOfControlPoints, Constants.CLAMPED);

        // When
        final List<Double> result = knotVector.getKnots();

        // Then
        assertThat(result).isNotNull().isEmpty();
    }
}
