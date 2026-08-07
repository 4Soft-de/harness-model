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

import com.foursoft.harness.vec.v2x.VecTemperatureType;
import com.foursoft.harness.vec.v2x.VecTemperatureTypeLiteral;

/**
 * Temperature types used by this API that VEC 2.2.0 does not define.
 *
 * <p>
 * {@code TemperatureType} is an open enumeration, so a document may use literals beyond the ones the
 * standard recommends. This enum is how that is done in a typesafe way: it implements the same
 * {@link VecTemperatureTypeLiteral} the generated {@link VecTemperatureType} implements, so its
 * constants can be passed wherever a temperature type literal is expected. Registering them with
 * {@link ScriptingOpenEnumLiterals} additionally makes a document that uses them read back as these
 * constants instead of as anonymous custom literals.
 * </p>
 *
 * <p>
 * A document containing such a literal does not validate against the strict VEC schema. That is the
 * point of the strict schema, not a defect: it is the schema that checks whether a document sticks
 * to the recommended literals.
 * </p>
 */
public enum AdditionalTemperatureType implements VecTemperatureTypeLiteral {

    /**
     * The temperature a component withstands for a short period of time, used by wire data sheets.
     */
    SHORT_TERM_AGING_TEMPERATURE("ShortTermAgingTemperature");

    private final String value;

    AdditionalTemperatureType(final String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

}
