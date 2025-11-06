/*-
 * ========================LICENSE_START=================================
 * NavExt Runtime
 * %%
 * Copyright (C) 2019 - 2023 4Soft GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
package com.foursoft.harness.navext.runtime.io.validation;

import com.foursoft.harness.navext.runtime.exception.XmlValidationException;
import com.foursoft.harness.navext.runtime.io.utils.XMLIOException;
import com.foursoft.harness.navext.runtime.io.validation.LogValidator.ErrorLocation;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A helper class to validate a given xml string against an xsd schema. All found errors are returned in a collection.
 */
public final class XMLValidation {
    private final Schema schema;

    /**
     * The validator can use a lot of memory, because it holds the xmlContent multiple times in memory!
     *
     * @param schema the schema to validate against
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
        } catch (final XMLIOException e) {
            throw new XMLIOException("XML contains fatal errors, cannot read it:", e);
        }
    }

    /**
     * Validates the given XML file against the given {@link Schema}.
     *
     * @param schema      The schema to use for validation.
     * @param xmlFilePath Path to the file which should be validated.
     * @param consumer    Consumer to run for the annotated XML file contents.
     *                    Can be used to display scheme violations.
     *                    <b>Will only be used if {@code detailedLog} is set to {@code true}!</b>
     * @param detailedLog Flag whether the validation errors should be annotated and processed or not.
     * @throws XMLIOException         In case something goes wrong before or during the validation.
     * @throws XmlValidationException In case the validation for the given file fails.
     */
    public static void validateXML(final Schema schema, final Path xmlFilePath, final Consumer<String> consumer,
                                   final boolean detailedLog) throws XMLIOException, XmlValidationException {
        try {
            final String xmlContent = Files.readString(xmlFilePath);
            validateXML(schema, xmlContent, consumer, detailedLog);
        } catch (final IOException e) {
            final String errorMsg = String.format("IOException occurred when trying to validate file '%s'.",
                                                  xmlFilePath);
            throw new XMLIOException(errorMsg, e);
        }
    }

    /**
     * Validates the given XML string against the given {@link Schema}.
     *
     * @param schema      The schema to use for validation.
     * @param xmlContent  Contents of an XML file which should be validated.
     * @param consumer    Consumer to run for the annotated XML file contents.
     *                    Can be used to display scheme violations.
     *                    <b>Will only be used if {@code detailedLog} is set to {@code true}!</b>
     * @param detailedLog Flag whether the validation errors should be annotated and processed or not.
     * @throws XmlValidationException In case the validation for the given file fails.
     */
    public static void validateXML(final Schema schema, final String xmlContent, final Consumer<String> consumer,
                                   final boolean detailedLog) throws XmlValidationException {
        Objects.requireNonNull(xmlContent, "XML contents may not be null.");

        final XMLValidation xmlValidation = new XMLValidation(schema);
        final Collection<LogValidator.ErrorLocation> errorLocations =
                xmlValidation.validateXML(xmlContent, StandardCharsets.UTF_8);
        final boolean valid = errorLocations.isEmpty();

        if (!valid) {
            final String additionalInformation;
            if (detailedLog) {
                additionalInformation = "Check the result of the used consumer for more information.";

                final String annotateXMLContent = LogErrors.annotateXMLContent(xmlContent, errorLocations);
                if (!annotateXMLContent.isEmpty()) {
                    consumer.accept(annotateXMLContent);
                }
            } else {
                additionalInformation = "Set the 'detailedLog' flag to obtain more information.";
            }

            throw new XmlValidationException("Schema validation failed! " + additionalInformation);
        }
    }

    private static InputStream toInputStream(final String input, final Charset charset) {
        return new ByteArrayInputStream(input.getBytes(charset));
    }

    private LogValidator createValidator() {
        final Validator validator = schema.newValidator();
        return new LogValidator(validator);
    }

}
