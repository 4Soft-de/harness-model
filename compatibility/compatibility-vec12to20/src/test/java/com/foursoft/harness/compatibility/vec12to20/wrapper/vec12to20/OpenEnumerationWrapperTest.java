/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.2.X To VEC 2.0.X
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
package com.foursoft.harness.compatibility.vec12to20.wrapper.vec12to20;

import com.foursoft.harness.compatibility.vec12to20.TestFiles;
import com.foursoft.harness.compatibility.vec12to20.util.DefaultVecReader;
import com.foursoft.harness.compatibility.vec12to20.wrapper.AbstractBaseWrapperTest;
import com.foursoft.harness.vec.v2x.VecContent;
import com.foursoft.harness.vec.v2x.VecDocumentType;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The typed accessors of an open enumeration keep working when a VEC 1.2.X document is read through
 * the VEC 2.X API, even though the two versions do not agree on which properties are open
 * enumerations at all.
 */
class OpenEnumerationWrapperTest extends AbstractBaseWrapperTest {

    @Test
    void literalsAreReadableThroughTheProxy() throws IOException {
        try (final InputStream input = TestFiles.getInputStream(TestFiles.OLD_BEETLE_V12X)) {
            final VecContent content = DefaultVecReader.read(input, "test");

            assertThat(content.getDocumentVersions()).isNotEmpty();

            for (final VecDocumentVersion documentVersion : content.getDocumentVersions()) {
                assertThat(documentVersion.getDocumentTypeLiteral())
                        .as("Literal of document type '%s'", documentVersion.getDocumentType())
                        .isNotNull()
                        .extracting("value")
                        .isEqualTo(documentVersion.getDocumentType());
            }

            assertThat(content.getDocumentVersions())
                    .extracting(VecDocumentVersion::getDocumentTypeLiteral)
                    .contains(VecDocumentType.HARNESS_DESCRIPTION);
        }
    }

}
