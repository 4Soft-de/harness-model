/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2025 4Soft GmbH
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
package com.foursoft.harness.vec.scripting.signals;

import com.foursoft.harness.vec.scripting.*;
import com.foursoft.harness.vec.scripting.net.NetSpecificationQueries;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecNetSpecification;
import com.foursoft.harness.vec.v2x.VecSignalSpecification;

public class SignalSpecificationBuilder implements Builder<VecSignalSpecification> {
    private final VecSignalSpecification signalSpecification = initializeSignalSpecification();
    private final VecSession session;
    private NetSpecificationQueries netQueries = new NetSpecificationQueries();

    public SignalSpecificationBuilder(final VecSession session) {
        this.session = session;
    }

    private VecSignalSpecification initializeSignalSpecification() {
        final VecSignalSpecification result = new VecSignalSpecification();
        result.setIdentification(DefaultValues.SIGNAL_SPEC_IDENTIFICATION);
        return result;
    }

    @Override
    public VecSignalSpecification build() {
        return signalSpecification;
    }

    public SignalSpecificationBuilder withNetworkLayer(final String networkDocumentNumber) {
        final VecDocumentVersion networkDocument = session.findDocument(networkDocumentNumber);
        netQueries = new NetSpecificationQueries(
                networkDocument.getSpecificationWithType(VecNetSpecification.class).orElseThrow(
                        () -> new VecScriptingException(
                                "No NetSpecification found in document with number: " + networkDocumentNumber)));
        return this;
    }

    public SignalSpecificationBuilder addSignal(final String identification,
                                                final Customizer<SignalBuilder> customizer) {
        final SignalBuilder builder = new SignalBuilder(identification, netQueries);

        customizer.customize(builder);

        this.signalSpecification.getSignals().add(builder.build());

        return this;
    }

}
