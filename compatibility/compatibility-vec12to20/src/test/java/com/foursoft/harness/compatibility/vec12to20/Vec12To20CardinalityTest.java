/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.2.X To VEC 2.0.X
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
package com.foursoft.harness.compatibility.vec12to20;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.vec.v12x.VecBridgeTerminalRole;
import com.foursoft.harness.vec.v12x.VecCavityReference;
import com.foursoft.harness.vec.v12x.VecComponentConnector;
import com.foursoft.harness.vec.v12x.VecComponentNode;
import com.foursoft.harness.vec.v12x.VecComponentPort;
import com.foursoft.harness.vec.v12x.VecConnection;
import com.foursoft.harness.vec.v12x.VecConnectionEnd;
import com.foursoft.harness.vec.v12x.VecConnectionGroup;
import com.foursoft.harness.vec.v12x.VecEEComponentRole;
import com.foursoft.harness.vec.v12x.VecHousingComponentReference;
import com.foursoft.harness.vec.v12x.VecMatingDetail;
import com.foursoft.harness.vec.v12x.VecMatingPoint;
import com.foursoft.harness.vec.v12x.VecPinWireMappingPoint;
import com.foursoft.harness.vec.v12x.VecPinWireMappingSpecification;
import com.foursoft.harness.vec.v12x.VecTerminalRole;
import com.foursoft.harness.vec.v12x.VecWireElementReference;
import com.foursoft.harness.vec.v12x.VecWireEnd;
import com.foursoft.harness.vec.v12x.VecWireGrouping;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * VEC 2.x turned single references and single values of VEC 1.2 into lists ({@code IDREF} to {@code IDREFS},
 * {@code [0..1]} to {@code [0..*]}). The value of VEC 1.2 has to arrive as the one element of the list, and a
 * list of VEC 2.x read as VEC 1.2 has to yield its first element.
 */
class Vec12To20CardinalityTest {

    private final CompatibilityContext upgrade = new Vec12XTo20XCompatibilityWrapper().getContext();

    @Test
    void wireElementReferenceKeepsItsConnectionAndConnectionGroup() {
        final VecWireElementReference source = new VecWireElementReference();
        source.setConnection(connection("C1"));
        source.setConnectionGroup(connectionGroup("G1"));

        final com.foursoft.harness.vec.v2x.VecWireElementReference upgraded = proxy(source);

        assertThat(upgraded.getConnection())
                .extracting(com.foursoft.harness.vec.v2x.VecConnection::getIdentification)
                .containsExactly("C1");
        assertThat(upgraded.getConnectionGroup())
                .extracting(com.foursoft.harness.vec.v2x.VecConnectionGroup::getIdentification)
                .containsExactly("G1");
    }

    @Test
    void wireEndKeepsItsConnectionEnd() {
        final VecWireEnd source = new VecWireEnd();
        final VecConnectionEnd connectionEnd = new VecConnectionEnd();
        connectionEnd.setIdentification("E1");
        source.setConnectionEnd(connectionEnd);

        final com.foursoft.harness.vec.v2x.VecWireEnd upgraded = proxy(source);

        assertThat(upgraded.getConnectionEnd())
                .extracting(com.foursoft.harness.vec.v2x.VecConnectionEnd::getIdentification)
                .containsExactly("E1");
    }

    @Test
    void eeComponentRoleKeepsItsComponentNode() {
        final VecEEComponentRole source = new VecEEComponentRole();
        source.setComponentNode(componentNode("N1"));

        final com.foursoft.harness.vec.v2x.VecEEComponentRole upgraded = proxy(source);

        assertThat(upgraded.getComponentNode())
                .extracting(com.foursoft.harness.vec.v2x.VecComponentNode::getIdentification)
                .containsExactly("N1");
    }

    @Test
    void portReferencesKeepTheirComponentPort() {
        final VecTerminalRole terminalRole = new VecTerminalRole();
        terminalRole.setComponentPort(componentPort("P1"));
        final VecCavityReference cavityReference = new VecCavityReference();
        cavityReference.setComponentPort(componentPort("P2"));

        final com.foursoft.harness.vec.v2x.VecTerminalRole upgradedTerminalRole = proxy(terminalRole);
        final com.foursoft.harness.vec.v2x.VecCavityReference upgradedCavityReference = proxy(cavityReference);

        assertThat(upgradedTerminalRole.getComponentPort())
                .extracting(com.foursoft.harness.vec.v2x.VecComponentPort::getIdentification)
                .containsExactly("P1");
        assertThat(upgradedCavityReference.getComponentPort())
                .extracting(com.foursoft.harness.vec.v2x.VecComponentPort::getIdentification)
                .containsExactly("P2");
    }

