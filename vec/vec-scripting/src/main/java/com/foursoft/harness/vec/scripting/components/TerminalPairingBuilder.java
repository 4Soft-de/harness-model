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
package com.foursoft.harness.vec.scripting.components;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.factories.NumericalValueFactory;
import com.foursoft.harness.vec.v2x.VecIntegerValueProperty;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecTerminalPairing;
import com.foursoft.harness.vec.v2x.VecTerminalPairingSpecification;

import java.math.BigInteger;

public class TerminalPairingBuilder implements Builder<VecTerminalPairingSpecification> {

    private final VecSession session;
    private final VecTerminalPairingSpecification specification = new VecTerminalPairingSpecification();
    private final VecTerminalPairing pairing = new VecTerminalPairing();

    TerminalPairingBuilder(final VecSession session, final VecPartVersion firstPartNumber,
                           final VecPartVersion secondPartNumber) {
        this.session = session;

        specification.getTerminalPairings().add(pairing);
        pairing.setFirstTerminal(firstPartNumber);
        pairing.setSecondTerminal(secondPartNumber);
    }

    public TerminalPairingBuilder withMatingForce(final double force, final double lowerTolerance,
                                                  final double upperTolerance) {
        pairing.setMatingForce(
                NumericalValueFactory.valueWithTolerance(force, lowerTolerance, upperTolerance, session.newton()));
        return this;
    }

    public TerminalPairingBuilder withUnMatingForce(final double force, final double lowerTolerance,
                                                    final double upperTolerance) {
        pairing.setUnmatingForce(
                NumericalValueFactory.valueWithTolerance(force, lowerTolerance, upperTolerance, session.newton()));
        return this;
    }

    public TerminalPairingBuilder withNumberOfMatingCycles(final int numberOfMatingCycles) {
        final VecIntegerValueProperty value = new VecIntegerValueProperty();
        value.setValue(BigInteger.valueOf(numberOfMatingCycles));
        value.setPropertyType("numberOfMatingCycles");

        pairing.getCustomProperties().add(value);
        return this;
    }

    @Override public VecTerminalPairingSpecification build() {
        return this.specification;
    }
}
