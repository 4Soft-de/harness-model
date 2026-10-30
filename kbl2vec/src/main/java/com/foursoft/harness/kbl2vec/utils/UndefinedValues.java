/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 - 2026 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.utils;

import com.google.common.base.Strings;

/**
 * Handles the placeholder for undefined values. Mandatory KBL attributes without a known value are filled with
 * {@value #PLACEHOLDER}. According to the VEC implementation guideline (general/default-values), the placeholder is
 * only written into mandatory VEC attributes, an optional VEC attribute is omitted instead.
 */
public final class UndefinedValues {

    public static final String PLACEHOLDER = "/NULL";

    private UndefinedValues() {
        // hide default constructor
    }

    /**
     * @param value Value of a KBL attribute.
     * @return {@code true} if the value is {@code null}, empty or the {@value #PLACEHOLDER} placeholder.
     */
    public static boolean isUndefined(final String value) {
        return Strings.isNullOrEmpty(value) || PLACEHOLDER.equals(value);
    }

    /**
     * Use this for optional VEC attributes, so that undefined values are omitted.
     *
     * @param value Value of a KBL attribute.
     * @return The value, or {@code null} if it is {@linkplain #isUndefined(String) undefined}.
     */
    public static String definedOrNull(final String value) {
        return isUndefined(value) ? null : value;
    }

}
