package com.foursoft.harness.kbl2vec.transform.geometry;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl.v25.KblUnit;

import java.util.Objects;

public final class GeometryUnitDetector {
    private GeometryUnitDetector() {
    }

    public static KblUnit getUnit(final KblHarness source) {
        return source.getParentKBLContainer().getSegments().stream()
                .map(s -> s.getPhysicalLength() != null ? s.getPhysicalLength() : s.getVirtualLength())
                .filter(Objects::nonNull)
                .map(KblNumericalValue::getUnitComponent)
                .findFirst()
                .orElse(null);
    }
}
