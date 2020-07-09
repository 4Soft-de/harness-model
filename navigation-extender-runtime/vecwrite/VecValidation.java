package com.volkswagenag.daem.converter.vec.c2v.vecwrite;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class VecValidation {
    private static final Logger LOGGER = LoggerFactory.getLogger(VecValidation.class);
    private static Schema schema120;

    private VecValidation() {

    }

    public static void validateXML(final String xmlContent, final boolean detailedLog) {
        try {
            final LogValidator validator = createValidator();
            final InputStream inputStream = IOUtils.toInputStream(xmlContent, StandardCharsets.UTF_8);
            final Source inputSource = new StreamSource(inputStream);
            final boolean validate = validator.validate(inputSource);
            if (detailedLog) {
                logXmlString(xmlContent, validator.getErrorLines());
            }
            if (!validate) {
                throw new RuntimeException("Schema validation failed! Use detailedLog for more information");
            }
        } catch (final SAXException | IOException e) {
            throw new RuntimeException("XML contains fatal errors, cannot read it:", e);
        }
    }

    public static InputStream getInputStream(final String fileName) throws IOException {
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final InputStream is = classloader.getResourceAsStream(fileName);
        if (is == null) {
            throw new IOException("Cannot open file:" + fileName);
        }
        return is;
    }

    public static void logXmlString(final String plainXml, final Collection<Pair<Integer, String>> errorLines) {

        final Map<Integer, String> errorMap = new HashMap<>();

        for (final Pair<Integer, String> errorLine : errorLines) {
            final Integer line = errorLine.getKey();
            final String error = errorLine.getValue();
            if (errorMap.containsKey(line)) {
                errorMap.put(line, errorMap.get(line) + "/" + error);

            } else {
                errorMap.put(line, error);
            }
        }

        final String[] lines = plainXml.split("\\r?\\n");
        final StringBuilder enhanced = new StringBuilder(plainXml.length() + lines.length * 2);
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

    private static LogValidator createValidator() {
        final Validator validator = getVec120Validator();
        return new LogValidator(validator);
    }

    private static Validator getVec120Validator() {
        synchronized (LOGGER) {
            if (schema120 == null) {
                try {
                    final InputStream xsdFile = getInputStream("vec120/vec_1.2.1.RC-SNAPSHOT.xsd");
                    final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    schema120 = schemaFactory.newSchema(new StreamSource(xsdFile));
                } catch (final Exception e) {
                    throw new RuntimeException("Cannot initialize Validator!", e);
                }
            }
            return schema120.newValidator();
        }
    }
}
