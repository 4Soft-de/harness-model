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
package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.KblCavitySeal;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecGeneralTechnicalPartSpecification;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CavitySealDocumentVersionTransformerTest {

    @Test
    void should_transformCavitySealDocumentVersion() {
        // Given
        final CavitySealDocumentVersionTransformer transformer = new CavitySealDocumentVersionTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCavitySeal source = new KblCavitySeal();
        source.setCompanyName("TestCompany");
        source.setPartNumber("TestPartNumber");
        source.setVersion("TestVersion");

        final VecPartVersion vecPartVersion = new VecPartVersion();
        final VecGeneralTechnicalPartSpecification vecGeneralTechnicalPartSpecification = new VecGeneralTechnicalPartSpecification();

        orchestrator.addMockMapping(source, vecPartVersion);
        orchestrator.addMockMapping(source, vecGeneralTechnicalPartSpecification);

        // When
        final VecDocumentVersion result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestCompany", VecDocumentVersion::getCompanyName)
                .returns("TestPartNumber", VecDocumentVersion::getDocumentNumber)
                .returns("TestVersion", VecDocumentVersion::getDocumentVersion)
                .satisfies(v -> assertThat(v.getReferencedPart()).containsExactly(vecPartVersion));
    }
}
