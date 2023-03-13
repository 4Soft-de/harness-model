/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.1.X To VEC 1.2.X
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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
package com.foursoft.harness.compatibility.vec11to12.util;

import com.foursoft.harness.compatibility.vec11to12.TestFiles;
import com.foursoft.harness.vec.v12x.VecContent;
import com.foursoft.harness.vec.v12x.VecCreation;
import com.foursoft.harness.vec.v12x.VecItemVersion;
import com.foursoft.harness.vec.v12x.Version;
import jakarta.xml.bind.ValidationEvent;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultVecReaderTest {

    @Test
    void testReadVEC113() throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            final VecContent vecContent = DefaultVecReader.read(inputStream, "test stream");
            assertThat(vecContent).isNotNull()
                    .returns(Version.VERSION, VecContent::getVecVersion);
            final List<VecCreation> creations = vecContent.getDocumentVersions().stream()
                    .map(VecItemVersion::getCreation)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            assertThat(creations).isNotEmpty();
        }
    }

    @Test
    void multipleReads() throws Exception {
        final List<ValidationEvent> events1 = new ArrayList<>();
        final List<ValidationEvent> events2 = new ArrayList<>();
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            DefaultVecReader.read(inputStream, "test stream", events1::add);
            assertEquals(0, events1.size());
        }
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            DefaultVecReader.read(inputStream, "test stream", events2::add);
            assertEquals(0, events1.size());
            assertEquals(0, events2.size());
        }
    }

}
