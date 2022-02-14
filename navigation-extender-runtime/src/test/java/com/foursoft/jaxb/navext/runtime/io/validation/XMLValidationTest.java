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

import com.foursoft.jaxb.navext.runtime.io.TestData;
import com.foursoft.jaxb.navext.runtime.io.validation.LogValidator.ErrorLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.validation.Schema;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;

class XMLValidationTest {


    public static XMLValidation getXmlValidation() {
        final String fileName = TestData.VALIDATE_BASE_PATH.resolve("basic-test.xsd").toString();
        final Schema schema = SchemaFactory.getSchema(fileName);
        return new XMLValidation(schema);
    }

    @Test
    void validateXML() throws Exception {
        final XMLValidation xmlValidation = getXmlValidation();

        final String content = new String(
                Files.readAllBytes(TestData.VALIDATE_BASE_PATH_SRC.resolve(TestData.BASIC_TEST_XML)));

        final Collection<ErrorLocation> errors = xmlValidation.validateXML(content,
                StandardCharsets.UTF_8);
        Assertions.assertTrue(errors.isEmpty());
    }

    @Test
    void validateErrorXML() throws Exception {
        final XMLValidation xmlValidation = getXmlValidation();

        final String content = new String(
                Files.readAllBytes(TestData.VALIDATE_BASE_PATH_SRC.resolve(TestData.ERROR_TEST_XML)));

        final Collection<ErrorLocation> errors = xmlValidation.validateXML(content,
                StandardCharsets.UTF_8);
        Assertions.assertFalse(errors.isEmpty());
        Assertions.assertEquals(2, errors.size());

    }

}
