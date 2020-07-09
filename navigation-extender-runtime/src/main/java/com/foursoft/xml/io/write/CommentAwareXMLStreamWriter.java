package com.foursoft.xml.io.write;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * with comments the formatting doesn't work, this adds the formatting back.
 */
public class CommentAwareXMLStreamWriter extends com.sun.xml.txw2.output.IndentingXMLStreamWriter {

    CommentAwareXMLStreamWriter(final XMLStreamWriter xmlStreamWriter) {
        super(xmlStreamWriter);
    }

    @Override
    public void writeComment(final String data)
            throws XMLStreamException {
        writeCharacters("\n"); // IndentingXMLStreamWriter uses \n
        super.writeComment(data);
    }
}
