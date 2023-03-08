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
import com.foursoft.harness.compatibility.vec11to12.util.Predicates;
import com.foursoft.harness.compatibility.vec11to12.wrapper.AbstractBaseWrapperTest;
import com.foursoft.harness.vec.v113.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class Vec11To12CavitySpecificationWrapperTest extends AbstractBaseWrapperTest {

    private static com.foursoft.harness.vec.v12x.VecSize createVecSize120(final double valueComponent) {
        final com.foursoft.harness.vec.v12x.VecSize vecSize = new com.foursoft.harness.vec.v12x.VecSize();
        vecSize.setXmlId("XMLID");
        final com.foursoft.harness.vec.v12x.VecNumericalValue vecNumericalValue =
                new com.foursoft.harness.vec.v12x.VecNumericalValue();
        vecNumericalValue.setValueComponent(valueComponent);
        vecSize.setHeight(vecNumericalValue);
        return vecSize;
    }

    @Test
    void invokeTest() throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            final VecContent vecContent113 = new VecReader().read(inputStream);

            final List<VecCavitySpecification> vecCavitySpecifications113 =
                    vecContent113.getDocumentVersions().stream()
                            .filter(Predicates.partMasterV11())
                            .map(VecDocumentVersion::getSpecifications)
                            .flatMap(Collection::stream)
                            .filter(VecCavitySpecification.class::isInstance)
                            .map(VecCavitySpecification.class::cast)
                            .collect(Collectors.toList());
            assertThat(vecCavitySpecifications113).isNotEmpty();

            final VecCavitySpecification vecCavitySpecification113 = vecCavitySpecifications113.get(0);
            final String xmlId113 = vecCavitySpecification113.getXmlId();

            final com.foursoft.harness.vec.v12x.VecContent vecContent120 =
                    get11To12Context().getWrapperProxyFactory().createProxy(vecContent113);

            final com.foursoft.harness.vec.v12x.VecCavitySpecification vecCavitySpecification120 =
                    vecContent120.getDocumentVersions().stream()
                            .filter(Predicates.partMasterV12())
                            .map(com.foursoft.harness.vec.v12x.VecDocumentVersion::getSpecifications)
                            .flatMap(Collection::stream)
                            .filter(com.foursoft.harness.vec.v12x.VecCavitySpecification.class::isInstance)
                            .map(com.foursoft.harness.vec.v12x.VecCavitySpecification.class::cast)
                            .filter(specification -> specification.getXmlId().equals(xmlId113))
                            .findFirst().orElse(null);
            assertThat(vecCavitySpecification120).isNotNull();
            assertThat(vecCavitySpecification120.getCavityDimension()).isNotNull();

            vecCavitySpecification120.getCavityDimension().getHeight().setValueComponent(5.0);
            assertThat(vecCavitySpecification113.getCavityDiameter())
                    .returns(5.0, VecNumericalValue::getValueComponent);

            final com.foursoft.harness.vec.v12x.VecSize vecSize120 = createVecSize120(10.0);
            vecCavitySpecification120.setCavityDimension(vecSize120);
            assertThat(vecCavitySpecification113.getCavityDiameter()).isNotNull()
                    .returns(10.0, VecNumericalValue::getValueComponent);
        }
    }

} 
