/*-
 * ========================LICENSE_START=================================
 * vec-v2x
 * %%
 * Copyright (C) 2020 - 2022 4Soft GmbH
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
package com.foursoft.harness.vec.v2x.validation;

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
