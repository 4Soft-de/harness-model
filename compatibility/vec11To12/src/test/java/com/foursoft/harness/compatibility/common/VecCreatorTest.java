package com.foursoft.harness.compatibility.common;

import com.foursoft.harness.compatibility.core.common.EventConsumer;
import com.foursoft.harness.compatibility.vec11to12.TestFiles;
import com.foursoft.harness.compatibility.vec11to12.Vec11XTo12XProcessor;
import com.foursoft.harness.compatibility.vec11to12.common.VecCreator;
import com.foursoft.harness.compatibility.vec11to12.common.VecVersion;
import com.foursoft.harness.compatibility.vec11to12.wrapper.AbstractBaseWrapperTest;
import com.foursoft.harness.vec.common.exception.VecException;
import com.foursoft.harness.vec.v113.VecContent;
import com.foursoft.harness.vec.v113.VecReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class VecCreatorTest extends AbstractBaseWrapperTest {

    private static final String EXCEPTION_MESSAGE_FORMAT = "VEC Version couldn't be detected from %s.";

    private static VecCreator vecCreator;

    @BeforeAll
    static void init() {
        vecCreator = new VecCreator(new Vec11XTo12XProcessor())
                .withValidationEventConsumer(new EventConsumer());
    }

    @Test
    void createVec11xFromStream() {
        final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE);
        final String streamName = "createVec11xFromStream";

        final VecContent vec11x = vecCreator.createVec(inputStream, streamName,
                                                       com.foursoft.harness.vec.v113.VecContent.class);
        checkVec113(vec11x);

        final VecContent vecContent = new VecContent();
        // Not setting a version to make it fail.
        assertThatExceptionOfType(VecException.class)
                .isThrownBy(() -> vecCreator.createVec(vecContent, streamName,
                                                       com.foursoft.harness.vec.v113.VecContent.class))
                .withMessage(EXCEPTION_MESSAGE_FORMAT, streamName);
    }

    @Test
    void createVec11xFromVec11x() {
        final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE);
        final VecContent vecFromReader = new VecReader().read(inputStream);
        assertThat(vecFromReader).isNotNull();

        final VecContent vecFromCreator = vecCreator.createVec(vecFromReader, "createVec11xFrom120",
                                                               com.foursoft.harness.vec.v113.VecContent.class);
        checkVec113(vecFromCreator);
    }

    @Test
    void createVec12xFromVec11xStream() {
        final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE);
        final String streamName = "createVec12xFromVec11xStream";

        final com.foursoft.harness.vec.v12x.VecContent vec12x =
                vecCreator.createVec(inputStream, streamName, com.foursoft.harness.vec.v12x.VecContent.class);
        checkVec12X(vec12x);

        final VecContent vecContent = new VecContent();
        // Not setting a version to make it fail.
        assertThatExceptionOfType(VecException.class)
                .isThrownBy(() -> vecCreator.createVec(vecContent, streamName,
                                                       com.foursoft.harness.vec.v12x.VecContent.class))
                .withMessage(EXCEPTION_MESSAGE_FORMAT, streamName);
    }

    @Test
    void createVec12xFromVec11x() {
        final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE);
        final VecContent vec113 = new com.foursoft.harness.vec.v113.VecReader().read(inputStream);
        assertThat(vec113).isNotNull();

        final com.foursoft.harness.vec.v12x.VecContent vec12x =
                vecCreator.createVec(vec113, "createVec12xFromVec", com.foursoft.harness.vec.v12x.VecContent.class);
        checkVec12X(vec12x);
    }

    private void checkVec113(final VecContent vec113) {
        final Optional<VecVersion> optVersion = VecVersion.findVersion(vec113);
        assertThat(optVersion)
                .isPresent()
                .get()
                .isEqualTo(VecVersion.VEC11X);
    }

    private void checkVec12X(final com.foursoft.harness.vec.v12x.VecContent vec120) {
        final Optional<VecVersion> optVersion = VecVersion.findVersion(vec120);
        assertThat(optVersion)
                .isPresent()
                .get()
                .isEqualTo(VecVersion.VEC12X);
    }

}