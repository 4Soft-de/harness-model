package com.foursoft.harness.kbl2vec.transform.geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public record KnotVector(int order, int numberOfControlPoints, String clamping) {

    public List<Double> getKnots() {
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
