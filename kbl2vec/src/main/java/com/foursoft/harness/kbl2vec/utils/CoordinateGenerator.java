package com.foursoft.harness.kbl2vec.utils;

import java.util.List;

public class CoordinateGenerator {

    private CoordinateGenerator() {}

    public static double getCoordinateOrDefault(final List<Double> coordinates, final int index) {
        if (coordinates == null || index >= coordinates.size() || coordinates.get(index) == null) {
            return 0.0;
        }
        return coordinates.get(index);
    }
}
