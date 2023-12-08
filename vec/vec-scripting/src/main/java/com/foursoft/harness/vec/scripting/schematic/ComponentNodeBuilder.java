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

import com.foursoft.harness.vec.common.util.StringUtils;
import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.v2x.VecComponentConnector;
import com.foursoft.harness.vec.v2x.VecComponentNode;
import com.foursoft.harness.vec.v2x.VecComponentPort;

import java.util.HashMap;
import java.util.Map;

import static com.foursoft.harness.vec.scripting.factories.LocalizedStringFactory.en;

public class ComponentNodeBuilder implements Builder<VecComponentNode> {

    private VecComponentNode componentNode = new VecComponentNode();

    private final Map<String, VecComponentConnector> componentConnectors = new HashMap<>();

    public ComponentNodeBuilder(String identification) {

        componentNode.setIdentification(identification);
    }

    public ComponentNodeBuilder addPort(final String componentConnectorId, final String portId) {
        return this.addPort(componentConnectorId, portId, null);
    }

    public ComponentNodeBuilder addPort(final String componentConnectorId, final String portId, String description) {
        final VecComponentConnector connector = this.componentConnectors.computeIfAbsent(componentConnectorId,
                                                                                         this::createSlot);

        VecComponentPort port = new VecComponentPort();
        port.setIdentification(portId);
        connector.getComponentPorts().add(port);

        if (StringUtils.isNotEmpty(description)) {
            port.getDescriptions().add(en(description));
        }

        return this;
    }

    @Override public VecComponentNode build() {
        return componentNode;
    }

    private VecComponentConnector createSlot(final String identification) {
        final VecComponentConnector connector = new VecComponentConnector();
        connector.setIdentification(identification);
        componentNode.getComponentConnectors().add(connector);
        return connector;
    }

    public ComponentNodeBuilder addChildNode(String identification, Customizer<ComponentNodeBuilder> customizer) {
        ComponentNodeBuilder componentNodeBuilder = new ComponentNodeBuilder(identification);

        customizer.customize(componentNodeBuilder);

        this.componentNode.getChildNodes().add(componentNodeBuilder.build());

        return this;
    }
}
