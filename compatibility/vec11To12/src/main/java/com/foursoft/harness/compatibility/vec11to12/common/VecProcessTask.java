package com.foursoft.harness.compatibility.vec11to12.common;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.vec.common.HasVecVersion;

import javax.xml.bind.ValidationEvent;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * POJO containing various information needed for processing / converting a {@code VecContent}.
 */
public class VecProcessTask {

    private final String streamName;
    private final InputStream inputStream;
    private final HasVecVersion vecContent;
    private final VecVersion sourceVersion;
    private final VecVersion targetVersion;
    private final Consumer<ValidationEvent> validationEventConsumer;
    private final Consumer<Context> compatibilityContextConsumer;

    /**
     * Creates a new VEC Process Task.
     *
     * @param inputStream                  {@link InputStream} to create a VecContent from.
     * @param streamName                   Name of the stream, used for logging.
     * @param sourceVersion                Source Version, detected from the InputStream.
     * @param targetVersion                Target Version the InputStream should be converted to.
     * @param validationEventConsumer      Consumer to run for {@link ValidationEvent}s.
     * @param compatibilityContextConsumer Consumer for a {@link CompatibilityContext}.
     *                                     Can be used if the VEC needs to be converted.
     */
    VecProcessTask(final InputStream inputStream,
                   final String streamName,
                   final VecVersion sourceVersion,
                   final VecVersion targetVersion,
                   final Consumer<ValidationEvent> validationEventConsumer,
                   final Consumer<Context> compatibilityContextConsumer) {
        this(null, inputStream, streamName,
             sourceVersion, targetVersion,
             validationEventConsumer, compatibilityContextConsumer);
    }

    /**
     * Creates a new VEC Process Task.
     *
     * @param vecContent                   A VecContent to create another VecContent in the given target version from.
     * @param streamName                   Name of the stream, used for logging.
     * @param sourceVersion                Source Version, detected from the VecContent.
     * @param targetVersion                Target Version the InputStream should be converted to.
     * @param validationEventConsumer      Consumer to run for {@link ValidationEvent}s.
     * @param compatibilityContextConsumer Consumer for a {@link CompatibilityContext}.
     *                                     Can be used if the VEC needs to be converted.
     */
    VecProcessTask(final HasVecVersion vecContent,
                   final String streamName,
                   final VecVersion sourceVersion,
                   final VecVersion targetVersion,
                   final Consumer<ValidationEvent> validationEventConsumer,
                   final Consumer<Context> compatibilityContextConsumer) {
        this(vecContent, null, streamName,
             sourceVersion, targetVersion,
             validationEventConsumer, compatibilityContextConsumer);
    }

    private VecProcessTask(final HasVecVersion vecContent,
                           final InputStream inputStream,
                           final String streamName,
                           final VecVersion sourceVersion,
                           final VecVersion targetVersion,
                           final Consumer<ValidationEvent> validationEventConsumer,
                           final Consumer<Context> compatibilityContextConsumer) {
        this.streamName = Objects.requireNonNull(streamName, "Stream Name may not be null.");
        this.inputStream = inputStream;
        this.vecContent = vecContent;
        this.sourceVersion = Objects.requireNonNull(sourceVersion, "Source Version may not be null.");
        this.targetVersion = Objects.requireNonNull(targetVersion, "Target Version may not be null.");
        this.validationEventConsumer = validationEventConsumer;
        this.compatibilityContextConsumer = compatibilityContextConsumer;

        if (inputStream == null && vecContent == null) {
            throw new IllegalStateException("Either the InputStream or the VecContent has to be non-null.");
        }
    }

    /**
     * Returns the name of the Stream.
     *
     * @return The name of the Stream.
     */
    public String getStreamName() {
        return streamName;
    }

    /**
     * Returns the {@link InputStream} which is used for processing.
     *
     * @return Possibly-empty InputStream. Will be empty if a {@link #getContent()} was given instead.
     */
    public Optional<InputStream> getInputStream() {
        return Optional.ofNullable(inputStream);
    }

    /**
     * Returns the {@link HasVecVersion VecContent} which is used for processing.
     *
     * @return Possibly-empty VecContent. Will be empty if a {@link #getInputStream()} was given instead.
     */
    public Optional<HasVecVersion> getContent() {
        return Optional.ofNullable(vecContent);
    }

    /**
     * Returns the source version of the given InputStream / VecContent.
     *
     * @return The source version of the given InputStream / VecContent.
     */
    public VecVersion getSourceVersion() {
        return sourceVersion;
    }

    /**
     * Returns the target version of the given InputStream / VecContent.
     *
     * @return The target version of the given InputStream / VecContent.
     */
    public VecVersion getTargetVersion() {
        return targetVersion;
    }

    /**
     * Returns the Consumer used for {@link ValidationEvent}s.
     *
     * @return Possibly-null Consumer used for {@link ValidationEvent}s.
     */
    public Consumer<ValidationEvent> getValidationEventConsumer() {
        return validationEventConsumer;
    }

    /**
     * Returns the Consumer for the {@link Context}.
     *
     * @return Possibly-null Consumer for the {@link Context}.
     */
    public Consumer<Context> getCompatibilityContextConsumer() {
        return compatibilityContextConsumer;
    }

}
