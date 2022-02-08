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

import com.foursoft.xml.io.TestData;
import com.foursoft.xml.io.validation.LogValidator.ErrorLocation;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;

import static com.foursoft.xml.io.validation.XMLValidationTest.getXmlValidation;
import static org.assertj.core.api.Assertions.assertThat;

class LogErrorsTest {

    @Test
    void logXmlString() throws Exception {
        final XMLValidation xmlValidation = getXmlValidation();

        final String content = new String(
                Files.readAllBytes(TestData.VALIDATE_BASE_PATH_SRC.resolve(TestData.ERROR_TEST_XML)));
        final Collection<ErrorLocation> errors = xmlValidation.validateXML(content,
                StandardCharsets.UTF_8);
        final String errorString = LogErrors.annotateXMLContent(content, errors);
        // line 21 contains a duplicate key
        assertThat(errorString).contains("21: ERROR     <ChildB id=\"id_8\">");
    }
}
