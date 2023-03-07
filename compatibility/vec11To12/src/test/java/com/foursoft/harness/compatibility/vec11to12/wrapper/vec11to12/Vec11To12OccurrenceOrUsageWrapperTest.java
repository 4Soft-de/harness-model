package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12;

import com.foursoft.harness.compatibility.vec11to12.TestFiles;
import com.foursoft.harness.compatibility.vec11to12.util.DefaultVecReader;
import com.foursoft.harness.compatibility.vec11to12.wrapper.AbstractBaseWrapperTest;
import com.foursoft.harness.vec.v12x.VecCompositionSpecification;
import com.foursoft.harness.vec.v12x.VecContent;
import com.foursoft.harness.vec.v12x.VecEEComponentRole;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class Vec11To12OccurrenceOrUsageWrapperTest extends AbstractBaseWrapperTest {

    @Test
    void invokeTest() throws IOException {
        try (final InputStream inputOriginal = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            final com.foursoft.harness.vec.v113.VecContent originalContent =
                    new com.foursoft.harness.vec.v113.VecReader().read(inputOriginal);
            assertThat(originalContent).isNotNull();

            final List<com.foursoft.harness.vec.v113.VecEEComponentRole> components113 =
                    originalContent.getDocumentVersions().stream()
                            .map(documentVersion -> documentVersion
                                    .getSpecificationsWithType(
                                            com.foursoft.harness.vec.v113.VecCompositionSpecification.class))
                            .flatMap(Collection::stream)
                            .map(com.foursoft.harness.vec.v113.VecCompositionSpecification::getComponents)
                            .flatMap(Collection::stream)
                            .map(partOccurrence -> partOccurrence
                                    .getRolesWithType(com.foursoft.harness.vec.v113.VecEEComponentRole.class))
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList());
            assertThat(components113)
                    .isNotNull()
                    .isNotEmpty();
        }

        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            final VecContent vecContent = DefaultVecReader.read(inputStream, "test stream");

            final List<VecEEComponentRole> components120 = vecContent.getDocumentVersions().stream()
                    .map(documentVersion -> documentVersion
                            .getSpecificationsWithType(VecCompositionSpecification.class))
                    .flatMap(Collection::stream)
                    .map(VecCompositionSpecification::getComponents)
                    .flatMap(Collection::stream)
                    .map(partOccurrence -> partOccurrence
                            .getRolesWithType(VecEEComponentRole.class))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            assertThat(components120)
                    .isNotNull()
                    .isNotEmpty();
        }
    }

} 