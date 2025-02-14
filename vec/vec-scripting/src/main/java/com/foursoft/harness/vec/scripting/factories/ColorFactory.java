package com.foursoft.harness.vec.scripting.factories;

import com.foursoft.harness.vec.v2x.VecColor;

public final class ColorFactory {

    private ColorFactory() {
        throw new AssertionError();
    }

    public static VecColor color(final String referenceSystem, final String key) {
        final VecColor color = new VecColor();
        color.setReferenceSystem(referenceSystem);
        color.setKey(key);
        return color;
    }

    public static VecColor ral(final String key) {
        return color("RAL", key);
    }
}
