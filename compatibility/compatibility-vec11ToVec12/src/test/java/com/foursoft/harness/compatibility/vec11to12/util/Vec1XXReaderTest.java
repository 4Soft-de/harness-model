/*-
 * ========================LICENSE_START=================================
 * compatibility-vec11tovec12
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
import com.foursoft.harness.compatibility.vec11to12.common.VecVersion;
import com.foursoft.harness.compatibility.vec11to12.wrapper.AbstractBaseWrapperTest;
import com.foursoft.harness.vec.v113.VecContent;
import com.foursoft.harness.vec.v113.VecCreation;
import com.foursoft.harness.vec.v113.VecDocumentVersion;
import com.foursoft.harness.vec.v113.VecItemVersion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class Vec1XXReaderTest extends AbstractBaseWrapperTest {

    @Test
    void testCreateVec11xWithoutConversion() throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            final VecContent vec11x = Vec1XXReader.createVec11x(inputStream, "test stream");
            assertThat(vec11x).isNotNull()
                    .returns(VecVersion.VEC11X.getCurrentVersion(),
                             VecContent::getVecVersion);
            final List<VecCreation> creations = vec11x.getDocumentVersions().stream()
                    .map(VecItemVersion::getCreation)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            assertThat(creations).isNotEmpty();
        }
    }

    @Test
    void testCreateVec12xWithoutConversion() throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            final com.foursoft.harness.vec.v12x.VecContent vec12x =
                    Vec1XXReader.createVec12x(inputStream, "test stream");
            assertThat(vec12x).isNotNull()
                    .returns(VecVersion.VEC12X.getCurrentVersion(),
                             com.foursoft.harness.vec.v12x.VecContent::getVecVersion);
            final List<String> specificationIds = vec12x.getDocumentVersions().stream()
                    .map(com.foursoft.harness.vec.v12x.VecDocumentVersion::getSpecifications)
                    .flatMap(Collection::stream)
                    .map(com.foursoft.harness.vec.v12x.VecSpecification::getIdentification)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            assertThat(specificationIds).isNotEmpty();
        }
    }

    @Test
    void testConvertVec11To12WithWrite(@TempDir final Path tmpDir) throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            final com.foursoft.harness.vec.v12x.VecContent vecContent =
                    Vec1XXReader.createVec12x(inputStream, "test stream");
            InitializeFields.initializeFields(vecContent);

            assertThat(vecContent)
                    .returns(com.foursoft.harness.vec.v12x.Version.VERSION,
                             com.foursoft.harness.vec.v12x.VecContent::getVecVersion);

            final File file = new File(tmpDir.toFile(), "beetle_vec12x.vec");
            try (final FileOutputStream outputStream = new FileOutputStream(file)) {
                new com.foursoft.harness.vec.v12x.VecWriter().write(vecContent, outputStream);
            }

            try (final InputStream is = new BufferedInputStream(new FileInputStream(file))) {
                final com.foursoft.harness.vec.v12x.VecContent reReadVec12x =
                        Vec1XXReader.createVec12x(is, "test stream");
                assertThat(reReadVec12x)
                        .returns(com.foursoft.harness.vec.v12x.Version.VERSION,
                                 com.foursoft.harness.vec.v12x.VecContent::getVecVersion);
            }

            Files.delete(file.toPath()); // Has to be explicitly deleted, only an empty TempDir can be deleted.
        }
    }

    @Test
    void testRoundtrip() throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            final com.foursoft.harness.vec.v12x.VecContent vec12x = Vec1XXReader.createVec12x(inputStream, "stream_1");
            InitializeFields.initializeFields(vec12x);

            assertThat(vec12x)
                    .returns(com.foursoft.harness.vec.v12x.Version.VERSION,
                             com.foursoft.harness.vec.v12x.VecContent::getVecVersion);

            final VecContent vec11x = Vec1XXReader.createVec11x(vec12x, "stream_2");
            InitializeFields.initializeFields(vec11x);

            assertThat(vec11x)
                    .returns(com.foursoft.harness.vec.v113.Version.VERSION,
                             com.foursoft.harness.vec.v113.VecContent::getVecVersion);

            final com.foursoft.harness.vec.v12x.VecContent vec12x_2 = Vec1XXReader.createVec12x(vec11x, "stream_3");
            InitializeFields.initializeFields(vec12x_2);

            assertThat(vec12x_2)
                    .returns(com.foursoft.harness.vec.v12x.Version.VERSION,
                             com.foursoft.harness.vec.v12x.VecContent::getVecVersion);

            final VecContent vec11x_2 = Vec1XXReader.createVec11x(vec12x_2, "stream_4");
            InitializeFields.initializeFields(vec11x_2);

            assertThat(vec11x_2)
                    .returns(com.foursoft.harness.vec.v113.Version.VERSION,
                             com.foursoft.harness.vec.v113.VecContent::getVecVersion);

            final VecDocumentVersion actual = getDocumentVersion(vec11x, "id_docu_ver_11372");
            final VecDocumentVersion expected = getDocumentVersion(vec11x_2, "id_docu_ver_11372");
            assertThat(actual).isNotNull();
            assertThat(expected).isNotNull();

            assertThat(actual)
                    .usingRecursiveComparison()
                    .ignoringCollectionOrder()
                    .ignoringFieldsMatchingRegexes("(^.*parent.*$)",
                                                   "(^.*ref.*$)",
                                                   "(^.*xmlId.*$)",
                                                   "(^.*callback.*$)")
                    .isEqualTo(expected);
        }
    }

    private VecDocumentVersion getDocumentVersion(final VecContent vecContent, final String xmlId) {
        return vecContent.getDocumentVersions().stream()
                .filter(documentVersion -> documentVersion.getXmlId().equals(xmlId))
                .findFirst().orElse(null);
    }

}