    @Test
    void connectionReferencesKeepTheirConnection() {
        final VecBridgeTerminalRole bridgeTerminalRole = new VecBridgeTerminalRole();
        bridgeTerminalRole.setConnection(connection("C1"));
        final VecMatingPoint matingPoint = new VecMatingPoint();
        matingPoint.setConnection(connection("C2"));
        final VecMatingDetail matingDetail = new VecMatingDetail();
        matingDetail.setConnection(connection("C3"));

        final com.foursoft.harness.vec.v2x.VecBridgeTerminalRole upgradedBridge = proxy(bridgeTerminalRole);
        final com.foursoft.harness.vec.v2x.VecMatingPoint upgradedMatingPoint = proxy(matingPoint);
        final com.foursoft.harness.vec.v2x.VecMatingDetail upgradedMatingDetail = proxy(matingDetail);

        assertThat(upgradedBridge.getConnection())
                .extracting(com.foursoft.harness.vec.v2x.VecConnection::getIdentification)
                .containsExactly("C1");
        assertThat(upgradedMatingPoint.getConnection())
                .extracting(com.foursoft.harness.vec.v2x.VecConnection::getIdentification)
                .containsExactly("C2");
        assertThat(upgradedMatingDetail.getConnection())
                .extracting(com.foursoft.harness.vec.v2x.VecConnection::getIdentification)
                .containsExactly("C3");
    }

    @Test
    void housingComponentReferenceAndWireGroupingKeepTheirReference() {
        final VecHousingComponentReference housingComponentReference = new VecHousingComponentReference();
        final VecComponentConnector connector = new VecComponentConnector();
        connector.setIdentification("X1");
        housingComponentReference.setComponentConnector(connector);
        final VecWireGrouping wireGrouping = new VecWireGrouping();
        wireGrouping.setConnectionGroup(connectionGroup("G2"));

        final com.foursoft.harness.vec.v2x.VecHousingComponentReference upgradedReference =
                proxy(housingComponentReference);
        final com.foursoft.harness.vec.v2x.VecWireGrouping upgradedGrouping = proxy(wireGrouping);

        assertThat(upgradedReference.getComponentConnector())
                .extracting(com.foursoft.harness.vec.v2x.VecComponentConnector::getIdentification)
                .containsExactly("X1");
        assertThat(upgradedGrouping.getConnectionGroup())
                .extracting(com.foursoft.harness.vec.v2x.VecConnectionGroup::getIdentification)
                .containsExactly("G2");
    }

    @Test
    void renamedSingleValueBecomesTheOnlyElementOfTheList() {
        final VecPinWireMappingSpecification source = new VecPinWireMappingSpecification();
        final VecPinWireMappingPoint point = new VecPinWireMappingPoint();
        point.setXmlId("point");
        source.setPinWireMappingPoint(point);

        final com.foursoft.harness.vec.v2x.VecPinWireMappingSpecification upgraded = proxy(source);

        assertThat(upgraded.getPinWireMappingPoints())
                .extracting(com.foursoft.harness.vec.v2x.VecPinWireMappingPoint::getXmlId)
                .containsExactly("point");
    }

    @Test
    void theListIsTheSameModifiableInstanceOnEveryCall() {
        final VecWireElementReference source = new VecWireElementReference();
        source.setConnection(connection("C1"));
        final com.foursoft.harness.vec.v2x.VecWireElementReference upgraded = proxy(source);

        final List<com.foursoft.harness.vec.v2x.VecConnection> connections = upgraded.getConnection();
        connections.clear();

        assertThat(upgraded.getConnection()).isSameAs(connections).isEmpty();
    }

    @Test
    void aListReadAsSingleValueYieldsItsFirstElement() {
        final CompatibilityContext downgrade = new Vec12XTo20XCompatibilityWrapper().getContext();
        final com.foursoft.harness.vec.v2x.VecWireElementReference source =
                new com.foursoft.harness.vec.v2x.VecWireElementReference();
        source.getConnection().add(v2xConnection("C1"));
        source.getConnection().add(v2xConnection("C2"));

        final VecWireElementReference downgraded = downgrade.getWrapperProxyFactory().createProxy(source);

        assertThat(downgraded.getConnection().getIdentification()).isEqualTo("C1");
    }

    private <T> T proxy(final Object source) {
        return upgrade.getWrapperProxyFactory().createProxy(source);
    }

    private VecConnection connection(final String identification) {
        final VecConnection connection = new VecConnection();
        connection.setIdentification(identification);
        return connection;
    }

    private com.foursoft.harness.vec.v2x.VecConnection v2xConnection(final String identification) {
        final com.foursoft.harness.vec.v2x.VecConnection connection = new com.foursoft.harness.vec.v2x.VecConnection();
        connection.setIdentification(identification);
        return connection;
    }

    private VecConnectionGroup connectionGroup(final String identification) {
        final VecConnectionGroup group = new VecConnectionGroup();
        group.setIdentification(identification);
        return group;
    }

    private VecComponentNode componentNode(final String identification) {
        final VecComponentNode node = new VecComponentNode();
        node.setIdentification(identification);
        return node;
    }

    private VecComponentPort componentPort(final String identification) {
        final VecComponentPort port = new VecComponentPort();
        port.setIdentification(identification);
        return port;
    }

}
