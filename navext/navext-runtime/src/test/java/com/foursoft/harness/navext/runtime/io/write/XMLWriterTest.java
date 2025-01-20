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
package com.foursoft.harness.navext.runtime.io.write;

import com.foursoft.harness.navext.runtime.io.TestData;
import com.foursoft.harness.navext.runtime.io.utils.ValidationEventCollector;
import com.foursoft.harness.navext.runtime.io.write.xmlmeta.XMLMeta;
import com.foursoft.harness.navext.runtime.io.write.xmlmeta.comments.Comments;
import com.foursoft.harness.navext.runtime.io.write.xmlmeta.processinginstructions.ProcessingInstruction;
import com.foursoft.harness.navext.runtime.io.write.xmlmeta.processinginstructions.ProcessingInstructions;
import com.foursoft.harness.navext.runtime.model.ChildA;
import com.foursoft.harness.navext.runtime.model.ChildB;
import com.foursoft.harness.navext.runtime.model.Root;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class XMLWriterTest {

    @Test
    void writeToString() {
        final Root root = TestData.readBasicXml();
        final ValidationEventCollector validationEventCollector = new ValidationEventCollector();
        final XMLWriter<Root> xmlWriter = new XMLWriter<>(Root.class, validationEventCollector);
        final String result = xmlWriter.writeToString(root);

        assertThat(validationEventCollector.hasEvents())
                .isFalse();
        assertThat(result)
                .contains("anotherAttribute")
                .contains("\"Some Information\"");
    }

    @Test
    void writeToStringWithCommentSimple() {
        final XMLWriter<Root> xmlWriter = new XMLWriter<>(Root.class);
        final Root root = new Root();
        final XMLMeta xmlMeta = new XMLMeta();
        final Comments comments = new Comments();
        comments.put(root, "TestComment");
        xmlMeta.setComments(comments);
        final String content = xmlWriter.writeToString(root, xmlMeta);
        assertThat(content)
                .contains("" +  // Empty string here for a better readability of the indentation of the expected String.
                                  "<!--TestComment-->\n" +
                                  "<Root></Root>");
    }

    @Test
    void writeToStringWithComments() {
        final Root root = TestData.readBasicXml();
        final ValidationEventCollector validationEventCollector = new ValidationEventCollector();
        final XMLWriter<Root> xmlWriter = new XMLWriter<>(Root.class, validationEventCollector);
        final Comments comments = new Comments();
        final String expectedComment = "Blafasel";
        comments.put(root.getChildA().get(0), expectedComment);
        comments.put(root, "Hello -- World");
        final XMLMeta xmlMeta = new XMLMeta();
        xmlMeta.setComments(comments);
        final ProcessingInstructions processingInstructions = new ProcessingInstructions();
        processingInstructions.put(root, new ProcessingInstruction("pc", "pc test"));
        processingInstructions.put(root.getChildA().get(1), new ProcessingInstruction("pc", "pc test 2"));
        xmlMeta.setProcessingInstructions(processingInstructions);
        final String result = xmlWriter.writeToString(root, xmlMeta);

        assertThat(validationEventCollector.hasEvents())
                .isFalse();
        assertThat(result)
                .contains(expectedComment)
                // check for correct new lines
                .startsWith("<?xml version=\"1.0\" ?>\n" +
                                    "<?pc pc test?>\n" +
                                    "<!--Hello - - World-->\n" +
                                    "<Root id=\"id_1\">")
                .contains("<?pc pc test 2?>\n" +
                                  "  <ChildA id=\"id_3\">")
                .doesNotContain("\n\n");
    }

    @Test
    void writeToStringDefaultLogger() {
        final Root root = TestData.readBasicXml();
        final XMLWriter<Root> xmlWriter = new XMLWriter<>(Root.class);
        final String result = xmlWriter.writeToString(root);
        assertThat(result)
                .contains("anotherAttribute")
                .contains("\"Some Information\"");
    }

    @Test
    void writeToStringError() {
        final Root root = TestData.readBasicXml();

        final ChildA childA = new ChildA();
        childA.getReferencedChildB().add(new ChildB());
        root.getChildA().add(childA);

        final XMLWriter<Root> xmlWriter = new XMLWriter<>(Root.class);
        // JAXB throws an NPE if the model is invalid.
        assertThatNullPointerException()
                .isThrownBy(() -> xmlWriter.writeToString(root));
    }

    @Test
    void writeToOutputStream() throws IOException {
        final Root root = TestData.readBasicXml();
        final ValidationEventCollector validationEventCollector = new ValidationEventCollector();
        final XMLWriter<Root> xmlWriter = new XMLWriter<>(Root.class, validationEventCollector);
        try (final ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream()) {
            xmlWriter.write(root, byteOutputStream);
            final String result = byteOutputStream.toString();

            assertThat(validationEventCollector.hasEvents())
                    .isFalse();
            assertThat(result)
                    .contains("anotherAttribute")
                    .contains("\"Some Information\"");
        }
    }

    @Test
    void writeToOutputStreamWithComments() throws IOException {
        final Root root = TestData.readBasicXml();
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
            assertThat(validationEventCollector.hasEvents())
                    .isFalse();
            assertThat(result)
                    .contains(expectedComment)
                    .contains(processingInstruction.getData());
        }
    }

}
