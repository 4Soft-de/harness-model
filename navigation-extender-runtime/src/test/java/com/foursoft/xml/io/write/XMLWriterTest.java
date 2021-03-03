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
package com.foursoft.xml.io.write;

import com.foursoft.test.model.ChildA;
import com.foursoft.test.model.ChildB;
import com.foursoft.test.model.Root;
import com.foursoft.xml.io.TestData;
import com.foursoft.xml.io.utils.ValidationEventCollector;
import com.foursoft.xml.io.write.xmlmeta.XMLMeta;
import com.foursoft.xml.io.write.xmlmeta.comments.Comments;
import com.foursoft.xml.io.write.xmlmeta.processinginstructions.ProcessingInstruction;
import com.foursoft.xml.io.write.xmlmeta.processinginstructions.ProcessingInstructions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class XMLWriterTest {

    @Test
    void writeToString() {
        final Root root = TestData.readBasicTest();
        final ValidationEventCollector validationEventCollector = new ValidationEventCollector();
        final XMLWriter<Root> xmlWriter = new XMLWriter<>(Root.class, validationEventCollector);
        final String result = xmlWriter.writeToString(root);
        assertFalse(validationEventCollector.hasEvents(), "Should produce no errors!");
        Assertions.assertThat(result).contains("anotherAttribute").contains("\"Some Information\"");
    }

    @Test
    void writeToStringWithComments() {
        final Root root = TestData.readBasicTest();
        final ValidationEventCollector validationEventCollector = new ValidationEventCollector();
        final XMLWriter<Root> xmlWriter = new XMLWriter<>(Root.class, validationEventCollector);
        final Comments comments = new Comments();
        final String expectedComment = "Blafasel";
        comments.put(root.getChildA().get(0), expectedComment);
        comments.put(root, "Hello World");
        final XMLMeta xmlMeta = new XMLMeta();
        xmlMeta.setComments(comments);
        final ProcessingInstructions processingInstructions = new ProcessingInstructions();
        processingInstructions.put(root, new ProcessingInstruction("pc", "pc test"));
        processingInstructions.put(root.getChildA().get(1), new ProcessingInstruction("pc", "pc test 2"));
        xmlMeta.setProcessingInstructions(processingInstructions);
        final String result = xmlWriter.writeToString(root, xmlMeta);
        assertFalse(validationEventCollector.hasEvents(), "Should produce no errors!");
        Assertions.assertThat(result).contains(expectedComment);
        // check for correct new lines
        Assertions.assertThat(result)
                .startsWith("<?xml version=\"1.0\" ?>\n" +
                        "<!--Hello World-->\n" +
                        "<?pc pc test?>\n" +
                        "<Root id=\"id_1\">")
                .contains(">\n" +
                        "<?pc pc test 2?>\n" +
                        "  <")
                .doesNotContain("\n\n");
    }

    @Test
    void writeToStringDefaultLogger() {
        final Root root = TestData.readBasicTest();
        final XMLWriter<Root> xmlWriter = new XMLWriter<>(Root.class);
        final String result = xmlWriter.writeToString(root);
        Assertions.assertThat(result).contains("anotherAttribute").contains("\"Some Information\"");
    }

    @Test
    void writeToStringError() {
        final Root root = TestData.readBasicTest();

        final ChildA childA = new ChildA();
        childA.getReferencedChildB().add(new ChildB());
        root.getChildA().add(childA);

        final XMLWriter<Root> xmlWriter = new XMLWriter<>(Root.class);
        assertThrows(NullPointerException.class, () -> xmlWriter.writeToString(root),
                "Jaxb throws a nullpointer exception if the model is invalid");

    }

    @Test
    void writeToOutputStream() throws IOException {
        final Root root = TestData.readBasicTest();
        final ValidationEventCollector validationEventCollector = new ValidationEventCollector();
        final XMLWriter<Root> xmlWriter = new XMLWriter<>(Root.class, validationEventCollector);
        try (final ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream()) {
            xmlWriter.write(root, byteOutputStream);
            final String result = byteOutputStream.toString();
            assertFalse(validationEventCollector.hasEvents(), "Should produce no errors!");
            Assertions.assertThat(result).contains("anotherAttribute").contains("\"Some Information\"");
        }
    }

    @Test
    void writeToOutputStreamWithComments() throws IOException {
        final Root root = TestData.readBasicTest();
        final ValidationEventCollector validationEventCollector = new ValidationEventCollector();
        final XMLWriter<Root> xmlWriter = new XMLWriter<>(Root.class, validationEventCollector);
        final XMLMeta meta = new XMLMeta();
        final Comments comments = new Comments();
        meta.setComments(comments);
        final String expectedComment = "Blafasel";
        comments.put(root.getChildA().get(0), expectedComment);

        final ProcessingInstructions processingInstructions = new ProcessingInstructions();
        final ProcessingInstruction processingInstruction = new ProcessingInstruction("pc", "\"checksum=sum\"");
        processingInstructions.put(root, processingInstruction);
        meta.setProcessingInstructions(processingInstructions);

        try (final ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream()) {
            xmlWriter.write(root, meta, byteOutputStream);
            final String result = byteOutputStream.toString();
            assertFalse(validationEventCollector.hasEvents(), "Should produce no errors!");
            Assertions.assertThat(result).contains(expectedComment);
            Assertions.assertThat(result).contains(processingInstruction.getData());
        }
    }

}
