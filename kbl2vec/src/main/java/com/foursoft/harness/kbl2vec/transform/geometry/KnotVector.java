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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public record KnotVector(int degree, int order, int numberOfControlPoints, String clamping) {

    public List<Double> getKnots() {
        if (numberOfControlPoints <= degree || order != degree + 1) {
            return new ArrayList<>();
        }

        final int knotLength = numberOfControlPoints + order;

        if (Constants.CLAMPED.equals(clamping)) {
            return clampedUniformKnots(knotLength);
        }

        if (Constants.UNCLAMPED.equals(clamping)) {
            return unclampedUniformKnots(knotLength);
        }

        return new ArrayList<>();
    }

    private List<Double> clampedUniformKnots(final int knotLength) {
        final List<Double> knots = new ArrayList<>();
        for (int i = 0; i < order; i++) {
            knots.add(0.0);
        }

        final int middleKnotLength = knotLength - 2 * order; // subtract start- and end knots
        final List<Double> middleKnot = IntStream.range(1, middleKnotLength + 1)
                .asDoubleStream()
                .boxed()
                .toList();
        knots.addAll(middleKnot);

        for (int i = 0; i < order; i++) {
            knots.add(middleKnotLength + 1.0);
        }

        return knots;
    }

    private List<Double> unclampedUniformKnots(final int knotLength) {
        final List<Double> knots = new ArrayList<>();
        for (double i = 0; i < knotLength; i++) {
            knots.add(i);
        }

        return knots;
    }
}
