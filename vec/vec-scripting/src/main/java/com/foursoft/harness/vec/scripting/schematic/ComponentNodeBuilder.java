/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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
package com.foursoft.harness.vec.scripting.schematic;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.scripting.VecScriptingException;
import com.foursoft.harness.vec.v2x.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.foursoft.harness.vec.common.util.StreamUtils.findOne;

public class ComponentNodeBuilder implements Builder<VecComponentNode> {

    private final Function<String, VecNetworkNode> networkNodeLookup;
    private final Function<String, VecSignal> signalLookup;
    private final VecComponentNode componentNode = new VecComponentNode();

    private final Map<String, VecComponentConnector> componentConnectors = new HashMap<>();

    public ComponentNodeBuilder(final String identification, final Function<String, VecNetworkNode> networkNodeLookup,
                                final Function<String, VecSignal> signalLookup) {
        this.networkNodeLookup = networkNodeLookup;
        this.signalLookup = signalLookup;

        componentNode.setIdentification(identification);
    }

    public ComponentNodeBuilder withNetworkNode(final String identification) {
        componentNode.setNetworkNode(networkNodeLookup.apply(identification));
        return this;
    }

    public ComponentNodeBuilder addPort(final String componentConnectorId, final String portId) {
        return this.addPort(componentConnectorId, portId, pb -> {
        });
    }

    public ComponentNodeBuilder addPort(final String componentConnectorId, final String portId,
                                        final String description) {

        return this.addPort(componentConnectorId, portId, pb -> {
            pb.withDescription(description);
        });

    }

    public ComponentNodeBuilder addPort(final String componentConnectorId, final String portId,
                                        final Customizer<ComponentPortBuilder> customizer) {
        final VecComponentConnector connector = this.componentConnectors.computeIfAbsent(componentConnectorId,
                                                                                         this::createSlot);
        final ComponentPortBuilder componentPortBuilder = new ComponentPortBuilder(portId, this::findNetworkPort,
                                                                                   signalLookup);

        customizer.customize(componentPortBuilder);

        connector.getComponentPorts().add(componentPortBuilder.build());

        return this;

    }

    @Override
    public VecComponentNode build() {
        return componentNode;
    }

    private VecComponentConnector createSlot(final String identification) {
        final VecComponentConnector connector = new VecComponentConnector();
        connector.setIdentification(identification);
        componentNode.getComponentConnectors().add(connector);
        return connector;
    }

    public ComponentNodeBuilder addChildNode(final String identification,
                                             final Customizer<ComponentNodeBuilder> customizer) {
        final ComponentNodeBuilder componentNodeBuilder = new ComponentNodeBuilder(identification, networkNodeLookup,
                                                                                   signalLookup);

        customizer.customize(componentNodeBuilder);

        this.componentNode.getChildNodes().add(componentNodeBuilder.build());

        return this;
    }

    private VecNetworkPort findNetworkPort(final String identification) {
        final VecNetworkNode networkNode = this.componentNode.getNetworkNode();
        if (networkNode == null) {
            throw new VecScriptingException("The component node " + this.componentNode.getIdentification() +
                                                    " is not associated with a NetworkPort.");
        }
        return networkNode
                .getPorts()
                .stream()
                .filter(port -> identification.equals(port.getIdentification()))
                .collect(findOne());
    }
}
