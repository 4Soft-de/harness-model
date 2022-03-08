package com.foursoft.harness.vec.v113.validation;

import com.foursoft.harness.vec.common.exception.VecException;
import com.foursoft.jaxb.navext.runtime.io.validation.LogErrors;
import com.foursoft.jaxb.navext.runtime.io.validation.LogValidator.ErrorLocation;
import com.foursoft.jaxb.navext.runtime.io.validation.XMLValidation;

import javax.xml.validation.Schema;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Validate VEC data.
 */
public final class VecValidation {
    private static final Schema schema = SchemaFactory.getSchema();

    private VecValidation() {
        // hide default constructor
    }

    /**
     * Validates a xml string against the vec schema
     *
     * @param xmlContent  the xml content
     * @param consumer    to display scheme violations.
     * @param detailedLog if true and error happens a detailed log is written, use always true in tests !
     */
    public static void validateXML(final String xmlContent, final Consumer<String> consumer, final boolean detailedLog) {
        Objects.requireNonNull(xmlContent);

        final XMLValidation xmlValidation = new XMLValidation(schema);
        final Collection<ErrorLocation> errorLocations = xmlValidation.validateXML(xmlContent, StandardCharsets.UTF_8);
        if (detailedLog && !errorLocations.isEmpty()) {
            final String annotateXMLContent = LogErrors.annotateXMLContent(xmlContent, errorLocations);
            if (!annotateXMLContent.isEmpty()) {
                consumer.accept(annotateXMLContent);
            }
        }
        if (!errorLocations.isEmpty()) {
            throw new VecException("Schema validation failed! Use detailedLog for more information");
        }
    }

}