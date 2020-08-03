/*-
 * ========================LICENSE_START=================================
 * navigation-extender-runtime
 * %%
 * Copyright (C) 2019 - 2020 4Soft GmbH
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
package com.foursoft.xml.io.validation;

import com.foursoft.xml.io.utils.XMLIOException;
import com.foursoft.xml.io.validation.LogValidator.ErrorLocation;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;

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

    private static InputStream toInputStream(final String input, final Charset charset) {
        return new ByteArrayInputStream(input.getBytes(charset));
    }

    private LogValidator createValidator() {
        final Validator validator = schema.newValidator();
        return new LogValidator(validator);
    }

}
