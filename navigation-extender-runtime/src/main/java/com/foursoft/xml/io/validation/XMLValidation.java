package com.foursoft.xml.io.validation;

import com.foursoft.xml.io.XMLIOException;
import com.foursoft.xml.io.validation.LogValidator.ErrorLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class XMLValidation {
    private static final Logger LOGGER = LoggerFactory.getLogger(XMLValidation.class);
    private final Schema schema;

    /**
     * Creating a schema with the following lines
     * final InputStream xsdFile = getInputStream("schema.xsd");
     * final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
     * schema = schemaFactory.newSchema(new StreamSource(xsdFile));
     * <p>
     * The validator can use a lot of memory, because it holds the xmlContent 3 times in memory!
     *
     * @param schema
     */
    public XMLValidation(final Schema schema) {
        this.schema = schema;
    }

    public Collection<ErrorLocation> validateXML(final String xmlContent, final Charset charset) {
        try {
            final LogValidator validator = createValidator();
            final InputStream inputStream = toInputStream(xmlContent, charset);
            final Source inputSource = new StreamSource(inputStream);
            final boolean validate = validator.validate(inputSource);

            if (validate) {
                return Collections.emptyList();
            } else {
                return validator.getErrorLines();
            }
        } catch (final SAXException | IOException e) {
            throw new XMLIOException("XML contains fatal errors, cannot read it:", e);
        }
    }

    public static void logXmlString(final String xmlContent, final Collection<ErrorLocation> errorLines) {

        final Map<Integer, String> errorMap = prepareErrors(errorLines);

        final String[] lines = xmlContent.split("\\r?\\n");
        final StringBuilder enhanced = new StringBuilder(xmlContent.length() * 2);
        for (int i = 1; i <= lines.length; i++) {

            enhanced.append(i);
            enhanced.append(": ");
            if (errorMap.containsKey(i)) {
                enhanced.append("ERROR ");
            } else {
                enhanced.append("      ");
            }
            enhanced.append(lines[i - 1]);
            if (errorMap.containsKey(i)) {
                enhanced.append(' ');
                enhanced.append(errorMap.get(i));
            }
            enhanced.append('\n');
        }
        LOGGER.info(enhanced.toString());

    }

    private static InputStream toInputStream(final String input, final Charset charset) {
        return new ByteArrayInputStream(input.getBytes(charset));
    }

    private static Map<Integer, String> prepareErrors(final Collection<ErrorLocation> errorLines) {
        final Map<Integer, String> errorMap = new HashMap<>();

        for (final ErrorLocation errorLine : errorLines) {
            final int line = errorLine.line;
            final String error = errorLine.message;
            if (errorMap.containsKey(line)) {
                errorMap.put(line, errorMap.get(line) + "/" + error);
            } else {
                errorMap.put(line, error);
            }
        }
        return errorMap;
    }

    private LogValidator createValidator() {
        final Validator validator = schema.newValidator();
        return new LogValidator(validator);
    }

}
