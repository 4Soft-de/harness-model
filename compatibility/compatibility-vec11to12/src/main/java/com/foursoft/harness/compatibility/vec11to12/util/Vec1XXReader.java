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

import com.foursoft.harness.compatibility.vec.common.VecCreator;
import com.foursoft.harness.compatibility.vec.common.VecProcessor;
import com.foursoft.harness.compatibility.vec11to12.Vec11XTo12XProcessor;

import java.io.InputStream;

/**
 * Class which is able to read and create any VEC 1.X.X.
 */
public final class Vec1XXReader {

    private static final VecProcessor CREATOR = new Vec11XTo12XProcessor();

    private Vec1XXReader() {
        //hide default constructor
    }

    /**
     * This method creates a VecContent in version 1.1.X from the given {@link InputStream}.
     *
     * @param inputStream InputStream of a VEC.
     * @param streamName  The name of the stream for logging.
     * @return The VecContent in version 1.1.X.
     */
    public static com.foursoft.harness.vec.v113.
            VecContent createVec11x(final InputStream inputStream, final String streamName) {
        return new VecCreator(CREATOR).createVec(inputStream, streamName,
                                                 com.foursoft.harness.vec.v113.VecContent.class);
    }

    /**
     * This method creates a VecContent in version 1.2.X from the given {@link InputStream}.
     *
     * @param inputStream InputStream of a VEC.
     * @param streamName  The name of the stream for logging.
     * @return The VecContent in version 1.2.X.
     */
    public static com.foursoft.harness.vec.v12x.
            VecContent createVec12x(final InputStream inputStream, final String streamName) {
        return new VecCreator(CREATOR).createVec(inputStream, streamName,
                                                 com.foursoft.harness.vec.v12x.VecContent.class);
    }

    /**
     * This method performs a downgrade for the given VecContent in version 1.2.X to version 1.1.X.
     *
     * @param vecContent The VecContent in version 1.2.X.
     * @param streamName The name of the stream for logging.
     * @return The VecContent in version 1.1.X.
     */
    public static com.foursoft.harness.vec.v113.VecContent createVec11x(
            final com.foursoft.harness.vec.v12x.VecContent vecContent, final String streamName) {
        return new VecCreator(CREATOR).createVec(vecContent, streamName,
                                                 com.foursoft.harness.vec.v113.VecContent.class);
    }

    /**
     * This method performs an upgrade for the given VecContent in version 1.1.X to version 1.2.X.
     *
     * @param vecContent The VecContent in version 1.1.X.
     * @param streamName The name of the stream for logging.
     * @return The VecContent in version 1.2.X.
     */
    public static com.foursoft.harness.vec.v12x.VecContent createVec12x(
            final com.foursoft.harness.vec.v113.VecContent vecContent, final String streamName) {
        return new VecCreator(CREATOR).createVec(vecContent, streamName,
                                                 com.foursoft.harness.vec.v12x.VecContent.class);
    }

}
