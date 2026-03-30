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
package com.foursoft.harness.kbl2vec.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.foursoft.harness.kbl2vec.utils.ListUtils.getElementOrDefault;
import static org.assertj.core.api.Assertions.assertThat;

class ListUtilsTest {

    @Test
    void should_returnDefaultValue() {
        // Given
        final List<Double> elements = new ArrayList<>();
        final double defaultValue = 0.0;
        final int index = 0;

        // When
        final double result = getElementOrDefault(elements, index, defaultValue);

        // Then
        assertThat(result).isEqualTo(defaultValue);
    }

    @Test
    void should_returnElement() {
        // Given
        final List<Double> elements = new ArrayList<>();
        final double defaultValue = 0.0;
        final int index = 0;
        final double element = 1.0;
        elements.add(element);

        // When
        final double result = getElementOrDefault(elements, index, defaultValue);

        // Then
        assertThat(result).isEqualTo(element);
    }
}
