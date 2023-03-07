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