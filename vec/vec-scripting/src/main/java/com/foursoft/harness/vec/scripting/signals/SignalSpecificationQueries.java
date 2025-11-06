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

import com.foursoft.harness.vec.scripting.VecScriptingException;
import com.foursoft.harness.vec.v2x.VecSignal;
import com.foursoft.harness.vec.v2x.VecSignalSpecification;

public class SignalSpecificationQueries {

    private final VecSignalSpecification signalSpecification;

    public SignalSpecificationQueries() {
        signalSpecification = null;
    }

    public SignalSpecificationQueries(final VecSignalSpecification signalSpecification) {
        this.signalSpecification = signalSpecification;
    }

    private VecSignalSpecification signalSpecification() {
        if (signalSpecification == null) {
            throw new VecScriptingException("No ConnectionSpecification set for context.");
        }
        return signalSpecification;
    }

    public VecSignal findSignal(
            final String signalId) {
        return signalSpecification()
                .getSignals()
                .stream()
                .filter(c -> signalId.equals(c.getIdentification()))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("No Signal exists with Identification='" + signalId + "'."));
    }

}
