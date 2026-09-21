/*-
 * ========================LICENSE_START=================================
 * VEC 1.1.3
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
package com.foursoft.harness.vec.v113;

import com.foursoft.harness.vec.common.openenum.CustomOpenEnumLiteral;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The behaviour of the open enumeration literals on the model itself: what a document containing an
 * unrecognized literal reads back as, and that such a document can be written in the first place.
 */
class OpenEnumerationTest {

    @Test
    void literalsAgreeWithTheStringsOfTheSampleDocument() {
        final VecContent content = new VecReader().read(TestFiles.getInputStream(TestFiles.SAMPLE_VEC));

        assertThat(content.getDocumentVersions()).isNotEmpty();

        for (final VecDocumentVersion documentVersion : content.getDocumentVersions()) {
            final VecDocumentTypeLiteral literal = documentVersion.getDocumentTypeLiteral();
            if (documentVersion.getDocumentType() == null) {
                assertThat(literal).isNull();
            } else {
                assertThat(literal).isNotNull();
                assertThat(literal.value()).isEqualTo(documentVersion.getDocumentType());
            }
        }
    }

    @Test
    void nullMeansUnsetAndNothingElse() {
        final VecDocumentVersion documentVersion = new VecDocumentVersion();
        assertThat(documentVersion.getDocumentTypeLiteral()).isNull();

        documentVersion.setDocumentTypeLiteral(VecDocumentType.PART_MASTER);
        assertThat(documentVersion.getDocumentType()).isEqualTo("PartMaster");

        documentVersion.setDocumentTypeLiteral(null);
        assertThat(documentVersion.getDocumentType()).isNull();
        assertThat(documentVersion.getDocumentTypeLiteral()).isNull();
    }

    @Test
    void aCustomLiteralSurvivesAWriteAndReadRoundTrip() {
        final VecContent written = new VecReader().read(TestFiles.getInputStream(TestFiles.SAMPLE_VEC));
        written.getDocumentVersions()
                .get(0)
                .setDocumentTypeLiteral(new VecDocumentTypeLiteral.Custom("AcmeInternalDocumentType"));

        final String xml = new VecWriter().writeToString(written);
        assertThat(xml).contains("AcmeInternalDocumentType");

        // Reading must not fail on the unrecognized literal: the mapped property is a plain string.
        final VecContent read = new VecReader().read(
                new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        final VecDocumentVersion reread = read.getDocumentVersions()
                .get(0);

        assertThat(reread.getDocumentType()).isEqualTo("AcmeInternalDocumentType");
        assertThat(reread.getDocumentTypeLiteral()).isInstanceOf(CustomOpenEnumLiteral.class);
    }

}
