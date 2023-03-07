package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification;

import com.foursoft.harness.compatibility.vec11to12.TestFiles;
import com.foursoft.harness.compatibility.vec11to12.util.DefaultVecReader;
import com.foursoft.harness.compatibility.vec11to12.util.Predicates;
import com.foursoft.harness.compatibility.vec11to12.wrapper.AbstractBaseWrapperTest;
import com.foursoft.harness.vec.v12x.VecContent;
import com.foursoft.harness.vec.v12x.VecWireElement;
import com.foursoft.harness.vec.v12x.VecWireSpecification;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class Vec11To12WireSpecificationWrapperTest extends AbstractBaseWrapperTest {

    @Test
    void invokeTest() throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            final VecContent vecContent = DefaultVecReader.read(inputStream, "test stream");
            vecContent.getDocumentVersions().stream()
                    .filter(Predicates.partMasterV12())
                    .map(documentVersion -> documentVersion.getSpecificationsWithType(VecWireSpecification.class))
                    .flatMap(Collection::stream)
                    .forEach(spec -> {
                        final VecWireElement wireElement = spec.getWireElement();
                        for (final VecWireElement swe : wireElement.getSubWireElements()) {
                            final VecWireElement parentWireElement = swe.getParentWireElement();
                            assertThat(parentWireElement).isEqualTo(wireElement);
                        }
                        final VecWireSpecification parentWireSpecification = wireElement.getParentWireSpecification();
                        assertThat(parentWireSpecification).isEqualTo(spec);
                    });

            final List<VecWireElement> components = vecContent.getDocumentVersions().stream()
                    .filter(Predicates.partMasterV12())
                    .map(documentVersion -> documentVersion.getSpecificationsWithType(VecWireSpecification.class))
                    .flatMap(Collection::stream)
                    .map(VecWireSpecification::getWireElement)
                    .collect(Collectors.toList());

            assertThat(components)
                    .isNotNull()
                    .isNotEmpty()
                    .allSatisfy(wireElement -> assertThat(wireElement.getParentWireSpecification())
                            .isNotNull());
        }
    }

}