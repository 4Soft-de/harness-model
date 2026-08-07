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
package com.foursoft.harness.vec.scripting.enums;

import com.foursoft.harness.vec.v2x.VecSignalType;
import com.foursoft.harness.vec.v2x.VecSignalTypeLiteral;

/**
 * @deprecated Use {@link VecSignalType}, which is generated from the literals the VEC schema defines for
 * this open enumeration, together with the {@link VecSignalTypeLiteral} it implements.
 */
@Deprecated(forRemoval = true)
public enum SignalType implements VecSignalTypeLiteral {

    /**
     * Replaced by {@link VecSignalType#INFORMATION}.
     */
    INFORMATION(VecSignalType.INFORMATION),

    /**
     * Replaced by {@link VecSignalType#ENERGY}.
     */
    ENERGY(VecSignalType.ENERGY),

    /**
     * Replaced by {@link VecSignalType#GROUND}.
     */
    GROUND(VecSignalType.GROUND);

    private final VecSignalTypeLiteral delegate;

    SignalType(final VecSignalTypeLiteral delegate) {
        this.delegate = delegate;
    }

    @Override
    public String value() {
        return delegate.value();
    }

}
