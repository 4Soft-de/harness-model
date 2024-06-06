package com.foursoft.harness.vec.scripting;

import com.foursoft.harness.vec.v2x.VecSize;
import com.foursoft.harness.vec.v2x.VecWireMountingDetail;

import static com.foursoft.harness.vec.scripting.factories.NumericalValueFactory.valueWithTolerance;
import static com.foursoft.harness.vec.scripting.factories.ValueRangeFactory.valueRange;

public class WireMountingDetailBuilder implements Builder<VecWireMountingDetail> {

    private final VecWireMountingDetail wireMountingDetail = new VecWireMountingDetail();
    private final VecSession session;

    public WireMountingDetailBuilder(final VecSession session) {
        this.session = session;
    }

    public WireMountingDetailBuilder withCoreCrimpHeight(double height, double lowerTolerance, double upperTolerance) {
        if (wireMountingDetail.getCoreCrimpSize() == null) {
            wireMountingDetail.setCoreCrimpSize(new VecSize());
        }
        wireMountingDetail.getCoreCrimpSize().setHeight(
                valueWithTolerance(height, lowerTolerance, upperTolerance, session.mm()));
        return this;
    }

    public WireMountingDetailBuilder withCoreCrimpWidth(double width, double lowerTolerance, double upperTolerance) {
        if (wireMountingDetail.getCoreCrimpSize() == null) {
            wireMountingDetail.setCoreCrimpSize(new VecSize());
        }
        wireMountingDetail.getCoreCrimpSize().setWidth(
                valueWithTolerance(width, lowerTolerance, upperTolerance, session.mm()));
        return this;
    }

    public WireMountingDetailBuilder withInsulationCrimpHeight(double height, double lowerTolerance,
                                                               double upperTolerance) {
        if (wireMountingDetail.getInsulationCrimpSize() == null) {
            wireMountingDetail.setInsulationCrimpSize(new VecSize());
        }
        wireMountingDetail.getInsulationCrimpSize().setHeight(
                valueWithTolerance(height, lowerTolerance, upperTolerance, session.mm()));
        return this;
    }

    public WireMountingDetailBuilder withInsulationCrimpWidth(double width, double lowerTolerance,
                                                              double upperTolerance) {
        if (wireMountingDetail.getInsulationCrimpSize() == null) {
            wireMountingDetail.setInsulationCrimpSize(new VecSize());
        }
        wireMountingDetail.getInsulationCrimpSize().setWidth(
                valueWithTolerance(width, lowerTolerance, upperTolerance, session.mm()));
        return this;
    }

    public WireMountingDetailBuilder withWireTipProtrusion(double min, double max) {
        this.wireMountingDetail.setWireTipProtrusion(valueRange(min, max, session.mm()));
        return this;
    }

    public VecWireMountingDetail build() {
        return wireMountingDetail;
    }
}
