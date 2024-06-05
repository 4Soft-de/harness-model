package com.foursoft.harness.vec.scripting.factories;

import com.foursoft.harness.vec.v2x.VecWireType;

public final class WireTypeFactory {
    private WireTypeFactory() {
        throw new AssertionError();
    }

    public static VecWireType din76722(String wireType) {
        VecWireType type = new VecWireType();
        type.setType(wireType);
        type.setReferenceSystem("DIN 76722");
        return type;
    }
}
