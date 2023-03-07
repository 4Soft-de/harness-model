package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification;

import com.foursoft.harness.compatibility.vec11to12.TestFiles;
import com.foursoft.harness.compatibility.vec11to12.util.DefaultVecReader;
import com.foursoft.harness.compatibility.vec11to12.util.Predicates;
import com.foursoft.harness.compatibility.vec11to12.wrapper.AbstractBaseWrapperTest;
import com.foursoft.harness.vec.v12x.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Vec11To12TapeSpecificationWrapperTest extends AbstractBaseWrapperTest {

    @Test
    void invokeTest() throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            final VecContent vecContent = DefaultVecReader.read(inputStream, "test stream");

            final VecTapeSpecification tapeSpecification = vecContent.getDocumentVersions()
                    .stream()
                    .filter(Predicates.partMasterV12())
                    .map(VecDocumentVersion::getSpecifications)
                    .flatMap(Collection::stream)
                    .filter(VecTapeSpecification.class::isInstance)
                    .map(VecTapeSpecification.class::cast)
                    .min(Comparator.comparing(VecSpecification::getIdentification))
                    .orElse(null);
            assertThat(tapeSpecification)
                    .isNotNull()
                    .returns(null, VecTapeSpecification::getCoilCoreDiameter)
                    .satisfies(tapeSpec -> assertThat(tapeSpec.getWidth()).isNotNull())
                    .satisfies(tapeSpec -> assertThat(tapeSpec.getThickness()).isNotNull());

            assertEquals(19.0, tapeSpecification.getWidth().getValueComponent());
            final VecNumericalValue newValue = new VecNumericalValue();
            newValue.setValueComponent(42.0);
            tapeSpecification.setWidth(newValue);
            assertEquals(42.0, tapeSpecification.getWidth().getValueComponent());
        }
    }

}