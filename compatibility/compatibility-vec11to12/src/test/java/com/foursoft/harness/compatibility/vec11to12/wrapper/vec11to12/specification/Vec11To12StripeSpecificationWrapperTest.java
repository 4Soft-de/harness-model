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
package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification;

import com.foursoft.harness.compatibility.vec11to12.TestFiles;
import com.foursoft.harness.compatibility.vec11to12.wrapper.AbstractBaseWrapperTest;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v113.VecContent;
import com.foursoft.harness.vec.v113.VecReader;
import com.foursoft.harness.vec.v12x.VecDocumentVersion;
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

        try (final InputStream inputStream = TestFiles.getInputStream("/vec11x/wire_protection+111_222_333.vec")) {
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

            assertThat(stripeSpecification).isNotNull()
                    .returns(1.3, c -> c.getThickness().getValueComponent());

        }
    }

    private com.foursoft.harness.vec.v113.VecContent prepareVec113() {
        final com.foursoft.harness.vec.v113.VecContent vecContent = new com.foursoft.harness.vec.v113.VecContent();
        vecContent.setXmlId("vec_content_1");
        vecContent.setVecVersion("1.1.3");

        final com.foursoft.harness.vec.v113.VecDocumentVersion vecDocumentVersion =
                new com.foursoft.harness.vec.v113.VecDocumentVersion();
        vecDocumentVersion.setXmlId("vec_document_version_1");

        final com.foursoft.harness.vec.v113.VecStripeSpecification vecStripeSpecification =
                new com.foursoft.harness.vec.v113.VecStripeSpecification();
        vecStripeSpecification.setXmlId("vec_stripe_specification_1");
        vecStripeSpecification.setIdentification("Stripe");
        vecStripeSpecification.getCustomProperties().add(createCustomProperty());

        vecDocumentVersion.getSpecifications().add(vecStripeSpecification);
        vecContent.getDocumentVersions().add(vecDocumentVersion);
        return vecContent;
    }

    private com.foursoft.harness.vec.v113.VecCustomProperty createCustomProperty() {
        final com.foursoft.harness.vec.v113.VecNumericalValueProperty vecNumericalValueProperty =
                new com.foursoft.harness.vec.v113.VecNumericalValueProperty();
        vecNumericalValueProperty.setXmlId("vec_numerical_value_property_1");
        vecNumericalValueProperty.setPropertyType("Thickness");

        final com.foursoft.harness.vec.v113.VecNumericalValue vecNumericalValue =
                new com.foursoft.harness.vec.v113.VecNumericalValue();
        vecNumericalValue.setXmlId("vec_numerical_value_property_2");
        vecNumericalValue.setValueComponent(1.0);

        vecNumericalValueProperty.setValue(vecNumericalValue);
        return vecNumericalValueProperty;
    }

}
