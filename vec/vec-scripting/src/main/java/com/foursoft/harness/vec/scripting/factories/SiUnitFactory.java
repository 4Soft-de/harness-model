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
package com.foursoft.harness.vec.scripting.factories;

import com.foursoft.harness.vec.v2x.VecSIUnit;
import com.foursoft.harness.vec.v2x.VecSiPrefix;
import com.foursoft.harness.vec.v2x.VecSiUnitName;

import java.math.BigInteger;

public class SiUnitFactory {

    private final VecSIUnit siUnit = new VecSIUnit();

    private SiUnitFactory(final VecSiUnitName unitName, final VecSiPrefix prefix, final BigInteger exponent) {
        this(unitName, prefix);
        siUnit.setExponent(exponent);
    }

    private SiUnitFactory(final VecSiUnitName unitName, final VecSiPrefix prefix) {
        siUnit.setSiPrefix(prefix);
        siUnit.setSiUnitName(unitName);
    }

    private SiUnitFactory(final VecSiUnitName unitName) {
        siUnit.setSiUnitName(unitName);
    }

    public static VecSIUnit degreeCelsius() {
        return new SiUnitFactory(VecSiUnitName.DEGREE_CELSIUS).create();
    }

    private VecSIUnit create() {
        return siUnit;
    }

    public static VecSIUnit squareMM() {
        return new SiUnitFactory(VecSiUnitName.METRE, VecSiPrefix.MILLI, BigInteger.TWO).create();
    }

    public static VecSIUnit mm() {
        return new SiUnitFactory(VecSiUnitName.METRE, VecSiPrefix.MILLI).create();
    }

    public static VecSIUnit gram() {
        return new SiUnitFactory(VecSiUnitName.GRAM, null).create();
    }

    public static VecSIUnit mOhm() {
        return new SiUnitFactory(VecSiUnitName.OHM, VecSiPrefix.MILLI).create();
    }

    public static VecSIUnit ohm() {
        return new SiUnitFactory(VecSiUnitName.OHM).create();
    }

    public static VecSIUnit perMeter() {
        return new SiUnitFactory(VecSiUnitName.METRE, null, BigInteger.valueOf(-1)).create();
    }

    public static VecSIUnit newton() {
        return new SiUnitFactory(VecSiUnitName.NEWTON, null, null).create();
    }

    public static VecSIUnit ampere() {
        return new SiUnitFactory(VecSiUnitName.AMPERE).create();
    }

    public static VecSIUnit volts() {
        return new SiUnitFactory(VecSiUnitName.VOLT).create();
    }

}
