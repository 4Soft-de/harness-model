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
package com.foursoft.harness.vec.scripting.net;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.scripting.DefaultValues;
import com.foursoft.harness.vec.v2x.VecNetSpecification;

public class NetSpecificationBuilder implements Builder<VecNetSpecification> {

    private final VecNetSpecification netSpecification = initializeNetSpecification();
    private final NetSpecificationQueries queries = new NetSpecificationQueries(netSpecification);

    @Override
    public VecNetSpecification build() {

        return netSpecification;
    }

    private VecNetSpecification initializeNetSpecification() {
        final VecNetSpecification result = new VecNetSpecification();
        result.setIdentification(DefaultValues.NETWORK_SPEC_IDENTIFICATION);
        return result;
    }

    public NetSpecificationBuilder addNetworkNode(final String identification,
                                                  final Customizer<NetworkNodeBuilder> customizer) {
        final NetworkNodeBuilder builder = new NetworkNodeBuilder(queries::findNetType, identification);

        customizer.customize(builder);

        this.netSpecification.getNetworkNodes().add(builder.build());

        return this;
    }

    public NetSpecificationBuilder addNetType(final String identification,
                                              final Customizer<NetTypeBuilder> customizer) {
        final NetTypeBuilder builder = new NetTypeBuilder(identification);

        customizer.customize(builder);

        this.netSpecification.getNetTypes().add(builder.build());

        return this;
    }

    public NetSpecificationBuilder addNet(final String identification,
                                          final Customizer<NetBuilder> customizer) {
        final NetBuilder builder = new NetBuilder(queries::findPort, queries::findNetType, identification);

        customizer.customize(builder);

        this.netSpecification.getNets().add(builder.build());

        return this;
    }

}
