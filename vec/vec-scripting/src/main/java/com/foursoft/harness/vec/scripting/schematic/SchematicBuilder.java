package com.foursoft.harness.vec.scripting.schematic;

import com.foursoft.harness.vec.scripting.DefaultValues;
import com.foursoft.harness.vec.scripting.RootBuilder;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.core.DocumentVersionBuilder;
import com.foursoft.harness.vec.v2x.VecComponentNode;
import com.foursoft.harness.vec.v2x.VecConnectionSpecification;

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
