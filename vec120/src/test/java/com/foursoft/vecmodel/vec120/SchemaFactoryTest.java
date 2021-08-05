package com.foursoft.vecmodel.vec120;

import com.foursoft.xml.io.validation.LogValidator;
import com.foursoft.xml.io.validation.XMLValidation;
import org.junit.jupiter.api.Test;

import javax.xml.validation.Schema;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class SchemaFactoryTest {

    @Test
    void testStrictSchema() {
        final VecContent root = new VecContent();
        root.setXmlId("id_1000_0");
        root.setVecVersion("1.2.0");


        final VecWriter vecWriter = new VecWriter();
        final String result = vecWriter.writeToString(root);

        final Schema schema = SchemaFactory.getStrictSchema();
        final Collection<LogValidator.ErrorLocation> errorLocations =
                new XMLValidation(schema).validateXML(result, StandardCharsets.UTF_8);

        assertThat(errorLocations).isEmpty();

    }

    @Test
    void testInvalidSchema() {
        final VecContent root = new VecContent();
        root.setXmlId("id_1000_0");
        root.setVecVersion("1.2.0");

        final VecPartVersion partVersion = new VecPartVersion();
        partVersion.setXmlId("id_1001_0");
        partVersion.setPartNumber("123_456_789");
        root.getPartVersions().add(partVersion);

        final VecWriter vecWriter = new VecWriter();
        final String result = vecWriter.writeToString(root);

        final Schema schema = SchemaFactory.getSchema();
        final Collection<LogValidator.ErrorLocation> errorLocations =
                new XMLValidation(schema).validateXML(result, StandardCharsets.UTF_8);

        assertThat(errorLocations).isNotEmpty();
    }
}
