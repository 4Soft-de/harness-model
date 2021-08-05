package com.foursoft.vecmodel.vec120;

import com.foursoft.vecmodel.common.exception.VecException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import java.io.InputStream;
import java.util.Objects;

public class SchemaFactory {

    private static final String SCHEMA_PATH = "vec120/vec_1.2.1.RC-SNAPSHOT.xsd";
    private static final String STRICT_SCHEMA_PATH = "vec120/vec_1.2.0-strict.xsd";

    private SchemaFactory() {
        // Hide constructor
    }

    public static Schema getStrictSchema() {
        return getSchema(STRICT_SCHEMA_PATH);
    }

    public static Schema getSchema() {
        return getSchema(SCHEMA_PATH);
    }

    private static Schema getSchema(final String fileName) {
        try (final InputStream xsdFile = getInputStream(fileName)) {
            final javax.xml.validation.SchemaFactory schemaFactory =
                    javax.xml.validation.SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            return schemaFactory.newSchema(new StreamSource(xsdFile));
        } catch (final Exception e) {
            throw new VecException("Cannot initialize Validator!", e);
        }
    }

    private static InputStream getInputStream(final String fileName) {
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final InputStream is = classloader.getResourceAsStream(fileName);
        return Objects.requireNonNull(is, "Cannot open file: " + fileName);
    }
}
