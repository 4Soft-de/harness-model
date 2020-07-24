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
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.transform.Source;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * a wrapper around a {@link Validator} which logs all error events in an array
 */
public class LogValidator {
    private final Validator validator;
    private final List<ErrorLocation> errorLines;
    private boolean isValid;

    public LogValidator(final Validator validator) {
        errorLines = new ArrayList<>();
        this.validator = validator;
        isValid = true;
        validator.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(final SAXParseException exception) {
                errorLines.add(new ErrorLocation(exception.getLineNumber(), exception.getMessage()));
                isValid = false;
            }

            @Override
            public void fatalError(final SAXParseException exception) {
                errorLines.add(new ErrorLocation(exception.getLineNumber(), exception.getMessage()));
                isValid = false;
            }

            @Override
            public void error(final SAXParseException exception) {
                errorLines.add(new ErrorLocation(exception.getLineNumber(), exception.getMessage()));
                isValid = false;
            }
        });
    }

    /**
     * @return all collected errors
     */
    public Collection<ErrorLocation> getErrorLines() {
        return errorLines;
    }

    /**
     * runs the validation
     *
     * @param source the source to validate
     * @return true if no validation errors were collected
     */
    public boolean validate(final Source source) {
        isValid = true;
        try {
            validator.validate(source);
        } catch (final SAXException | IOException e) {
            throw new XMLIOException(e.getMessage(), e);
        }
        return isValid;
    }

    /**
     * ErrorLocation holds the line and message of the found error
     */
    public static class ErrorLocation {
        public final int line;
        public final String message;

        public ErrorLocation(final int line, final String message) {
            this.line = line;
            this.message = message;
        }

        @Override public String toString() {
            return "ErrorLocation{" +
                    "line=" + line +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
