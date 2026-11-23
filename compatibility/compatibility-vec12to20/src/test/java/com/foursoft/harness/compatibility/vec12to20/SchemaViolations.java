/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.2.X To VEC 2.0.X
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
package com.foursoft.harness.compatibility.vec12to20;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Collects all violations of an XML document against a schema instead of stopping at the first one, so that
 * a failing test shows every problem of a converted VEC at once.
 */
public final class SchemaViolations {

    private final Schema schema;

    /**
     * @param schema Schema to validate against.
     */
    public SchemaViolations(final Schema schema) {
        this.schema = schema;
    }

    /**
     * Validates the given document.
     *
     * @param xml Document to validate.
     * @return The messages of all errors, empty if the document is valid. Warnings are not reported.
     */
    public List<String> of(final String xml) {
        final List<String> violations = new ArrayList<>();
        final Validator validator = schema.newValidator();
        validator.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(final SAXParseException exception) {
                // Warnings do not make a document invalid.
            }

            @Override
            public void error(final SAXParseException exception) {
                violations.add(describe(exception));
            }

            @Override
            public void fatalError(final SAXParseException exception) {
                violations.add(describe(exception));
            }
        });
        try {
            validator.validate(new StreamSource(new StringReader(xml)));
        } catch (final SAXException e) {
            violations.add(e.getMessage());
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        return violations;
    }

    private String describe(final SAXParseException exception) {
        return "line " + exception.getLineNumber() + ": " + exception.getMessage();
    }

}
