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
package com.foursoft.harness.vec.scripting.topology;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.enums.TemperatureType;
import com.foursoft.harness.vec.scripting.factories.ValueRangeFactory;
import com.foursoft.harness.vec.v2x.VecRobustnessProperties;
import com.foursoft.harness.vec.v2x.VecTemperatureInformation;
import com.foursoft.harness.vec.v2x.VecTopologyZone;
import com.foursoft.harness.vec.v2x.VecValueRange;

public class TopologyZoneBuilder implements Builder<VecTopologyZone> {

    private final VecTopologyZone topologyZone = new VecTopologyZone();
    private final VecSession session;
    private final TopologySpecificationQueries queries;

    TopologyZoneBuilder(final VecSession session, final String identification,
                        final TopologySpecificationQueries queries) {
        this.session = session;
        this.queries = queries;
        topologyZone.setIdentification(identification);
    }

    public TopologyZoneBuilder withAssignedSegment(final String segmentId,
                                                   final Customizer<TopologyZoneAssigmentBuilder> customizer) {
        final TopologyZoneAssigmentBuilder builder = new TopologyZoneAssigmentBuilder(this.session,
                                                                                      queries.findTopologySegment(
                                                                                              segmentId));

        customizer.customize(builder);

        this.topologyZone.getAssignments().add(builder.build());

        return this;
    }

    public TopologyZoneBuilder withAmbientTemperature(final double minimum, final double maximum) {
        final VecValueRange range = ValueRangeFactory.valueRange(minimum, maximum, session.degreeCelsius());
        final VecTemperatureInformation temperatureInformation = new VecTemperatureInformation();

        temperatureInformation.setTemperatureRange(range);
        temperatureInformation.setTemperatureType(TemperatureType.AMBIENT_TEMPERATURE.value());

        topologyZone.setAmbientTemperature(temperatureInformation);

        return this;
    }

    public TopologyZoneBuilder withLiquidIngressRequirement(final String ipClassRequirement) {
        final VecRobustnessProperties rp = new VecRobustnessProperties();
        rp.setClazz("LiquidIngressProtection");
        rp.setClassKey(ipClassRequirement);
        rp.setClassReferenceSystem("ISO 20653");
        rp.setHasRobustness(true);

        topologyZone.getRequiredRobustnessProperties().add(rp);
        return this;
    }

    public TopologyZoneBuilder withAssignedSegment(final String segmentId) {
        return withAssignedSegment(segmentId, x -> {
        });
    }

    @Override
    public VecTopologyZone build() {
        return topologyZone;
    }
}
