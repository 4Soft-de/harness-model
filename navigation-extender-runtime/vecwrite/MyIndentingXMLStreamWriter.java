package com.volkswagenag.daem.converter.vec.c2v.vecwrite;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * with comments the formatting doesn't work, this adds the formatting back,
 * there is no Java API, its now part of Jaxb!
 */
public class MyIndentingXMLStreamWriter extends com.sun.xml.txw2.output.IndentingXMLStreamWriter {

    MyIndentingXMLStreamWriter(final XMLStreamWriter xmlStreamWriter) {
        super(xmlStreamWriter);
    }

    @Override
    public void writeComment(final String data)
            throws XMLStreamException {

        writeCharacters("\n");
        super.writeComment(data);
    }
}
