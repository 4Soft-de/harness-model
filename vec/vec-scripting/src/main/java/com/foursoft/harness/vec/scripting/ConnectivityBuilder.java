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
package com.foursoft.harness.vec.scripting;

import com.foursoft.harness.vec.v2x.*;

public class ConnectivityBuilder extends AbstractChildBuilder<HarnessBuilder> {
    private final VecContactingSpecification contactingSpecification;
    private final VecWireElementReference wireElementReference;

    public ConnectivityBuilder(final HarnessBuilder harnessBuilder,
                               final VecContactingSpecification contactingSpecification,
                               final VecWireElementReference wireElementReference) {
        super(harnessBuilder);
        this.contactingSpecification = contactingSpecification;
        this.wireElementReference = wireElementReference;
    }

    public ConnectivityBuilder addEnd(final String connectorId, final String cavityNumber) {
        final VecContactPoint cp = createContactPointWithWireMounting(connectorId + "." + cavityNumber);

        final VecCavityMounting cavityMounting = new VecCavityMounting();
        cp.getCavityMountings()
                .add(cavityMounting);

        final VecConnectorHousingRole connector = this.parent
                .occurrence(connectorId)
                .getRoleWithType(VecConnectorHousingRole.class)
                .orElseThrow();

        final VecCavityReference cavityReference = connector.getSlotReferences()
                .stream()
                .map(VecSlotReference.class::cast)
                .flatMap(s -> s.getCavityReferences()
                        .stream())
                .filter(c -> c.getIdentification()
                        .equals(cavityNumber))
                .findAny()
                .orElseThrow();
        cavityMounting.getEquippedCavityRef()
                .add(cavityReference);

        return this;
    }

    public ConnectivityBuilder addEndWithTerminalOnly(final String terminalId) {
        final VecContactPoint cp = createContactPointWithWireMounting(
                wireElementReference.getIdentification() + "-" + terminalId);

        final VecPluggableTerminalRole terminal = this.parent
                .occurrence(terminalId)
                .getRoleWithType(VecPluggableTerminalRole.class)
                .orElseThrow();

        cp.setMountedTerminal(terminal);

        return this;
    }

    private VecContactPoint createContactPointWithWireMounting(final String endpointId) {
        final VecContactPoint cp = new VecContactPoint();
        cp.setIdentification(endpointId);
        contactingSpecification.getContactPoints()
                .add(cp);

        final VecWireEnd end = new VecWireEnd();
        end.setIdentification(endpointId);
        if (wireElementReference
                .getWireEnds()
                .isEmpty()) {
            end.setPositionOnWire(0.0);
        } else {
            end.setPositionOnWire(1.0);
        }
        wireElementReference.getWireEnds()
                .add(end);

        final VecWireMounting wireMounting = new VecWireMounting();

        cp.getWireMountings()
                .add(wireMounting);
        wireMounting.getReferencedWireEnd()
                .add(end);
        return cp;
    }

}
