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
package com.foursoft.xml.io.read;

import com.foursoft.xml.io.TestData;
import com.foursoft.xml.io.utils.XMLIOException;
import com.foursoft.xml.model.AbstractBase;
import com.foursoft.xml.model.Root;
import jakarta.xml.bind.ValidationEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.foursoft.xml.io.TestData.BASIC_TEST_XML;

class XMLReaderTest {

    @Test
    void readInputStream() {
        final TestXMLReader reader = new TestXMLReader();
        final InputStream inputStream = getClass().getResourceAsStream("/basic/" + BASIC_TEST_XML);
        final Root read = reader.read(inputStream);
        Assertions.assertNotNull(read, "read must be not null");
    }

    @Test
    void testReadWrongFile() {
        final String fileName = TestData.BASIC_BASE_PATH.resolve("unknown-test.xml").toAbsolutePath().toString();
        final TestXMLReader reader = new TestXMLReader();
        Assertions.assertThrows(XMLIOException.class, () -> reader.read(fileName));
    }

    @Test
    void testReadFile() {
        final String fileName = TestData.BASIC_BASE_PATH.resolve(BASIC_TEST_XML).toAbsolutePath().toString();
        final TestXMLReader reader = new TestXMLReader();
        final Root read = reader.read(fileName);
        Assertions.assertNotNull(read, "read must be not null");
    }

    @Test
    void testReadInputStreamConsumer() {
        final List<ValidationEvent> events = new ArrayList<>();
        final Consumer<ValidationEvent> myConsumer = events::add;
        final TestXMLReader reader = new TestXMLReader(myConsumer);
        final InputStream inputStream = getClass().getResourceAsStream("/basic/" + BASIC_TEST_XML);
        final Root read = reader.read(inputStream);
        Assertions.assertNotNull(read, "read must be not null");
        Assertions.assertEquals(0, events.size(), "There should be no validation events");
    }

    @Test
    void testReadInputStreamConsumerError() {
        final List<ValidationEvent> events = new ArrayList<>();
        final Consumer<ValidationEvent> myConsumer = events::add;
        final TestXMLReader reader = new TestXMLReader(myConsumer);
        final InputStream inputStream = getClass().getResourceAsStream("/basic/" + "error-test.xml");

        final Root read = reader.read(inputStream);
        Assertions.assertNotNull(read, "read must be not null");
        Assertions.assertEquals(1, events.size(), "There should be one validation events");
    }

    class TestXMLReader extends XMLReader<Root, AbstractBase> {
        TestXMLReader() {
            super(Root.class, AbstractBase.class, AbstractBase::getXmlId);
        }

        TestXMLReader(final Consumer<ValidationEvent> consumer) {
            super(Root.class, AbstractBase.class, AbstractBase::getXmlId, consumer);
        }
    }
}
