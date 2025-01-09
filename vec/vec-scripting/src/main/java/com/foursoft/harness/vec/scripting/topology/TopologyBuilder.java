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
import com.foursoft.harness.vec.scripting.DefaultValues;
import com.foursoft.harness.vec.scripting.Locator;
import com.foursoft.harness.vec.v2x.VecConfigurationConstraint;
import com.foursoft.harness.vec.v2x.VecTopologyNode;
import com.foursoft.harness.vec.v2x.VecTopologySpecification;

public class TopologyBuilder implements Builder<VecTopologySpecification> {

    private final VecTopologySpecification topologySpecification;
    private final Locator<VecConfigurationConstraint> configurationConstraintLocator;

    public TopologyBuilder(Locator<VecConfigurationConstraint> configurationConstraintLocator) {
        this.configurationConstraintLocator = configurationConstraintLocator;
        this.topologySpecification = initializeTopologySpecification();
    }

    @Override public VecTopologySpecification build() {
        return topologySpecification;
    }

    private static VecTopologySpecification initializeTopologySpecification() {
        final VecTopologySpecification specification = new VecTopologySpecification();
        specification.setIdentification(DefaultValues.TOPO_SPEC_IDENTIFICATION);
        return specification;
    }

    public TopologyBuilder addNode(String identification, Customizer<TopologyNodeBuilder> customizer) {
        TopologyNodeBuilder builder = new TopologyNodeBuilder(identification);

        customizer.customize(builder);

        this.topologySpecification.getTopologyNodes().add(builder.build());

        return this;
    }

    public TopologyBuilder addNode(String identification) {
        return addNode(identification, x -> {
        });
    }

    public TopologyBuilder addSegment(String startNodeIdentification, String endNodeIdentification,
                                      String identification, Customizer<TopologySegmentBuilder> customizer) {
        VecTopologyNode startNode = findOrCreateNode(startNodeIdentification);
        VecTopologyNode endNode = findOrCreateNode(endNodeIdentification);

        TopologySegmentBuilder builder = new TopologySegmentBuilder(configurationConstraintLocator, identification,
                                                                    startNode, endNode);

        customizer.customize(builder);

        this.topologySpecification.getTopologySegments().add(builder.build());

        return this;
    }

    public TopologyBuilder addSegment(String startNodeIdentification, String endNodeIdentification,
                                      String identification) {
        return this.addSegment(startNodeIdentification, endNodeIdentification, identification, x -> {
        });
    }

    private VecTopologyNode findOrCreateNode(String identification) {
        return this.topologySpecification.getTopologyNodes().stream().filter(
                t -> t.getIdentification().equals(identification)).findFirst().orElseGet(() -> {
            addNode(identification);
            return findOrCreateNode(identification);
        });
    }
}
