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

import com.foursoft.harness.vec.v2x.*;
import com.foursoft.harness.vec.v2x.visitor.BaseVisitor;
import com.foursoft.harness.vec.v2x.visitor.DepthFirstTraverserImpl;
import com.foursoft.harness.vec.v2x.visitor.TraversingVisitor;

import java.util.ArrayList;
import java.util.List;

public final class SchematicQueries {

    private SchematicQueries() {
        throw new AssertionError();
    }

    public static VecComponentNode findNode(VecConnectionSpecification connectionSpecification, String nodeId) {
        List<VecComponentNode> nodes = new ArrayList<>();
        connectionSpecification.accept(
                new TraversingVisitor<>(new DepthFirstTraverserImpl<>(), new BaseVisitor<>() {
                    @Override public Object visitVecComponentNode(final VecComponentNode node) {
                        if (nodeId.equals(node.getIdentification())) {
                            nodes.add(node);
                        }
                        return null;
                    }
                }
                ));
        if (nodes.isEmpty()) {
            throw new IllegalArgumentException("No ComponentNode exists with Identification='" + nodeId + "'.");
        }
        if (nodes.size() > 1) {
            throw new IllegalArgumentException(
                    "More than one ComponentNode exists with Identification='" + nodeId + "'.");
        }
        return nodes.get(0);
    }

    public static VecConnection findConnection(VecConnectionSpecification connectionSpecification,
                                               String connectionId) {
        return connectionSpecification.getConnections().stream().filter(c -> connectionId.equals(c.getIdentification()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(
                        "No Connection exists with Identification='" + connectionId + "'."));
    }

    public static VecComponentConnector findConnector(VecComponentNode node, String connectorId) {
        List<VecComponentConnector> connectors = node.getComponentConnectors().stream().filter(
                c -> connectorId.equals(c.getIdentification())).toList();

        if (connectors.isEmpty()) {
            throw new IllegalArgumentException(
                    "No ComponentConnector exists in " + node.getIdentification() + "with Identification='" +
                            connectorId + "'.");
        }
        if (connectors.size() > 1) {
            throw new IllegalArgumentException(
                    "More than one ComponentConnector exists in " + node.getIdentification() +
                            "with Identification='" +
                            connectorId + "'.");
        }
        return connectors.get(0);
    }

    public static VecComponentPort findPort(VecComponentConnector connector, String portId) {
        List<VecComponentPort> ports = connector.getComponentPorts().stream().filter(
                c -> portId.equals(c.getIdentification())).toList();

        if (ports.isEmpty()) {
            throw new IllegalArgumentException(
                    "No ComponentPort exists in " + connector.getIdentification() + "with Identification='" +
                            portId + "'.");
        }
        if (ports.size() > 1) {
            throw new IllegalArgumentException(
                    "More than one ComponentPort exists in " + connector.getIdentification() +
                            "with Identification='" +
                            portId + "'.");
        }
        return ports.get(0);
    }

    public static VecComponentPort findPort(VecConnectionSpecification connectionSpecification, String nodeId,
                                            String connectorId, String portId) {
        VecComponentNode node = findNode(connectionSpecification, nodeId);
        VecComponentConnector connector = findConnector(node, connectorId);
        return findPort(connector, portId);
    }
}
