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
package com.foursoft.harness.vec.scripting.harness;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.v2x.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ConnectivityBuilder implements Builder<List<VecContactPoint>> {
    private final VecSession session;
    private final VecWireElementReference wireElementReference;
    private final Function<String, VecPartOccurrence> occurrenceLocator;
    private final List<VecContactPoint> createdContactPoints = new ArrayList<>();

    public ConnectivityBuilder(VecSession session, final VecWireElementReference wireElementReference,
                               Function<String, VecPartOccurrence> occurrenceLocator) {
        this.session = session;
        this.wireElementReference = wireElementReference;
        this.occurrenceLocator = occurrenceLocator;
    }

    public ConnectivityBuilder addEnd(final String connectorId, final String cavityNumber) {
        final VecContactPoint cp = createContactPointWithWireMounting(connectorId + "." + cavityNumber, we -> {
        }, null);

        final VecCavityMounting cavityMounting = new VecCavityMounting();
        cp.getCavityMountings()
                .add(cavityMounting);

        final VecConnectorHousingRole connector = occurrenceLocator.apply(connectorId)
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

    public ConnectivityBuilder addEndWithTerminalOnly(final String terminalId,
                                                      Customizer<WireEndBuilder> wireEndCustomizer,
                                                      Customizer<WireMountingDetailBuilder> wireMountingDetailBuilderCustomizer) {
        final VecContactPoint cp = createContactPointWithWireMounting(
                wireElementReference.getIdentification() + "-" + terminalId, wireEndCustomizer,
                wireMountingDetailBuilderCustomizer);

        final VecPluggableTerminalRole terminal = occurrenceLocator.apply(terminalId)
                .getRoleWithType(VecPluggableTerminalRole.class)
                .orElseThrow();

        cp.setMountedTerminal(terminal);

        terminal.getWireReceptionReferences().forEach(wrRef -> {
            cp.getWireMountings().stream().flatMap(wm -> wm.getWireMountingDetails().stream()).forEach(
                    wmd -> wmd.setContactedWireReception(wrRef));
        });

        return this;
    }

    @Override public List<VecContactPoint> build() {
        return createdContactPoints;
    }

    private VecContactPoint createContactPointWithWireMounting(final String endpointId,
                                                               Customizer<WireEndBuilder> wireEndCustomizer,
                                                               Customizer<WireMountingDetailBuilder> wireMountingDetailCustomizer) {
        final VecContactPoint cp = new VecContactPoint();
        cp.setIdentification(endpointId);
        createdContactPoints.add(cp);

        WireEndBuilder wireEndBuilder = new WireEndBuilder(this.session);

        wireEndCustomizer.customize(wireEndBuilder);

        final VecWireEnd end = wireEndBuilder.build();
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

        if (wireMountingDetailCustomizer != null) {
            final WireMountingDetailBuilder wireMountingDetailBuilder = new WireMountingDetailBuilder(this.session);
            wireMountingDetailCustomizer.customize(wireMountingDetailBuilder);
            final VecWireMountingDetail detail = wireMountingDetailBuilder.build();

            wireMounting.getWireMountingDetails().add(detail);
            detail.getReferencedWireEnd().add(end);
        }

        return cp;
    }

}
