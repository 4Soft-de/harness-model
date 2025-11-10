/*-
 * ========================LICENSE_START=================================
 * KBL Common
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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
package com.foursoft.harness.kbl.common.validation;

import com.foursoft.harness.kbl.common.exception.KblException;
import com.foursoft.harness.navext.runtime.io.utils.XMLIOException;
import com.foursoft.harness.navext.runtime.io.validation.XMLValidation;
import com.foursoft.harness.navext.runtime.io.validation.XmlValidationException;

import javax.xml.validation.Schema;
import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * Validate KBL data.
 *
 * @deprecated Use {@link XMLValidation} instead.
 */
@Deprecated(forRemoval = true)
public class KblValidation {

    private KblValidation() {
        // hide constructor
    }

    /**
     * validates a xml string against the kbl schema
     *
     * @param schema      the kbl schema
     * @param kblPath     path to kbl
     * @param consumer    to display scheme violations.
     * @param detailedLog if true and error happens a detailed log is written, use always true in tests !
     * @deprecated Use {@link XMLValidation#validateXML(Schema, Path, Consumer)} instead.
     */
    @Deprecated(forRemoval = true)
    public static void validateXML(final Schema schema, final Path kblPath, final Consumer<String> consumer,
                                   final boolean detailedLog) {
        try {
            XMLValidation.validateXML(schema, kblPath, detailedLog ? consumer : null);
        } catch (final XMLIOException | XmlValidationException e) {
            throw new KblException("Schema validation failed! Could not read Path: " + kblPath, e);
        }
    }

    /**
     * validates a xml string against the kbl schema
     *
     * @param schema      the kbl schema
     * @param xmlContent  the xml content
     * @param consumer    to display scheme violations.
     * @param detailedLog if true and error happens a detailed log is written, use always true in tests !
     * @deprecated Use {@link XMLValidation#validateXML(Schema, String, Consumer)} instead.
     */
    @Deprecated(forRemoval = true)
    public static void validateXML(final Schema schema, final String xmlContent, final Consumer<String> consumer,
                                   final boolean detailedLog) {
        try {
            XMLValidation.validateXML(schema, xmlContent, detailedLog ? consumer : null);
        } catch (final XmlValidationException e) {
            throw new KblException("Schema validation failed! Use detailedLog for more information");
        }
    }

}
