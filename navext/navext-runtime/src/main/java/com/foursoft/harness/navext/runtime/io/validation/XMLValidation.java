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
 * A helper class to validate a given xml string against an XSD schema.
 * <p>
 * All found errors can be returned in a collection or as a method throwing an exception in case there are errors.
 */
public final class XMLValidation {

    private final Schema schema;

    /**
     * Creates a new instance with the given schema.
     * <b>Notice:</b> The validator can use a lot of memory, because it holds the XML content multiple times in memory!
     *
     * @param schema The schema to validate XML contents against.
     */
    public XMLValidation(final Schema schema) {
        this.schema = schema;
    }

    /**
     * Validates the given XML string against the stored {@link Schema}.
     *
     * @param xmlContent Contents of an XML file which should be validated.
     * @param charset    {@link Charset} of the XML contents.
     * @return Possibly-empty Collection with {@link ErrorLocation validation error}s.
     * @throws XMLIOException In case validating the XML fails due to an unexpected error.
     *                        See {@link LogValidator#validate(Source)}.
     * @deprecated Use {@link #validateXML(String)} instead.
     * <b>Notice:</b> The given Charset will already be ignored, {@link StandardCharsets#UTF_8} will be used.
     * See <a href="https://github.com/4Soft-de/harness-model/issues/404">GitHub issue</a>.
     */
    @Deprecated(forRemoval = true)
    public Collection<ErrorLocation> validateXML(final String xmlContent, final Charset charset) throws XMLIOException {
        return validateXML(xmlContent);
    }

    /**
     * Validates the given UTF-8 encoded XML string against the stored {@link Schema}.
     *
     * @param xmlContent Contents of an XML file which should be validated.
     * @return Possibly-empty Collection with {@link ErrorLocation validation error}s.
     * @throws XMLIOException In case validating the XML fails due to an unexpected error.
     *                        See {@link LogValidator#validate(Source)}.
     */
    public Collection<ErrorLocation> validateXML(final String xmlContent) throws XMLIOException {
        final LogValidator validator = createValidator();
        final InputStream inputStream = toInputStream(xmlContent, StandardCharsets.UTF_8);
        final Source inputSource = new StreamSource(inputStream);

        try {
            final boolean valid = validator.validate(inputSource);
            return valid ? Collections.emptyList() : validator.getErrorLines();
        } catch (final XMLIOException e) {
            throw new XMLIOException("XML contains fatal errors, cannot read it.", e);
        }
    }

    /**
     * Validates the given XML file against the given {@link Schema}.
     *
     * @param schema      The schema to use for validation.
     * @param xmlFilePath Path to the file which should be validated.
     * @param consumer    Consumer to run for the annotated XML file contents.
     *                    Could be used to display or log scheme violations.
     *                    Can be {@code null} if the annotated XML file contents is not wanted / needed.
     * @throws XMLIOException         Due to one of the following cases:
     *                                <ul>
     *                                    <li>Reading the given file fails.</li>
     *                                    <li>Validating the XML fails due to an unexpected error.
     *                                    See {@link #validateXML(String, Charset)}.
     *                                    </li>
     *                                </ul>
     * @throws XmlValidationException In case the validation for the given file fails.
     */
    public static void validateXML(final Schema schema, final Path xmlFilePath, final Consumer<String> consumer)
            throws XMLIOException, XmlValidationException {

        final String xmlContent;
        try {
            xmlContent = Files.readString(xmlFilePath);
        } catch (final IOException e) {
            final String errorMsg = String.format("IOException occurred when trying to validate file '%s'.",
                                                  xmlFilePath);
            throw new XMLIOException(errorMsg, e);
        }

        validateXML(schema, xmlContent, consumer);
    }

    /**
     * Validates the given XML string against the given {@link Schema}.
     *
     * @param schema     The schema to use for validation.
     * @param xmlContent Contents of an XML file which should be validated.
     * @param consumer   Consumer to run for the annotated XML file contents.
     *                   Could be used to display or log scheme violations.
     *                   Can be {@code null} if the annotated XML file contents is not wanted / needed.
     * @throws XMLIOException         In case validating the XML fails due to an unexpected error.
     *                                See {@link #validateXML(String, Charset)}.
     * @throws XmlValidationException In case the validation for the given file fails.
     */
    public static void validateXML(final Schema schema, final String xmlContent, final Consumer<String> consumer)
            throws XMLIOException, XmlValidationException {
        Objects.requireNonNull(xmlContent, "XML contents may not be null.");

        final XMLValidation xmlValidation = new XMLValidation(schema);
        final Collection<LogValidator.ErrorLocation> errorLocations =
                xmlValidation.validateXML(xmlContent);
        final boolean valid = errorLocations.isEmpty();

        if (!valid) {
            final String additionalInformation;
            if (consumer != null) {
                additionalInformation = "Check the result of the used consumer for more information.";

                final String annotateXMLContent = LogErrors.annotateXMLContent(xmlContent, errorLocations);
                if (!annotateXMLContent.isEmpty()) {
                    consumer.accept(annotateXMLContent);
                }
            } else {
                additionalInformation = "Define a consumer to obtain more information.";
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
