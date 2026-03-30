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
package com.foursoft.harness.vec.files.guidelines;

import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.enums.LengthClassification;
import com.foursoft.harness.vec.scripting.topology.TopologyBuilder;
import com.foursoft.harness.vec.scripting.topology.TopologyZonesBuilder;
import com.foursoft.harness.vec.v2x.VecAnchorType;
import org.junit.jupiter.api.Test;

import static com.foursoft.harness.vec.files.TestUtils.storeVecAndValidate;
import static org.assertj.core.api.Assertions.assertThatCode;

class TopologyTest {

    @Test
    void should_create_a_topology_with_requirements() {
        final VecSession session = new VecSession();

        session.harness("123456789", "A", harness -> {
            harness.withTopology(this::buildTopology);
            harness.withTopologyZones(this::buildZones);
        });

        assertThatCode(
                storeVecAndValidate("ecad-wiki-guideline-topology", session)).doesNotThrowAnyException();

    }

    private void buildZones(final TopologyZonesBuilder zones) {
        zones.addZone("HOT", zone -> {
                    zone.withAmbientTemperature(-30, 125)
                            .withAssignedSegment("SEG-I", assigment ->
                                    assigment.withCoverage(coverage ->
                                                                   coverage.from("ND-IV", VecAnchorType.FROM_END_NODE)
                                                                           .to("Loc A", VecAnchorType.FROM_END_NODE,
                                                                               600)
                                    )
                            )
                            .withAssignedSegment("SEG-II", assigment ->
                                    assigment.withCoverage(coverage ->
                                                                   coverage.from("ND-IV", VecAnchorType.FROM_START_NODE)
                                                                           .to("Loc B", VecAnchorType.FROM_START_NODE,
                                                                               500)
                                    )
                            )
                            .withAssignedSegment("SEG-III");
                })
                .addZone("WET", zone -> {
                    zone.withLiquidIngressRequirement("9K")
                            .withAssignedSegment("SEG-II", assigment ->
                                    assigment.withCoverage(coverage ->
                                                                   coverage.from("Loc C", VecAnchorType.FROM_START_NODE,
                                                                                 200)
                                                                           .to("Loc D", VecAnchorType.FROM_START_NODE,
                                                                               600)
                                    )
                            );
                });
    }

    private void buildTopology(final TopologyBuilder topology) {
        topology.addNode("ND-I")
                .addNode("ND-II")
                .addNode("ND-III")
                .addNode("ND-IV")
                .addSegment("ND-I", "ND-IV", "SEG-I",
                            segment -> segment.withSegmentLength(1100, LengthClassification.DESIGNED))
                .addSegment("ND-IV", "ND-II", "SEG-II",
                            segment -> segment.withSegmentLength(1200, LengthClassification.DESIGNED))
                .addSegment("ND-III", "ND-IV", "SEG-III",
                            segment -> segment.withSegmentLength(1300, LengthClassification.DESIGNED))
        ;
    }

}
