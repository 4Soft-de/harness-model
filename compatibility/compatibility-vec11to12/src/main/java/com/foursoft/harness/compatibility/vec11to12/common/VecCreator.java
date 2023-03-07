/*-
 * ========================LICENSE_START=================================
 * compatibility-vec11to12
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
package com.foursoft.harness.compatibility.vec11to12.common;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.compatibility.core.common.EventConsumer;
import com.foursoft.harness.compatibility.vec11to12.common.util.VecVersionDetector;
import com.foursoft.harness.vec.common.HasVecVersion;
import com.foursoft.harness.vec.common.exception.VecException;

import javax.xml.bind.ValidationEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Class which creates specific VECs.
 * If needed (and possible), the actual version will be converted to the requested one.
 */
public final class VecCreator {

    private static final Map<String, VecVersion> CLASS_PATH_TO_VEC_VERSION_MAP = Map.ofEntries(
            Map.entry("com.foursoft.harness.vec.v113.VecContent", VecVersion.VEC11X),
            Map.entry("com.foursoft.harness.vec.v12x.VecContent", VecVersion.VEC12X)
    );

    private final VecProcessor processor;
    private Consumer<ValidationEvent> validationEventConsumer;
    private Consumer<Context> compatibilityContextConsumer;

    /**
     * Creates a new instance using a {@link VecProcessor}.
     *
     * @param processor VecProcessor to use for creating the VEC.
     */
    public VecCreator(final VecProcessor processor) {
        this.processor = processor;
    }

    /**
     * Sets the consumer to run for {@link ValidationEvent}s.
     *
     * @param validationEventConsumer Consumer to run for {@link ValidationEvent}s.
     * @return The current creator, useful for chaining.
     */
    public VecCreator withValidationEventConsumer(final Consumer<ValidationEvent> validationEventConsumer) {
        this.validationEventConsumer = validationEventConsumer;
        return this;
    }

    /**
     * Sets the consumer to run for a {@link CompatibilityContext}.
     *
     * @param compatibilityContextConsumer Consumer for a {@link CompatibilityContext}.
     *                                     Can be used if the VEC needs to be converted.
     * @return The current creator, useful for chaining.
     */
    public VecCreator withCompatibilityContextConsumer(
            final Consumer<Context> compatibilityContextConsumer) {
        this.compatibilityContextConsumer = compatibilityContextConsumer;
        return this;
    }

    /**
     * Reads the given {@link InputStream} and creates a VEC of the given target class from it.
     *
     * @param inputStream InputStream to create a VecContent from.
     * @param streamName  Name of the stream, used for logging.
     * @param targetClass Target VEC class.
     * @param <T>         Target type.
     * @return A never-{@code null} VecContent.
     * @throws VecException In case anything went wrong when trying to create the VEC.
     *                      Examples: The source / target VEC Version can't be detected or
     *                      the given InputStream cannot be converted to the target VEC Version.
     */
    public <T extends HasVecVersion> T createVec(final InputStream inputStream,
                                                 final String streamName,
                                                 final Class<T> targetClass) {
        return createVecContentFromStream(inputStream, streamName, targetClass);
    }

    /**
     * Reads the given {@link HasVecVersion VecContent} and creates a VEC of the given target class from it.
     *
     * @param vecContent  VecContent to create a VecContent from.
     * @param streamName  Name of the stream, used for logging
     * @param targetClass Target VEC class.
     * @param <T>         Target type.
     * @return A never-{@code null} VecContent.
     * @throws VecException In case anything went wrong when trying to create the VEC.
     *                      Examples: The source / target VEC Version can't be detected or
     *                      the given VecContent cannot be converted to the target VEC Version.
     */
    public <T extends HasVecVersion> T createVec(final HasVecVersion vecContent,
                                                 final String streamName,
                                                 final Class<T> targetClass) {
        return createContentFromVecContent(vecContent, streamName, targetClass);
    }

    private <T extends HasVecVersion, V extends HasVecVersion> V createContentFromVecContent(final T content,
                                                                                             final String streamName,
                                                                                             final Class<V> targetClass) {
        Objects.requireNonNull(content, "The VecContent may not be null.");

        final VecVersion vecVersion = VecVersion.findVersion(content).orElse(null);
        if (vecVersion == null) {
            final String errorMsg = String.format("VEC Version couldn't be detected from %s.", streamName);
            throw new VecException(errorMsg);
        }

        final VecVersion targetVecVersion = CLASS_PATH_TO_VEC_VERSION_MAP.get(targetClass.getName());
        if (targetVecVersion == null) {
            final String errorMsg = String.format("VEC Conversion for %s is not supported yet.", targetClass);
            throw new VecException(errorMsg);
        }

        if (vecVersion != targetVecVersion && !vecVersion.canBeConvertedTo(targetVecVersion)) {
            final String errorMsg = String.format("VEC %s cannot be converted to VEC %s!",
                                                  vecVersion.getCurrentVersion(), targetVecVersion.getCurrentVersion());
            throw new VecException(errorMsg);
        }

        final VecProcessTask vecProcessTask = new VecProcessTask(content, streamName,
                                                                 vecVersion, targetVecVersion,
                                                                 ensureValidValidationEventConsumer(),
                                                                 compatibilityContextConsumer);

        try {
            return processor.createContent(vecProcessTask, targetClass);
        } catch (final Exception e) {
            final String errorMsg = String.format("Failed to create an VEC from %s.", streamName);
            throw new VecException(errorMsg);
        }
    }

    private <T extends HasVecVersion> T createVecContentFromStream(final InputStream inputStream,
                                                                   final String streamName,
                                                                   final Class<T> targetClass) {
        Objects.requireNonNull(inputStream, "Input Stream may not be null.");

        // Make sure the InputStream can be marked / reset.
        final InputStream streamToUse = inputStream.markSupported()
                ? inputStream
                : new BufferedInputStream(inputStream);

        final VecVersion vecVersion;
        try {
            vecVersion = VecVersionDetector.getVecVersion(streamToUse, streamName);
            if (vecVersion == null) {
                final String errorMsg = String.format("VEC Version couldn't be detected from %s.", streamName);
                throw new VecException(errorMsg);
            }
        } catch (final IOException e) {
            final String errorMsg = String.format("An Exception occurred when trying to detect the VEC Version of %s.",
                                                  streamName);
            throw new VecException(errorMsg, e);
        }

        final VecVersion targetVecVersion = CLASS_PATH_TO_VEC_VERSION_MAP.get(targetClass.getName());
        if (targetVecVersion == null) {
            final String errorMsg = String.format("VEC Conversion for %s is not supported yet.", targetClass);
            throw new VecException(errorMsg);
        }

        if (vecVersion != targetVecVersion && !vecVersion.canBeConvertedTo(targetVecVersion)) {
            final String errorMsg = String.format("VEC %s cannot be converted to VEC %s!",
                                                  vecVersion.getCurrentVersion(), targetVecVersion.getCurrentVersion());
            throw new VecException(errorMsg);
        }

        final VecProcessTask vecProcessTask = new VecProcessTask(streamToUse, streamName,
                                                                 vecVersion, targetVecVersion,
                                                                 ensureValidValidationEventConsumer(),
                                                                 compatibilityContextConsumer);

        try {
            return processor.createContent(vecProcessTask, targetClass);
        } catch (final Exception e) {
            final String errorMsg = String.format("Failed to create an VEC from %s.", streamName);
            throw new VecException(errorMsg);
        }
    }

    private Consumer<ValidationEvent> ensureValidValidationEventConsumer() {
        return validationEventConsumer == null
                ? new EventConsumer()
                : validationEventConsumer;
    }

}
