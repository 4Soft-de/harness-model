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

import com.foursoft.harness.vec.scripting.*;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecTopologySpecification;
import com.foursoft.harness.vec.v2x.VecTopologyZoneSpecification;

public class TopologyZonesBuilder implements Builder<VecTopologyZoneSpecification> {
    private final VecTopologyZoneSpecification zoneSpecification;
    private final VecSession session;
    private TopologySpecificationQueries topologyQueries = new TopologySpecificationQueries();

    public TopologyZonesBuilder(final VecSession session, final VecTopologySpecification topologySpecification) {
        this.session = session;
        this.zoneSpecification = init();
        this.topologyQueries = new TopologySpecificationQueries(topologySpecification);

    }

    private static VecTopologyZoneSpecification init() {
        final VecTopologyZoneSpecification result = new VecTopologyZoneSpecification();
        result.setIdentification(DefaultValues.TOPO_ZONE_SPEC_IDENTIFICATION);
        return result;
    }

    public TopologyZonesBuilder withTopology(final String topologyDocumentNumber) {
        final VecDocumentVersion topoDocument = session.findDocument(topologyDocumentNumber);
        topologyQueries = new TopologySpecificationQueries(
                topoDocument.getSpecificationWithType(VecTopologySpecification.class).orElseThrow(
                        () -> new VecScriptingException(
                                "No TopologySpecification found in document with number: " + topologyDocumentNumber)));
        return this;
    }

    public TopologyZonesBuilder addZone(final String identification, final Customizer<TopologyZoneBuilder> customizer) {
        final TopologyZoneBuilder builder = new TopologyZoneBuilder(session, identification, topologyQueries);

        customizer.customize(builder);

        this.zoneSpecification.getZones().add(builder.build());

        return this;
    }

    @Override
    public VecTopologyZoneSpecification build() {
        return this.zoneSpecification;
    }
}
