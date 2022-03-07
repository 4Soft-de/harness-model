package com.foursoft.harness.kbl.v24.validation;

import com.foursoft.harness.kbl.v24.exception.KblException;
import com.foursoft.jaxb.navext.runtime.io.validation.LogErrors;
import com.foursoft.jaxb.navext.runtime.io.validation.LogValidator;
import com.foursoft.jaxb.navext.runtime.io.validation.XMLValidation;

import javax.xml.validation.Schema;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Validate KBL data.
 */

public class KblValidation {

    private static final Schema schema = SchemaFactory.getSchema();

    private KblValidation() {
        // hide constructor
    }

    /**
     * validates a xml string against the kbl schema
     *
     * @param xmlContent  the xml content
     * @param consumer    to display scheme violations.
     * @param detailedLog if true and error happens a detailed log is written, use always true in tests !
     */
    public static void validateXML(final String xmlContent, final Consumer<String> consumer, final boolean detailedLog) {
        Objects.requireNonNull(xmlContent);
        final XMLValidation xmlValidation = new XMLValidation(schema);
        final Collection<LogValidator.ErrorLocation> errorLocations = xmlValidation.validateXML(xmlContent,
                StandardCharsets.UTF_8);
        if (detailedLog && !errorLocations.isEmpty()) {
            // TODO use  https://github.com/4Soft-de/jaxb-enhanced-navigation/pull/7 scoped!
            final String annotateXMLContent = LogErrors.annotateXMLContent(xmlContent, errorLocations);
            if (!annotateXMLContent.isEmpty()) {
                consumer.accept(annotateXMLContent);
            }
        }
        if (!errorLocations.isEmpty()) {
            throw new KblException("Schema validation failed! Use detailedLog for more information");
        }
    }

}
