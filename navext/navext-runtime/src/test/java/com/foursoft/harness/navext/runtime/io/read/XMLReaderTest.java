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
package com.foursoft.harness.navext.runtime.io.read;

import com.foursoft.harness.navext.runtime.io.TestData;
import com.foursoft.harness.navext.runtime.io.utils.XMLIOException;
import com.foursoft.harness.navext.runtime.model.Root;
import jakarta.xml.bind.ValidationEvent;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.foursoft.harness.navext.runtime.io.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class XMLReaderTest {

    @Test
    void testReadInputStream() {
        final Root read = readBasicXml();
        assertThat(read)
                .isNotNull();
    }

    @Test
    void testReadWrongFile() {
        final String fileName = Paths.get("unknown-test.xml").toString();
        final TestXMLReader reader = new TestXMLReader();
        assertThatExceptionOfType(XMLIOException.class)
                .isThrownBy(() -> reader.read(fileName));
    }

    @Test
    void testReadDocument() throws ParserConfigurationException, IOException, SAXException {
        final InputStream inputStream = TestData.getInputStream(BASIC_BASIC_TEST_XML);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder builder = documentBuilderFactory
                .newDocumentBuilder();

        Document document = builder.parse(inputStream);
        final TestXMLReader reader = new TestXMLReader();
        final Root read = reader.read(document);
        assertThat(read)
                .isNotNull();
    }

    @Test
    void testReadFile() {
        final String fileName = Paths.get("src/test/resources/")
                .resolve(BASIC_BASIC_TEST_XML)
                .toString();
        final TestXMLReader reader = new TestXMLReader();
        final Root read = reader.read(fileName);
        assertThat(read)
                .isNotNull();
    }

    @Test
    void testReadInputStreamConsumer() {
        final List<ValidationEvent> events = new ArrayList<>();
        final Consumer<ValidationEvent> myConsumer = events::add;
        final TestXMLReader reader = new TestXMLReader(myConsumer);
        final InputStream inputStream = TestData.getInputStream(BASIC_BASIC_TEST_XML);

        final Root read = reader.read(inputStream);
        assertThat(read)
                .isNotNull();
        assertThat(events)
                .isEmpty();
    }

    @Test
    void testReadInputStreamConsumerError() {
        final List<ValidationEvent> events = new ArrayList<>();
        final Consumer<ValidationEvent> myConsumer = events::add;
        final TestXMLReader reader = new TestXMLReader(myConsumer);
        final InputStream inputStream = TestData.getInputStream(BASIC_DUPLICATE_ELEMENT_TEST_XML);

        final Root read = reader.read(inputStream);
        assertThat(read)
                .isNotNull();
        assertThat(events)
                // The caught error is actually not for the duplicated but because id_9 is now defined nowhere.
                .isNotEmpty()
                .hasSize(1)
                .singleElement()
                .returns(ValidationEvent.ERROR, ValidationEvent::getSeverity)
                .returns(true, e -> e.getMessage().contains("id_9"));
    }

}
