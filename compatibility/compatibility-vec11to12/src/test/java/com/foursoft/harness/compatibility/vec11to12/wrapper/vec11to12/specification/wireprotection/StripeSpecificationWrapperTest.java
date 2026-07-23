/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.1.X To VEC 1.2.X
 * %%
 * Copyright (C) 2020 - 2024 4Soft GmbH
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
package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification.wireprotection;

import com.foursoft.harness.compatibility.vec11to12.TestFiles;
import com.foursoft.harness.compatibility.vec11to12.util.WrapperUtils;
import com.foursoft.harness.compatibility.vec11to12.wrapper.AbstractBaseWrapperTest;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v113.VecContent;
import com.foursoft.harness.vec.v113.VecReader;
import com.foursoft.harness.vec.v12x.VecDocumentVersion;
import com.foursoft.harness.vec.v12x.VecNumericalValue;
import com.foursoft.harness.vec.v12x.VecStripeSpecification;
import com.foursoft.harness.vec.v12x.predicates.VecPredicates;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class Vec11To12StripeSpecificationWrapperTest extends AbstractBaseWrapperTest {

    @Test
    void invokeTest() throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.WIRE_PROTECTION_11X)) {
            final VecContent vecContent113 = new VecReader().read(inputStream);

            final com.foursoft.harness.vec.v12x.VecContent vecContent120 =
                    get11To12Context().getWrapperProxyFactory().createProxy(vecContent113);

            final VecStripeSpecification stripeSpecification = vecContent120.getDocumentVersions()
                    .stream()
                    .filter(VecPredicates.partMasterDocument())
                    .map(VecDocumentVersion::getSpecifications)
                    .flatMap(Collection::stream)
                    .filter(VecStripeSpecification.class::isInstance)
                    .map(VecStripeSpecification.class::cast)
                    .filter(Objects::nonNull)
                    .collect(StreamUtils.findOneOrNone())
                    .orElse(null);

            assertThat(stripeSpecification)
                    .isNotNull()
                    .returns(1.3, c -> c.getThickness().getValueComponent())
                    .satisfies(spec -> assertThat(spec.getCustomProperties())
                            .isEmpty()
                    );

            final VecNumericalValue copiedValue =
                    WrapperUtils.copyVec12xNumericalValue(stripeSpecification.getThickness());
            copiedValue.setValueComponent(3.1);
            stripeSpecification.setThickness(copiedValue);

            assertThat(stripeSpecification)
                    .returns(3.1, c -> c.getThickness().getValueComponent());
        }
    }

}
