package com.foursoft.harness.vec.files;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class TestUtils {
    private TestUtils() {
        throw new AssertionError("TestUtils must not be instantiated.");
    }

    public static OutputStream createTestFileStream(String testcase) throws IOException {
        Path dir = FileSystems.getDefault().getPath(".", "target", "samples");

        Files.createDirectories(dir);

        return Files.newOutputStream(dir.resolve(testcase + ".vec"), StandardOpenOption.CREATE);
    }
}
