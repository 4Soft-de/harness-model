package com.foursoft.harness.vec.rdf.common.meta;

import com.foursoft.harness.vec.rdf.common.meta.xmi.VersionLookupModelProvider;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MetaDataServiceTest {

    @Test
    void should_instantiate_metadata_service() {
        final MetaDataService service = new MetaDataService(new VersionLookupModelProvider());

        assertThat(service).isNotNull();
    }

    @Test
    void should_return_correct_metadata() {
        final MetaDataService service = new MetaDataService(new VersionLookupModelProvider());

        final ClassMetaData chsMetaData = service.metaDataFor(VecConnectorHousingSpecification.class);
        final ClassMetaData numValMetaData = service.metaDataFor(VecNumericalValue.class);

        assertThat(chsMetaData).isNotNull();
        assertThat(chsMetaData.isAnonymous()).isFalse();
        assertThat(chsMetaData.getType()).isEqualTo(VecConnectorHousingSpecification.class);

        assertThat(chsMetaData.fields()).filteredOn(
                fieldMetaData -> fieldMetaData.getField().getName().equals("connectorPositionAssuranceType")).hasSize(
                1).singleElement().satisfies(fieldMetaData -> {
            assertThat(fieldMetaData.getModelType().isEnum()).isTrue();
        });

        assertThat(numValMetaData).isNotNull();
        assertThat(numValMetaData.isAnonymous()).isTrue();
        assertThat(numValMetaData.getType()).isEqualTo(VecNumericalValue.class);
        assertThat(numValMetaData.fields()).hasSize(3);

        assertThat(numValMetaData.fields()[0]).satisfies(f -> {
            assertThat(f.getField().getName()).isEqualTo("valueComponent");
            assertThat(f.getType()).isEqualTo(double.class);
            assertThat(f.getModelType()).isNotNull();
            assertThat(f.isAssociation()).isFalse();
            assertThat(f.isOrdered()).isFalse();
            assertThat(f.isUnique()).isTrue();
            assertThat(f.getModelType().isPrimitive()).isTrue();
            assertThat(f.isEnumField()).isFalse();
        });
    }

}