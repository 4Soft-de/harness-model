package com.foursoft.harness.vec.scripting.schematic;

import com.foursoft.harness.vec.common.util.StringUtils;
import com.foursoft.harness.vec.scripting.AbstractChildBuilder;
import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.v2x.VecComponentConnector;
import com.foursoft.harness.vec.v2x.VecComponentNode;
import com.foursoft.harness.vec.v2x.VecComponentPort;

import java.util.HashMap;
import java.util.Map;

import static com.foursoft.harness.vec.scripting.factories.LocalizedStringFactory.en;

public class ComponentNodeBuilder<P extends Builder> extends AbstractChildBuilder<P> {

    private final ComponentNodeBuilderContext context;

    private VecComponentNode componentNode = new VecComponentNode();

    private final Map<String, VecComponentConnector> componentConnectors = new HashMap<>();

    public ComponentNodeBuilder(final P parent, ComponentNodeBuilderContext context, String identification) {
        super(parent);
        this.context = context;

        componentNode.setIdentification(identification);

        this.context.addComponentNode(componentNode);

    }

    public ComponentNodeBuilder<P> addPort(final String componentConnectorId, final String portId) {
        return this.addPort(componentConnectorId, portId, null);
    }

    public ComponentNodeBuilder<P> addPort(final String componentConnectorId, final String portId, String description) {
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

    private VecComponentConnector createSlot(final String identification) {
        final VecComponentConnector connector = new VecComponentConnector();
        connector.setIdentification(identification);
        componentNode.getComponentConnectors().add(connector);
        return connector;
    }

    public ComponentNodeBuilder<ComponentNodeBuilder<P>> addChildNode(String identification) {
        return new ComponentNodeBuilder<>(this, new Context(), identification);
    }

    private class Context implements ComponentNodeBuilderContext {

        @Override public void addComponentNode(final VecComponentNode node) {
            componentNode.getChildNodes().add(node);
        }
    }
}
