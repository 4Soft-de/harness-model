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

import com.foursoft.harness.kbl.v25.KblBSplineCurve;
import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class KnotVectorTest {

    @Test
    void should_returnClampedUniformKnots() {
        // Given
        final KblBSplineCurve source = new KblBSplineCurve();

        final int degree = 6;
        source.setDegree(BigInteger.valueOf(degree));

        final List<KblCartesianPoint> controlPoints = List.of(new KblCartesianPoint(), new KblCartesianPoint(),
                                                              new KblCartesianPoint(), new KblCartesianPoint(),
                                                              new KblCartesianPoint(), new KblCartesianPoint(),
                                                              new KblCartesianPoint());
        source.getControlPoints().addAll(controlPoints);

        // expectedKnotVectorSize = numberOfControlPoints + degree + 1 = 14
        final int expectedKnotVectorSize = 14;

        final KnotVector knotVector = new KnotVector(source, Clamping.CLAMPED);

        // When
        final List<Double> result = knotVector.deriveKnots();

        // Then
        assertThat(result).isNotNull()
                .hasSize(expectedKnotVectorSize)
                .containsExactly(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
    }

    @Test
    void should_returnUnclampedUniformKnots() {
        // Given
        final KblBSplineCurve source = new KblBSplineCurve();

        final int degree = 6;
        source.setDegree(BigInteger.valueOf(degree));

        final List<KblCartesianPoint> controlPoints = List.of(new KblCartesianPoint(), new KblCartesianPoint(),
                                                             new KblCartesianPoint(), new KblCartesianPoint(),
                                                             new KblCartesianPoint(), new KblCartesianPoint(),
                                                             new KblCartesianPoint());
        source.getControlPoints().addAll(controlPoints);

        // expectedKnotVectorSize = numberOfControlPoints + degree + 1 = 14
        final int expectedKnotVectorSize = 14;

        final KnotVector knotVector = new KnotVector(source, Clamping.UNCLAMPED);

        // When
        final List<Double> result = knotVector.deriveKnots();

        // Then
        assertThat(result).isNotNull()
                .hasSize(expectedKnotVectorSize)
                .containsExactly(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0);
    }

    @Test
    void should_returnClampedKnotsWithMiddleVector() {
        // Given
        final KblBSplineCurve source = new KblBSplineCurve();

        final int degree = 2;
        source.setDegree(BigInteger.valueOf(degree));

        final List<KblCartesianPoint> controlPoints = List.of(new KblCartesianPoint(), new KblCartesianPoint(),
                                                              new KblCartesianPoint(), new KblCartesianPoint(),
                                                              new KblCartesianPoint(), new KblCartesianPoint(),
                                                              new KblCartesianPoint());
        source.getControlPoints().addAll(controlPoints);

        // expectedKnotVectorSize = numberOfControlPoints + degree + 1 = 10
        final int expectedKnotVectorSize = 10;

        final KnotVector knotVector = new KnotVector(source, Clamping.CLAMPED);

        // When
        final List<Double> result = knotVector.deriveKnots();

        // Then
        assertThat(result).isNotNull()
                .hasSize(expectedKnotVectorSize)
                .containsExactly(0.0, 0.0, 0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 5.0, 5.0);
    }

    @Test
    void should_returnEmptyList_whenNumberOfControlPointsNotGreaterThanOrder() {
        // Given
        final  KblBSplineCurve source = new KblBSplineCurve();

        final int degree = 6;
        source.setDegree(BigInteger.valueOf(degree));

        final List<KblCartesianPoint> controlPoints = List.of(new KblCartesianPoint());
        source.getControlPoints().addAll(controlPoints);

        final KnotVector knotVector = new KnotVector(source, Clamping.CLAMPED);

        // When
        final List<Double> result = knotVector.deriveKnots();

        // Then
        assertThat(result).isNotNull().isEmpty();
    }
}
