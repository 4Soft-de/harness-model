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
import com.foursoft.harness.vec.scripting.DefaultValues;
import com.foursoft.harness.vec.v2x.VecConnectionSpecification;

public class SchematicBuilder implements Builder<VecConnectionSpecification> {

    private final VecConnectionSpecification connectionSpecification;

    public SchematicBuilder() {
        this.connectionSpecification = initializeConnectionSpecification();
    }

    @Override
    public VecConnectionSpecification build() {
        return this.connectionSpecification;
    }

    private VecConnectionSpecification initializeConnectionSpecification() {
        VecConnectionSpecification connectionSpecification = new VecConnectionSpecification();
        connectionSpecification.setIdentification(DefaultValues.CONNECTION_SPEC_IDENTIFICATION);
        return connectionSpecification;
    }

    public SchematicBuilder addComponentNode(String identification, Customizer<ComponentNodeBuilder> customizer) {
        ComponentNodeBuilder builder = new ComponentNodeBuilder(identification);

        customizer.customize(builder);

        this.connectionSpecification.getComponentNodes().add(builder.build());

        return this;
    }

    public SchematicBuilder addConnection(String identification, Customizer<ConnectionBuilder> customizer) {
        ConnectionBuilder builder = new ConnectionBuilder(
                ((nodeId, connectorId, portId) -> SchematicQueries.findPort(connectionSpecification, nodeId,
                                                                            connectorId, portId)), identification);

        customizer.customize(builder);

        this.connectionSpecification.getConnections().add(builder.build());

        return this;
    }

}
