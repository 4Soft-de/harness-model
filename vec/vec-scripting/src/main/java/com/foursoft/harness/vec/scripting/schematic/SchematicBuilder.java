package com.foursoft.harness.vec.scripting.schematic;

import com.foursoft.harness.vec.scripting.DefaultValues;
import com.foursoft.harness.vec.scripting.RootBuilder;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.core.DocumentVersionBuilder;
import com.foursoft.harness.vec.v2x.VecComponentNode;
import com.foursoft.harness.vec.v2x.VecConnectionSpecification;
import com.foursoft.harness.vec.v2x.visitor.BaseVisitor;
import com.foursoft.harness.vec.v2x.visitor.DepthFirstTraverserImpl;
import com.foursoft.harness.vec.v2x.visitor.TraversingVisitor;

import java.util.ArrayList;
import java.util.List;

public class SchematicBuilder implements RootBuilder {

    private final VecSession session;
    private final DocumentVersionBuilder container;
    private final VecConnectionSpecification connectionSpecification;

    public SchematicBuilder(final VecSession session, DocumentVersionBuilder container) {
        this.session = session;
        this.container = container;
        this.connectionSpecification = initializeCompositionSpecification();
    }

    private VecConnectionSpecification initializeCompositionSpecification() {
        VecConnectionSpecification connectionSpecification = new VecConnectionSpecification();
        connectionSpecification.setIdentification(DefaultValues.CONNECTION_SPEC_IDENTIFICATION);
        this.container.addSpecification(connectionSpecification);
        return connectionSpecification;
    }

    public ComponentNodeBuilder<SchematicBuilder> addComponentNode(String identification) {
        return new ComponentNodeBuilder<>(this, new Context(), identification);
    }

    public ConnectionBuilder addConnection(String identification) {
        return new ConnectionBuilder(this, this.connectionSpecification, identification);
    }

    public VecComponentNode node(String nodeId) {
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

    public VecConnectionSpecification getElement() {
        return this.connectionSpecification;
    }

    @Override public VecSession getSession() {
        return this.session;
    }

    @Override public void end() {

    }

    private class Context implements ComponentNodeBuilderContext {

        @Override public void addComponentNode(final VecComponentNode node) {
            connectionSpecification.getComponentNodes().add(node);
        }
    }

}
