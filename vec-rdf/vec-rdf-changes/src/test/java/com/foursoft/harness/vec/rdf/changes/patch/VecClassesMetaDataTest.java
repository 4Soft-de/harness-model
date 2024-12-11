package com.foursoft.harness.vec.rdf.changes.patch;

import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import static org.assertj.core.api.Assertions.assertThat;

class VecClassesMetaDataTest {

    private final VecClassesMetaData metaData = VecClassesMetaData.metaData();

    @Test
    void should_return_all_fields() {
        VecField[] connectorHousingFields = metaData.fieldsForClass(VecConnectorHousingSpecification.class);

        assertThat(connectorHousingFields).hasSize(25);
    }

    @Test
    void should_return_a_field() throws IntrospectionException {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor("identification",
                                                                       VecConnectorHousingSpecification.class);

        assertThat(metaData.fieldForPropertyDescriptor(propertyDescriptor)).isNotNull();
    }

}