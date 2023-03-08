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
package com.foursoft.harness.compatibility.vec11to12.common.util;

import com.foursoft.harness.compatibility.vec11to12.common.VecVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Class providing methods to detect a {@link VecVersion} from different resources such as Files or InputStreams.
 */
public class VecVersionDetector {

    private static final Logger LOGGER = LoggerFactory.getLogger(VecVersionDetector.class);

    /**
     * The amount of Bytes to be read initially.
     */
    private static final int INITIAL_AMOUNT_TO_READ = 1024;

    private VecVersionDetector() {
        //hide constructor
    }

    /**
     * Tries to get the {@link VecVersion} from the given {@link File}.
     *
     * @param file File which should be used to detect the VecVersion from.
     * @return Possibly-{@code null} VecVersion if no valid VEC Version could have been detected.
     * @throws IOException In case the file couldn't be found, opened or processed properly.
     */
    public static VecVersion getVecVersion(final File file) throws IOException {
        final InputStream is = new FileInputStream(file);
        final String name = file.getName();
        return getVecVersion(is, name);
    }

    /**
     * Tries to get the {@link VecVersion} from the given {@link InputStream}.
     * <p>
     * It will initially read {@link #INITIAL_AMOUNT_TO_READ} bytes from the Stream to detect the version.
     * If no version can be found, the Stream will be read again with the doubled amount of bytes.
     * This will be done until no bytes are left to be read.
     * <p>
     * In every case, the Stream will be reset, so it can be read again from its beginning.
     *
     * @param inputStream InputStream which should be used to detect the VecVersion from.
     * @param streamName  Name of the stream, used for logging.
     * @return Possibly-{@code null} VecVersion if no valid VEC Version could have been detected.
     * @throws IOException In case the InputStream couldn't be processed properly.
     */
    public static VecVersion getVecVersion(final InputStream inputStream, final String streamName) throws IOException {
        int bytesToRead = INITIAL_AMOUNT_TO_READ;
        byte[] byteArray = new byte[bytesToRead];

        // Make sure the InputStream can be marked / reset.
        final InputStream streamToUse = inputStream.markSupported()
                ? inputStream
                : new BufferedInputStream(inputStream);

        int readBytes = 0;
        final int available = streamToUse.available();  // Might not return the total amount of bytes though.

        // The beginning of the stream is marked as the point to jump back if InputStream#reset is called.
        // With that, the whole stream can be read again.
        // In addition to, it allows the available bytes to be read without the marker getting invalidated.
        // This is needed because else, InputStream#reset might throw an Exception.
        streamToUse.mark(available);

        VecVersion version = null;

        while ((readBytes = streamToUse.read(byteArray, 0, bytesToRead)) >= 0) {
            boolean leave = false;

            final String characters = new String(byteArray, StandardCharsets.UTF_8);
            final VecVersion vecVersion = getVecVersion(characters);
            if (vecVersion != null) {
                version = vecVersion;
                leave = true;
            }

            // Might have read the whole stream, exit then.
            if (readBytes < bytesToRead) {
                leave = true;
            }

            if (leave) {
                break;
            }

            // Double the amount of bytes which should be read.
            bytesToRead += bytesToRead;
            byteArray = new byte[bytesToRead];

            // Reset the stream, so it can be read again from the beginning.
            // This time the doubled amount of bytes will be read.
            streamToUse.reset();
        }

        streamToUse.reset(); // Reset the stream, so it can be read again from the beginning.

        if (version == null) {
            LOGGER.warn("Couldn't find a VEC Version in the given Stream {}.", streamName);
        }
        return version;
    }

    /**
     * Tries to get the {@link VecVersion} from the given String.
     *
     * @param fileContents Contents of a file which should be used to detect the VecVersion from.
     * @return Possibly-{@code null} VecVersion if no valid VEC Version could have been detected.
     */
    public static VecVersion getVecVersion(final String fileContents) {
        return VecVersion.findVersion(fileContents).orElse(null);
    }

}
