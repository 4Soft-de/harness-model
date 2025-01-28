/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2025 4Soft GmbH
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
package com.foursoft.harness.vec.scripting.harness;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.VecSession;
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

    public WireMountingDetailBuilder withCoreCrimpHeight(final double height, final double lowerTolerance,
                                                         final double upperTolerance) {
        if (wireMountingDetail.getCoreCrimpSize() == null) {
            wireMountingDetail.setCoreCrimpSize(new VecSize());
        }
        wireMountingDetail.getCoreCrimpSize().setHeight(
                valueWithTolerance(height, lowerTolerance, upperTolerance, session.mm()));
        return this;
    }

    public WireMountingDetailBuilder withCoreCrimpWidth(final double width, final double lowerTolerance,
                                                        final double upperTolerance) {
        if (wireMountingDetail.getCoreCrimpSize() == null) {
            wireMountingDetail.setCoreCrimpSize(new VecSize());
        }
        wireMountingDetail.getCoreCrimpSize().setWidth(
                valueWithTolerance(width, lowerTolerance, upperTolerance, session.mm()));
        return this;
    }

    public WireMountingDetailBuilder withInsulationCrimpHeight(final double height, final double lowerTolerance,
                                                               final double upperTolerance) {
        if (wireMountingDetail.getInsulationCrimpSize() == null) {
            wireMountingDetail.setInsulationCrimpSize(new VecSize());
        }
        wireMountingDetail.getInsulationCrimpSize().setHeight(
                valueWithTolerance(height, lowerTolerance, upperTolerance, session.mm()));
        return this;
    }

    public WireMountingDetailBuilder withInsulationCrimpWidth(final double width, final double lowerTolerance,
                                                              final double upperTolerance) {
        if (wireMountingDetail.getInsulationCrimpSize() == null) {
            wireMountingDetail.setInsulationCrimpSize(new VecSize());
        }
        wireMountingDetail.getInsulationCrimpSize().setWidth(
                valueWithTolerance(width, lowerTolerance, upperTolerance, session.mm()));
        return this;
    }

    public WireMountingDetailBuilder withWireTipProtrusion(final double min, final double max) {
        this.wireMountingDetail.setWireTipProtrusion(valueRange(min, max, session.mm()));
        return this;
    }

    public VecWireMountingDetail build() {
        return wireMountingDetail;
    }
}
