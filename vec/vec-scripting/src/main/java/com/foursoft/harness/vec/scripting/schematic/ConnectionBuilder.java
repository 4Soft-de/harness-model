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
import com.foursoft.harness.vec.v2x.VecComponentPort;
import com.foursoft.harness.vec.v2x.VecConnection;
import com.foursoft.harness.vec.v2x.VecConnectionEnd;

public class ConnectionBuilder implements Builder<VecConnection> {
    private final VecConnection connection = new VecConnection();
    private final ComponentPortLookup componentPortLookup;

    public ConnectionBuilder(ComponentPortLookup componentPortLookup,
                             String identification) {
        this.componentPortLookup = componentPortLookup;

        connection.setIdentification(identification);

    }

    public ConnectionBuilder addEnd(final String nodeId, final String connectorId, final String portId
    ) {
        return this.addEnd(nodeId, connectorId, portId, true);
    }

    public ConnectionBuilder addEnd(final String nodeId, final String connectorId, final String portId,
                                    boolean isExternal) {
        VecConnectionEnd end = new VecConnectionEnd();
        end.setIdentification(nodeId + "." + connectorId + "." + portId);
        this.connection.getConnectionEnds().add(end);

        VecComponentPort port = componentPortLookup.find(nodeId, connectorId, portId);
        end.setConnectedComponentPort(port);
        return this;
    }

    @Override public VecConnection build() {
        return connection;
    }

}
