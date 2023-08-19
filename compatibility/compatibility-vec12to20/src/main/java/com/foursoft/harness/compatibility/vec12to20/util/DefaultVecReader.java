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
package com.foursoft.harness.compatibility.vec12to20.util;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.common.EventConsumer;
import com.foursoft.harness.compatibility.vec12to20.common.VecVersion;
import com.foursoft.harness.compatibility.vec12to20.common.util.VecVersionDetector;
import com.foursoft.harness.vec.v2x.VecContent;
import com.foursoft.harness.vec.v2x.visitor.Visitable;
import jakarta.xml.bind.ValidationEvent;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Default VEC Reader which will create VEC 1.2.X {@link VecContent}s.
 * Can also take {@link InputStream}s of older VEC versions which will be converted to VEC 1.2.X.
 * <b>Important notice:</b> If the {@code VecContent} was converted, the fields will always be {@code null}.
 * When trying to call methods on the {@code VecContent} (e.g. {@code VecContent#getVecVersion}),
 * the value wil be calculated on the fly by
 * executing a corresponding wrapper (in this case: {@link Vec12To11ContentWrapper}).
 * <p>
 * If you want the fields to be set or if you need to write the {@code VecContent},
 * you have to call {@link InitializeFields#initializeFields(Visitable)}.
 * This will take some time though which is why this is not done by default.
 */
public final class DefaultVecReader {

    private DefaultVecReader() {
        // hide constructor
    }

    /**
     * Reads the given {@link InputStream} and creates a {@link VecContent} from it.
     * <p>
     * Uses a {@link EventConsumer} for validating {@link ValidationEvent}s.
     *
     * @param inputStream InputStream to create a VecContent from.
     * @param streamName  Name of the stream, used for logging.
     * @return A never-{@code null} VecContent.
     * @throws IOException In case the InputStream couldn't be processed or converted to a VecContent.
     */
    public static VecContent read(final InputStream inputStream, final String streamName) throws IOException {
        return read(inputStream, streamName, new EventConsumer());
    }

    /**
     * Reads the given {@link InputStream} and creates a {@link VecContent} from it.
     *
     * @param inputStream   InputStream to create a VecContent from.
     * @param streamName    Name of the stream, used for logging.
     * @param eventConsumer Consumer to run for {@link ValidationEvent}s. May not be {@code null}.
     * @return A never-{@code null} VecContent.
     * @throws IOException In case the InputStream couldn't be processed or converted to a VecContent.
     */
    public static VecContent read(final InputStream inputStream, final String streamName,
                                  final Consumer<ValidationEvent> eventConsumer) throws IOException {
        return read(inputStream, streamName, eventConsumer, null);
    }

    /**
     * Reads the given {@link InputStream} and creates a {@link VecContent} from it.
     *
     * @param inputStream                  InputStream to create a VecContent from.
     * @param streamName                   Name of the stream, used for logging.
     * @param eventConsumer                Consumer to run for {@link ValidationEvent}s. May not be {@code null}.
     * @param compatibilityContextConsumer Consumer for a {@link CompatibilityContext}. May be {@code null}.
     *                                     Can be used if the VEC needs to be converted.
     * @return A never-{@code null} VecContent.
     * @throws IOException In case the InputStream couldn't be processed or converted to a VecContent.
     */
    public static synchronized VecContent read(final InputStream inputStream,
                                               final String streamName,
                                               final Consumer<ValidationEvent> eventConsumer,
                                               final Consumer<CompatibilityContext> compatibilityContextConsumer)
            throws IOException {
        Objects.requireNonNull(inputStream);
        Objects.requireNonNull(eventConsumer);

        // Make sure the InputStream can be marked / reset.
        final InputStream streamToUse = inputStream.markSupported()
                ? inputStream
                : new BufferedInputStream(inputStream);

        final VecVersion vecVersion = VecVersionDetector.getVecVersion(streamToUse, streamName);
        return VecReaderFactory.createContent(vecVersion, streamToUse, eventConsumer, compatibilityContextConsumer);
    }

}
