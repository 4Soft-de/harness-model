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

import com.foursoft.harness.kbl.v25.KblAliasIdentification;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecAliasIdentification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AliasIdentificationTransformerTest {

    @Test
    void should_transformAliasIdentification() {
        // Given
        final KblAliasIdentification source = new KblAliasIdentification();
        source.setAliasId("MF1");
        source.setScope("VOBES");
        source.setDescription("Alias-Ref");

        // When
        final VecAliasIdentification result = new TestConversionOrchestrator()
                .transform(new AliasIdentificationTransformer(), source);

        // Then
        assertThat(result).isNotNull()
                .returns("MF1", VecAliasIdentification::getIdentificationValue)
                .returns("VOBES", VecAliasIdentification::getScope)
                .returns("Alias-Ref", VecAliasIdentification::getType);
    }

    @Test
    void should_omitThePlaceholder_inOptionalAttributes() {
        // Given
        final KblAliasIdentification source = new KblAliasIdentification();
        source.setAliasId("MF1");
        source.setScope("/NULL");
        source.setDescription("/NULL");

        // When
        final VecAliasIdentification result = new TestConversionOrchestrator()
                .transform(new AliasIdentificationTransformer(), source);

        // Then
        assertThat(result).isNotNull()
                .returns(null, VecAliasIdentification::getScope)
                .returns(null, VecAliasIdentification::getType);
    }
}
