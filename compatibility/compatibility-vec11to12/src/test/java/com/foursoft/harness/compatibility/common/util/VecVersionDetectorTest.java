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
package com.foursoft.harness.compatibility.common.util;

import com.foursoft.harness.compatibility.vec11to12.TestFiles;
import com.foursoft.harness.compatibility.vec11to12.common.VecVersion;
import com.foursoft.harness.compatibility.vec11to12.common.util.VecVersionDetector;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

class VecVersionDetectorTest {

    @Test
    void testGetVecVersionEmptyFile() throws IOException {
        final File testFile = Files.createTempFile("empty-file_", ".vec").toFile();
        Assertions.assertThat(VecVersionDetector.getVecVersion(testFile)).isNull();
    }

    @Test
    void testGetVecVersionBigFile() throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream(
                "/vec12x/version-not-within-first-1024-bytes.vec")) {
            final VecVersion vecVersion = VecVersionDetector.getVecVersion(inputStream,
                                                                           "version-not-within-first-1024-bytes.vec");
            Assertions.assertThat(vecVersion).isEqualTo(VecVersion.VEC12X);
        }

        try (final InputStream inputStream = TestFiles.getInputStream("/version-not-in-file.vec")) {
            final VecVersion vecVersion = VecVersionDetector.getVecVersion(inputStream, "version-not-in-file.vec");
            Assertions.assertThat(vecVersion).isNull();
        }
    }

    @Test
    void testGetVecVersionWhereVersionIsNotExplicitlyDefined() throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream(
                "/vec12x/vec121-test.vec")) {
            final VecVersion vecVersion = VecVersionDetector.getVecVersion(inputStream, "vec121-test.vec");
            Assertions.assertThat(vecVersion).isEqualTo(VecVersion.VEC12X);
        }
    }

}
