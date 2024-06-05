package com.foursoft.harness.vec.scripting.factories;

import com.foursoft.harness.vec.v2x.VecUnit;
import com.foursoft.harness.vec.v2x.VecValueRange;

public final class ValueRangeFactory {

    private ValueRangeFactory() {
        throw new AssertionError();
    }

    public static VecValueRange valueRange(final double min, final double max, VecUnit unit) {
        final VecValueRange valueRange = new VecValueRange();
        valueRange.setMinimum(min);
        valueRange.setMaximum(max);
        valueRange.setUnitComponent(unit);

        return valueRange;
    }
}
