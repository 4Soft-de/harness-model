package com.foursoft.harness.vec.scripting;

import com.foursoft.harness.vec.v2x.VecWireEnd;

import static com.foursoft.harness.vec.scripting.factories.NumericalValueFactory.valueWithTolerance;

public class WireEndBuilder implements Builder<VecWireEnd> {

    private final VecWireEnd wireEnd;
    private final VecSession session;

    public WireEndBuilder(VecSession session) {
        this.session = session;
        wireEnd = new VecWireEnd();
    }

    public WireEndBuilder withStrippingLength(double value, double lowerTolerance,
                                              final double upperTolerance) {
        wireEnd.setStrippingLength(valueWithTolerance(value, lowerTolerance, upperTolerance, session.mm()));
        return this;
    }

    @Override public VecWireEnd build() {
        return wireEnd;
    }
}
