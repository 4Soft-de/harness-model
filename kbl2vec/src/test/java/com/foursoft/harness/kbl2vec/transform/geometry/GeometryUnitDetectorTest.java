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
package com.foursoft.harness.kbl2vec.transform.geometry;

import com.foursoft.harness.kbl.v25.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GeometryUnitDetectorTest {

    @Test
    void should_getBaseUnit() {
        // Given
        final KblHarness source = new KblHarness();

        final KBLContainer container = new KBLContainer();
        source.setParentKBLContainer(container);

        final KblNumericalValue length = new KblNumericalValue();
        final KblUnit expectedUnit = new KblUnit();
        length.setUnitComponent(expectedUnit);

        final KblSegment segment = new KblSegment();
        segment.setPhysicalLength(length);
        container.getSegments().add(segment);

        // When
        final KblUnit result = GeometryUnitDetector.getUnit(source);

        // Then
        assertThat(result).isEqualTo(expectedUnit);
    }

    @Test
    void should_returnNull() {
        // Given
        final KblHarness source = new KblHarness();

        final KBLContainer container = new KBLContainer();
        source.setParentKBLContainer(container);

        final KblSegment segment = new KblSegment();
        container.getSegments().add(segment);

        // When
        final KblUnit result = GeometryUnitDetector.getUnit(source);

        // Then
        assertThat(result).isNull();
    }
}
