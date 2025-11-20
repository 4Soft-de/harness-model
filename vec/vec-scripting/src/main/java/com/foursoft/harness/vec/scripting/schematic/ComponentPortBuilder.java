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
package com.foursoft.harness.vec.scripting.schematic;

import com.foursoft.harness.vec.common.util.StringUtils;
import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.v2x.VecComponentPort;
import com.foursoft.harness.vec.v2x.VecNetworkPort;
import com.foursoft.harness.vec.v2x.VecSignal;

import java.util.function.Function;

import static com.foursoft.harness.vec.scripting.factories.LocalizedStringFactory.en;

public class ComponentPortBuilder implements Builder<VecComponentPort> {

    private final VecComponentPort componentPort = new VecComponentPort();
    private final Function<String, VecNetworkPort> networkPortLookup;
    private final Function<String, VecSignal> signalLookup;

    public ComponentPortBuilder(final String identification, final Function<String, VecNetworkPort> networkPortLookup,
                                final Function<String, VecSignal> signalLookup) {
        this.networkPortLookup = networkPortLookup;
        this.signalLookup = signalLookup;
        componentPort.setIdentification(identification);
    }

    public ComponentPortBuilder withDescription(final String description) {
        if (StringUtils.isNotEmpty(description)) {
            componentPort.getDescriptions().add(en(description));
        }

        return this;
    }

    public ComponentPortBuilder withSignal(final String signalIdentification) {
        componentPort.setSignal(signalLookup.apply(signalIdentification));
        return this;
    }

    public ComponentPortBuilder withNetworkPort(final String identification) {
        componentPort.setNetworkPort(networkPortLookup.apply(identification));
        return this;
    }

    @Override
    public VecComponentPort build() {
        return componentPort;
    }
}
