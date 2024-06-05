package com.foursoft.harness.vec.scripting;

import com.foursoft.harness.vec.v2x.VecCrimpDetail;
import com.foursoft.harness.vec.v2x.VecSize;

import static com.foursoft.harness.vec.scripting.factories.NumericalValueFactory.valueWithTolerance;

public abstract class CrimpDetailBuilder<THIS extends CrimpDetailBuilder<THIS, T>, T extends VecCrimpDetail>
        implements Builder<T> {
    protected final VecSession session;

    protected abstract T getCrimpDetail();

    protected CrimpDetailBuilder(final VecSession session) {
        this.session = session;
    }

    public THIS withHeight(double height, double lowerTolerance, double upperTolerance) {
        if (getCrimpDetail().getSize() == null) {
            getCrimpDetail().setSize(new VecSize());
        }
        getCrimpDetail().getSize().setHeight(valueWithTolerance(height, lowerTolerance, upperTolerance, session.mm()));
        return (THIS) this;
    }

    public THIS withWidth(double width, double lowerTolerance, double upperTolerance) {
        if (getCrimpDetail().getSize() == null) {
            getCrimpDetail().setSize(new VecSize());
        }
        getCrimpDetail().getSize().setWidth(valueWithTolerance(width, lowerTolerance, upperTolerance, session.mm()));
        return (THIS) this;
    }
}
