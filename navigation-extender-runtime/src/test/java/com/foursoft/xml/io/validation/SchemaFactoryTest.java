package com.foursoft.xml.io.validation;

import com.foursoft.xml.io.TestData;
import org.junit.jupiter.api.Test;

import javax.xml.validation.Schema;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SchemaFactoryTest {

    @Test
    void testGetSchema() {
        final String fileName = TestData.VALIDATE_BASE_PATH.resolve("basic-test.xsd").toString();
        final Schema schema = SchemaFactory.getSchema(fileName);
        assertThat(schema).isNotNull();
    }

    @Test
    void testInvalidSchema() {
        assertThatThrownBy(() -> SchemaFactory.getSchema(""))
                .isInstanceOf(SchemaFactoryException.class);
    }
}
