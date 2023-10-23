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

import com.foursoft.harness.navext.runtime.io.TestData;
import com.foursoft.harness.navext.runtime.io.utils.XMLIOException;
import com.foursoft.harness.navext.runtime.io.validation.LogValidator.ErrorLocation;
import com.foursoft.harness.navext.runtime.io.write.XMLWriter;
import com.foursoft.harness.navext.runtime.io.write.xmlmeta.XMLMeta;
import com.foursoft.harness.navext.runtime.io.write.xmlmeta.comments.Comments;
import com.foursoft.harness.navext.runtime.model.Root;
import org.junit.jupiter.api.Test;

import javax.xml.validation.Schema;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;

import static org.assertj.core.api.Assertions.*;

class XMLValidationTest {

    public static XMLValidation getXmlValidation() {
        final Schema schema = TestData.getBasicSchema();
        return new XMLValidation(schema);
    }

    @Test
    void validateXML() throws IOException {
        final XMLValidation xmlValidation = getXmlValidation();

        final String content = new String(
                Files.readAllBytes(TestData.getPath(TestData.VALIDATE_BASIC_TEST_XML)));

        final Collection<ErrorLocation> errors = xmlValidation.validateXML(content,
                                                                           StandardCharsets.UTF_8);
        assertThat(errors)
                .isEmpty();
    }

    @Test
    void validateDuplicateXmlElementError() throws IOException {
        final XMLValidation xmlValidation = getXmlValidation();

        final String content = new String(
                Files.readAllBytes(TestData.getPath(TestData.VALIDATE_DUPLICATE_ELEMENT_TEST_XML)));

        final Collection<ErrorLocation> errors = xmlValidation.validateXML(content,
                                                                           StandardCharsets.UTF_8);
        assertThat(errors)
                .isNotEmpty()
                .hasSize(2)
                .allSatisfy(el -> assertThat(el)
                        .returns(21, x -> x.line)
                        .returns(true, x -> x.message.contains("id_8")));
    }

    @Test
    void validateDoubleHyphenInXmlCommentError() throws IOException {
        // Validate that no error is thrown when writing an Object with a double hyphen in an XML comment.
        final Root root = TestData.readBasicXml();
        final XMLWriter<Root> xmlWriter = new XMLWriter<>(Root.class);

        final XMLMeta xmlMeta = new XMLMeta();
        final Comments comments = new Comments();
        comments.put(root, "Hello -- World");
        xmlMeta.setComments(comments);
        final String result = xmlWriter.writeToString(root, xmlMeta);
        assertThat(result).contains("Hello - - World");

        final XMLValidation xmlValidation = getXmlValidation();
        assertThatNoException()
                .isThrownBy(() -> xmlValidation.validateXML(result, StandardCharsets.UTF_8));

        // Validate that an error is thrown when validating an XML string with a double hyphen in an XML comment.
        final String content = new String(
                Files.readAllBytes(TestData.getPath(TestData.VALIDATE_DOUBLE_HYPHEN_TEST_XML)));

        assertThatExceptionOfType(XMLIOException.class)
                .isThrownBy(() -> xmlValidation.validateXML(content, StandardCharsets.UTF_8))
                .withCauseInstanceOf(XMLIOException.class)
                .havingCause()
                .withMessageContaining("--");
    }

}
