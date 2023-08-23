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
package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification;

import com.foursoft.harness.compatibility.vec11to12.TestFiles;
import com.foursoft.harness.compatibility.vec11to12.util.DefaultVecReader;
import com.foursoft.harness.compatibility.vec11to12.wrapper.AbstractBaseWrapperTest;
import com.foursoft.harness.vec.v12x.*;
import com.foursoft.harness.vec.v12x.predicates.VecPredicates;
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
                    .filter(VecPredicates.partMasterDocument())
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
