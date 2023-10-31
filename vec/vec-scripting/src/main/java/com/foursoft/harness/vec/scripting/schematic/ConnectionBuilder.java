package com.foursoft.harness.vec.scripting.schematic;

import com.foursoft.harness.vec.scripting.AbstractChildBuilder;
import com.foursoft.harness.vec.v2x.*;
import com.foursoft.harness.vec.v2x.visitor.BaseVisitor;
import com.foursoft.harness.vec.v2x.visitor.DepthFirstTraverserImpl;
import com.foursoft.harness.vec.v2x.visitor.TraversingVisitor;

import java.util.ArrayList;
import java.util.List;

public class ConnectionBuilder extends AbstractChildBuilder<SchematicBuilder> {

    private final VecConnectionSpecification connectionSpecification;
    private final VecConnection connection = new VecConnection();

    public ConnectionBuilder(final SchematicBuilder parent, VecConnectionSpecification connectionSpecification,
                             String identification) {
        super(parent);
        this.connectionSpecification = connectionSpecification;

        connection.setIdentification(identification);

        this.connectionSpecification.getConnections().add(connection);
    }

    public ConnectionBuilder addEnd(final String nodeId, final String connectorId, final String portId
    ) {
        return this.addEnd(nodeId, connectorId, portId, true);
    }

    public ConnectionBuilder addEnd(final String nodeId, final String connectorId, final String portId,
                                    boolean isExternal) {
        VecConnectionEnd end = new VecConnectionEnd();
        end.setIdentification(nodeId + "." + connectorId + "." + portId);
        this.connection.getConnectionEnds().add(end);

        PortPath path = find(nodeId, connectorId, portId);
        end.setConnectedComponentPort(path.port());
        return this;
    }

    private PortPath find(final String nodeId, final String connectorId, final String portId) {
        ComponentPortLocator locator = new ComponentPortLocator(nodeId, connectorId, portId);
        return locator.find();
    }

    private class ComponentPortLocator {

        private final String nodeId;
        private final String connectorId;
        private final String portId;

        ComponentPortLocator(final String nodeId, final String connectorId, final String portId) {
            this.nodeId = nodeId;
            this.connectorId = connectorId;
            this.portId = portId;
        }

        private VecComponentNode findNode() {
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

        private VecComponentConnector findConnector(VecComponentNode node) {
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

        private VecComponentPort findPort(VecComponentConnector connector) {
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

        public PortPath find() {
            VecComponentNode node = findNode();
            VecComponentConnector connector = findConnector(node);
            VecComponentPort port = findPort(connector);
            return new PortPath(node, connector, port);
        }

    }

    private record PortPath(VecComponentNode node, VecComponentConnector connector, VecComponentPort port) {
    }

}
