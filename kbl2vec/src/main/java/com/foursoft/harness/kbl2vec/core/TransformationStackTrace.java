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
package com.foursoft.harness.kbl2vec.core;

import org.apache.commons.lang3.ArrayUtils;

public record TransformationStackTrace(TransformationStackTraceElement<?, ?>[] elements) {

    public static TransformationStackTrace empty() {
        return new TransformationStackTrace(new TransformationStackTraceElement[0]);
    }

    public <S, D> TransformationStackTrace addElement(final S sourceObject, final D targetObject,
                                                      final Transformer<S, D> usedTransformer) {
        final TransformationStackTraceElement<S, D> newElement = new TransformationStackTraceElement<>(sourceObject,
                                                                                                       targetObject,
                                                                                                       usedTransformer);
        return new TransformationStackTrace(ArrayUtils.add(this.elements, newElement));
    }

    @Override
    public String toString() {
        String result = "";

        for (int i = 0; i < elements.length; i++) {
            result += "  ".repeat(i) + (i + 1) + ". " + elements[i] + "\n";
        }

        return result;
    }
}
