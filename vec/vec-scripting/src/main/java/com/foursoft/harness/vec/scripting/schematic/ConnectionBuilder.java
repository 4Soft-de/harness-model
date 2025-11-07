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
import com.foursoft.harness.vec.v2x.*;

import java.util.function.Function;

public class ConnectionBuilder implements Builder<VecConnection> {
    private final VecConnection connection = new VecConnection();
    private final ComponentPortLookup componentPortLookup;
    private final Function<String, VecNet> netLookup;
    private final Function<String, VecSignal> signalLookup;

    public ConnectionBuilder(final ComponentPortLookup componentPortLookup,
                             final String identification, final Function<String, VecNet> netLookup,
                             final Function<String, VecSignal> signalLookup) {
        this.componentPortLookup = componentPortLookup;
        this.netLookup = netLookup;
        this.signalLookup = signalLookup;

        connection.setIdentification(identification);

    }

    public ConnectionBuilder withNet(final String netIdentification) {
        connection.setNet(netLookup.apply(netIdentification));

        return this;
    }

    public ConnectionBuilder withSignal(final String signalIdentification) {
        connection.setSignal(signalLookup.apply(signalIdentification));

        return this;
    }

    public ConnectionBuilder addEnd(final String nodeId, final String connectorId, final String portId
    ) {
        return this.addEnd(nodeId, connectorId, portId, true);
    }

    public ConnectionBuilder addEnd(final String nodeId, final String connectorId, final String portId,
                                    final boolean isExternal) {
        final VecConnectionEnd end = new VecConnectionEnd();
        end.setIdentification(nodeId + "." + connectorId + "." + portId);
        end.setIsExternalEnd(isExternal);
        this.connection.getConnectionEnds().add(end);

        final VecComponentPort port = componentPortLookup.find(nodeId, connectorId, portId);
        end.setConnectedComponentPort(port);
        return this;
    }

    @Override
    public VecConnection build() {
        return connection;
    }

}
