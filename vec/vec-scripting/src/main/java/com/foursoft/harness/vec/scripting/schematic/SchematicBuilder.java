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
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.v2x.VecConnectionSpecification;
import com.foursoft.harness.vec.v2x.VecReusageSpecification;

import java.util.ArrayList;
import java.util.List;

public class SchematicBuilder implements Builder<SchematicResult> {

    private final VecConnectionSpecification connectionSpecification;
    private final List<VecReusageSpecification> reusageSpecifications = new ArrayList<>();
    private final VecSession session;

    public SchematicBuilder(final VecSession session) {
        this.session = session;
        this.connectionSpecification = initializeConnectionSpecification();
    }

    @Override
    public SchematicResult build() {
        return new SchematicResult(this.connectionSpecification, reusageSpecifications);
    }

    private VecConnectionSpecification initializeConnectionSpecification() {
        final VecConnectionSpecification result = new VecConnectionSpecification();
        result.setIdentification(DefaultValues.CONNECTION_SPEC_IDENTIFICATION);
        return result;
    }

    public SchematicBuilder addComponentNodeReusage(final String schematicDocumentNumber,
                                                    final String connectionSpecification,
                                                    final String templateIdentification,
                                                    final String usageIdentification) {
        final VecConnectionSpecification templateSpecification = session.findDocument(schematicDocumentNumber)
                .getSpecificationWith(
                        VecConnectionSpecification.class, DefaultValues.CONNECTION_SPEC_IDENTIFICATION).orElseThrow();

        final VecReusageSpecification reusageSpecification = new VecReusageSpecification();
        reusageSpecification.setIdentification("RS-" + schematicDocumentNumber + "-" + connectionSpecification);
        reusageSpecifications.add(reusageSpecification);

        final VecConnectionSpecification fragment = new ComponentNodeReuseBuilder(templateSpecification,
                                                                                  reusageSpecification,
                                                                                  templateIdentification,
                                                                                  usageIdentification).build();

        this.connectionSpecification.getComponentNodes().addAll(fragment.getComponentNodes());
        this.connectionSpecification.getConnections().addAll(fragment.getConnections());
        this.connectionSpecification.getConnectionGroups().addAll(fragment.getConnectionGroups());

        return this;
    }

    public SchematicBuilder addComponentNode(final String identification,
                                             final Customizer<ComponentNodeBuilder> customizer) {
        final ComponentNodeBuilder builder = new ComponentNodeBuilder(identification);

        customizer.customize(builder);

        this.connectionSpecification.getComponentNodes().add(builder.build());

        return this;
    }

    public SchematicBuilder addConnection(final String identification, final Customizer<ConnectionBuilder> customizer) {
        final ConnectionBuilder builder = new ConnectionBuilder(
                ((nodeId, connectorId, portId) -> SchematicQueries.findPort(connectionSpecification, nodeId,
                                                                            connectorId, portId)), identification);

        customizer.customize(builder);

        this.connectionSpecification.getConnections().add(builder.build());

        return this;
    }

}
