package com.foursoft.harness.compatibility.vec11to12.util;

import com.foursoft.harness.compatibility.vec11to12.TestFiles;
import com.foursoft.harness.vec.v12x.VecContent;
import com.foursoft.harness.vec.v12x.VecCreation;
import com.foursoft.harness.vec.v12x.VecItemVersion;
import com.foursoft.harness.vec.v12x.Version;
import org.junit.jupiter.api.Test;

import javax.xml.bind.ValidationEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultVecReaderTest {

    @Test
    void testReadVEC113() throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            final VecContent vecContent = DefaultVecReader.read(inputStream, "test stream");
            assertThat(vecContent).isNotNull()
                    .returns(Version.VERSION, VecContent::getVecVersion);
            final List<VecCreation> creations = vecContent.getDocumentVersions().stream()
                    .map(VecItemVersion::getCreation)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            assertThat(creations).isNotEmpty();
        }
    }

    @Test
    void multipleReads() throws Exception {
        final List<ValidationEvent> events1 = new ArrayList<>();
        final List<ValidationEvent> events2 = new ArrayList<>();
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            DefaultVecReader.read(inputStream, "test stream", events1::add);
            assertEquals(0, events1.size());
        }
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            DefaultVecReader.read(inputStream, "test stream", events2::add);
            assertEquals(0, events1.size());
            assertEquals(0, events2.size());
        }
    }

}