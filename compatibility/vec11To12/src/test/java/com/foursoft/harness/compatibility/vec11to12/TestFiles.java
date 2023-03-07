package com.foursoft.harness.compatibility.vec11to12;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Objects;

public final class TestFiles {

    public static final String OLD_BEETLE = "/vec11x/oldbeetle_vec113.vec";

    private TestFiles() {
        // hide constructor
    }

    public static InputStream getInputStream(final String path) {
        final InputStream resourceAsStream = TestFiles.class.getResourceAsStream(path);
        Objects.requireNonNull(resourceAsStream, "Couldn't get resource " + path);
        return new BufferedInputStream(resourceAsStream);
    }

}
