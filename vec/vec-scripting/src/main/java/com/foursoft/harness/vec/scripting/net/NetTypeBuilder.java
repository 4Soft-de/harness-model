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
import com.foursoft.harness.vec.scripting.enums.SignalInformationType;
import com.foursoft.harness.vec.scripting.enums.SignalSubType;
import com.foursoft.harness.vec.scripting.enums.SignalType;
import com.foursoft.harness.vec.v2x.VecNetType;

public class NetTypeBuilder implements Builder<VecNetType> {

    private final VecNetType netType = new VecNetType();

    public NetTypeBuilder(final String identification) {
        this.netType.setIdentification(identification);
    }

    @Override
    public VecNetType build() {

        return netType;
    }

    public NetTypeBuilder withSignalType(final SignalType signalType) {
        this.netType.setSignalType(signalType.value());
        return this;
    }

    public NetTypeBuilder withSignalInformationType(final SignalInformationType signalInformationType) {
        this.netType.setSignalInformationType(signalInformationType.value());
        return this;
    }

    public NetTypeBuilder withSignalSubType(final SignalSubType signalSubType) {
        this.netType.setSignalSubType(signalSubType.value());
        return this;
    }
}
