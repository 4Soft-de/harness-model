package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification;

import com.foursoft.harness.compatibility.vec11to12.TestFiles;
import com.foursoft.harness.compatibility.vec11to12.util.Predicates;
import com.foursoft.harness.compatibility.vec11to12.wrapper.AbstractBaseWrapperTest;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v113.VecContent;
import com.foursoft.harness.vec.v113.VecReader;
import com.foursoft.harness.vec.v12x.VecDocumentVersion;
import com.foursoft.harness.vec.v12x.VecStripeSpecification;
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
                    .filter(Predicates.partMasterV12())
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