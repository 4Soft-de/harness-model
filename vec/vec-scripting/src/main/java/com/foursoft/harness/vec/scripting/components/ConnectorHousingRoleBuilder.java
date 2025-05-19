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
package com.foursoft.harness.vec.scripting.components;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.harness.HarnessQueries;
import com.foursoft.harness.vec.scripting.schematic.ComponentNodeLookup;
import com.foursoft.harness.vec.scripting.schematic.SchematicQueries;
import com.foursoft.harness.vec.v2x.*;

public class ConnectorHousingRoleBuilder implements Builder<VecConnectorHousingRole> {

    private final VecConnectorHousingRole connectorHousingRole;
    private final ComponentNodeLookup componentNodeLookup;

    public ConnectorHousingRoleBuilder(final String identification,
                                       final VecConnectorHousingSpecification specification,
                                       final ComponentNodeLookup componentNodeLookup) {
        this.componentNodeLookup = componentNodeLookup;
        this.connectorHousingRole = connectorHousingRole(identification, specification);

    }

    private VecConnectorHousingRole connectorHousingRole(final String identification,
                                                         final VecConnectorHousingSpecification specification) {

        final VecConnectorHousingRole role = new VecConnectorHousingRole();
        role.setIdentification(identification);

        role.setConnectorHousingSpecification(specification);

        role.getSlotReferences()
                .addAll(specification.getSlots()
                                .stream()
                                .map(this::toSlotReference)
                                .toList());

        return role;
    }

    public ConnectorHousingRoleBuilder withComponentConnector(final String componentNodeId,
                                                              final String componentConnectorId) {
        final VecComponentNode node = componentNodeLookup.find(componentNodeId);

        final VecComponentConnector connector = SchematicQueries.findConnector(node, componentConnectorId);

        this.connectorHousingRole.getComponentConnector().add(connector);

        return this;
    }

    /**
     * Can only be used if withComponentConnector has been called before.
     *
     * @param cavityNumber
     * @param portId
     * @return
     */
    public ConnectorHousingRoleBuilder withPort(final String cavityNumber, final String portId) {
        if (this.connectorHousingRole.getComponentConnector().isEmpty()) {
            throw new IllegalStateException("ComponentConnector has to be set first.");
        }
        final VecCavityReference cavityReference = HarnessQueries.findCavity(connectorHousingRole, cavityNumber);

        for (final VecComponentConnector connector : this.connectorHousingRole.getComponentConnector()) {
            final VecComponentPort port = SchematicQueries.findPort(connector, portId);
            cavityReference.getComponentPort().add(port);
        }
        return this;
    }

    @Override
    public VecConnectorHousingRole build() {
        return this.connectorHousingRole;
    }

    private VecSlotReference toSlotReference(final VecAbstractSlot abstractSlot) {
        final VecSlot slot = (VecSlot) abstractSlot;
        final VecSlotReference slotReference = new VecSlotReference();
        slotReference.setIdentification(slot.getSlotNumber());
        slotReference.setReferencedSlot(slot);
        slotReference.getCavityReferences()
                .addAll(slot.getCavities()
                                .stream()
                                .map(this::toCavityReference)
                                .toList());
        return slotReference;
    }

    private VecCavityReference toCavityReference(final VecCavity cavity) {
        final VecCavityReference cavityReference = new VecCavityReference();
        cavityReference.setIdentification(cavity.getCavityNumber());
        cavityReference.setReferencedCavity(cavity);
        return cavityReference;
    }
}
