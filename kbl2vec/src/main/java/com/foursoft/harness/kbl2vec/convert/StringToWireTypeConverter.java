/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.convert;

import com.foursoft.harness.vec.v2x.VecWireType;
import com.google.common.base.Strings;

import java.util.Optional;

public class StringToWireTypeConverter implements Converter<String, Optional<VecWireType>> {

    private final String referenceSystem;

    public StringToWireTypeConverter(final String referenceSystem) {
        this.referenceSystem = referenceSystem;
    }

    @Override
    public Optional<VecWireType> convert(final String source) {
        if (Strings.isNullOrEmpty(source)) {
            return Optional.empty();
        }
        final VecWireType wireType = new VecWireType();
        wireType.setReferenceSystem(referenceSystem);
        wireType.setType(source);
        return Optional.of(wireType);
    }
}
