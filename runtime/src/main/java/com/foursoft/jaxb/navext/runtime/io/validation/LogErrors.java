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
package com.foursoft.jaxb.navext.runtime.io.validation;

import com.foursoft.jaxb.navext.runtime.io.validation.LogValidator.ErrorLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * helper class to log  {@link LogValidator.ErrorLocation}
 */
public final class LogErrors {

    private LogErrors() {

    }

    /**
     * adds all error messages to the xml content
     *
     * @param xmlContent the xml content containing errors
     * @param errorLines the detected errors
     * @return the annotated xml content
     */
    public static String annotateXMLContent(final String xmlContent,
                                            final Collection<ErrorLocation> errorLines) {
        if (errorLines.isEmpty()) {
            return xmlContent;
        }
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
        return enhanced.toString();

    }

    private static Map<Integer, String> prepareErrors(final Collection<LogValidator.ErrorLocation> errorLines) {
        final Map<Integer, String> errorMap = new HashMap<>();

        for (final LogValidator.ErrorLocation errorLine : errorLines) {
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
}
