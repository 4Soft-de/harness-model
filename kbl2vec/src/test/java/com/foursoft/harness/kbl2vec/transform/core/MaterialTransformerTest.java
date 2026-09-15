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
package com.foursoft.harness.kbl2vec.transform.core;

import com.foursoft.harness.kbl.v25.KblMaterial;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecMaterial;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class MaterialTransformerTest {

    private final MaterialTransformer transformer = new MaterialTransformer();

    @Test
    void should_transformMaterial() {
        // Given
        final KblMaterial source = new KblMaterial();
        source.setMaterialKey("PA66");
        source.setMaterialReferenceSystem("ISO 1043");

        // When
        final VecMaterial result = new TestConversionOrchestrator().transform(transformer, source);

        // Then
        assertThat(result)
                .isNotNull()
                .returns("PA66", VecMaterial::getKey)
                .returns("ISO 1043", VecMaterial::getReferenceSystem);
    }

    @Test
    void should_transformMaterial_whenReferenceSystemIsMissing() {
        // Given
        final KblMaterial source = new KblMaterial();
        source.setMaterialKey("PA66");

        // When
        final VecMaterial result = new TestConversionOrchestrator().transform(transformer, source);

        // Then
        assertThat(result)
                .isNotNull()
                .returns("PA66", VecMaterial::getKey)
                .returns(null, VecMaterial::getReferenceSystem);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void should_notTransform_whenMaterialKeyIsBlank(final String blankKey) {
        // Given
        final KblMaterial source = new KblMaterial();
        source.setMaterialKey(blankKey);
        source.setMaterialReferenceSystem("ISO 1043");

        // When
        final VecMaterial result = new TestConversionOrchestrator().transform(transformer, source);

        // Then
        assertThat(result).isNull();
    }
}
