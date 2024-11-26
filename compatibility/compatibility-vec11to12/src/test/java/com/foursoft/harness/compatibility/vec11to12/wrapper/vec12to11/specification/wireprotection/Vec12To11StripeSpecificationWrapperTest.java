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
package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.wireprotection;

import com.foursoft.harness.compatibility.vec11to12.TestFiles;
import com.foursoft.harness.compatibility.vec11to12.wrapper.AbstractBaseWrapperTest;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v113.*;
import com.foursoft.harness.vec.v113.predicates.VecPredicates;
import com.foursoft.harness.vec.v12x.VecContent;
import com.foursoft.harness.vec.v12x.VecReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class Vec12To11StripeSpecificationWrapperTest extends AbstractBaseWrapperTest {

    @Test
    void invokeTest() throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream("/vec12x/wire_protection+111_222_333.vec")) {
            final VecContent vecContent120 = new VecReader().read(inputStream);

            final com.foursoft.harness.vec.v113.VecContent vecContent113 =
                    get11To12Context().getWrapperProxyFactory().createProxy(vecContent120);

            final VecStripeSpecification stripeSpecification = vecContent113.getDocumentVersions()
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
                    .satisfies(spec -> assertThat(spec.getCustomProperties())
                            .hasSize(1)
                            .singleElement()
                            .extracting(VecNumericalValueProperty.class::cast)
                            .returns("Thickness", VecCustomProperty::getPropertyType)
                            .extracting(VecNumericalValueProperty::getValue)
                            .returns(1.3, VecNumericalValue::getValueComponent)
                    );
        }
    }

}
