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

import com.foursoft.harness.kbl.v25.KblExternalReference;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExternalReferenceDocumentVersionTransformerTest {

    private final ExternalReferenceDocumentVersionTransformer transformer =
            new ExternalReferenceDocumentVersionTransformer();

    @Test
    void should_transformExternalReference() {
        // Given
        final KblExternalReference source = externalReference("EKR", "drawing.pdf", "PDM", "Wolfsburg", "PDF");

        // When
        final VecDocumentVersion result = new TestConversionOrchestrator().transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("EKR", VecDocumentVersion::getDocumentType)
                .returns("4711", VecDocumentVersion::getDocumentNumber)
                .returns("A", VecDocumentVersion::getDocumentVersion)
                .returns("drawing.pdf", VecDocumentVersion::getFileName)
                .returns("PDM", VecDocumentVersion::getCreatingSystem)
                .returns("Wolfsburg", VecDocumentVersion::getLocation)
                .returns("PDF", VecDocumentVersion::getDataFormat);
    }

    @Test
    void should_omitThePlaceholder_inOptionalAttributesOnly() {
        // Given
        final KblExternalReference source = externalReference("/NULL", "/NULL", "/NULL", "/NULL", "/NULL");
        source.setDocumentNumber("/NULL");
        source.setChangeLevel("/NULL");

        // When
        final VecDocumentVersion result = new TestConversionOrchestrator().transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(null, VecDocumentVersion::getDocumentType)
                .returns(null, VecDocumentVersion::getFileName)
                .returns(null, VecDocumentVersion::getCreatingSystem)
                .returns(null, VecDocumentVersion::getLocation)
                .returns(null, VecDocumentVersion::getDataFormat)
                // Mandatory, the placeholder is kept.
                .returns("/NULL", VecDocumentVersion::getDocumentNumber)
                .returns("/NULL", VecDocumentVersion::getDocumentVersion);
    }

    private static KblExternalReference externalReference(final String documentType, final String fileName,
                                                          final String creatingSystem, final String location,
                                                          final String dataFormat) {
        final KblExternalReference source = new KblExternalReference();
        source.setDocumentType(documentType);
        source.setDocumentNumber("4711");
        source.setChangeLevel("A");
        source.setFileName(fileName);
        source.setCreatingSystem(creatingSystem);
        source.setLocation(location);
        source.setDataFormat(dataFormat);
        return source;
    }
}
