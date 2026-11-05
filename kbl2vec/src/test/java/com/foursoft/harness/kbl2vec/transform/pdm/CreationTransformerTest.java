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
package com.foursoft.harness.kbl2vec.transform.pdm;

import com.foursoft.harness.kbl.v25.KblCreation;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCreation;
import com.foursoft.harness.vec.v2x.VecPerson;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreationTransformerTest {

    private final CreationTransformer transformer = new CreationTransformer();

    @Test
    void should_transformCreation() {
        // Given
        final KblCreation source = new KblCreation();
        source.setName("Kyriazis");
        source.setDepartment("K-SIPE-3");
        source.setDate("270115");

        // When
        final VecCreation result = new TestConversionOrchestrator().transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("2015-01-27T00:00:00", v -> v.getCreationDate().toXMLFormat());
        assertThat(result.getCreator())
                .returns("Kyriazis", VecPerson::getLastName)
                .returns("K-SIPE-3", VecPerson::getDepartment);
    }

    @Test
    void should_notTransform_whenTheMandatoryDateCannotBeParsed() {
        // Given
        final KblCreation source = new KblCreation();
        source.setName("Kyriazis");
        source.setDate("2015-01-27");

        // When / Then
        assertThat(new TestConversionOrchestrator().transform(transformer, source)).isNull();
    }
}
