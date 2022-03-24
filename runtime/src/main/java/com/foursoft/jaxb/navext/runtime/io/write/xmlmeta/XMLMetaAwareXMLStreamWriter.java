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
package com.foursoft.jaxb.navext.runtime.io.write.xmlmeta;

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * with meta the formatting doesn't work, this adds the formatting back.
 */
public class XMLMetaAwareXMLStreamWriter extends IndentingXMLStreamWriter {

    private final Class<?> baseType;

    public XMLMetaAwareXMLStreamWriter(final XMLStreamWriter xmlStreamWriter, final Class<?> baseType) {
        super(xmlStreamWriter);
        this.baseType = baseType;
    }

    @Override
    public void writeComment(final String data) throws XMLStreamException {
        writeComment(data, null);
    }

    public void writeComment(String data, Object source) throws XMLStreamException {
        boolean rootElement = isRootElement(source);
        if (rootElement)  {
            super.writeComment(data);
            writeNewline();
        }  else  {
            writeNewline();
            super.writeComment(data);
        }
    }

    @Override
    public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
        writeProcessingInstruction(target, data, null);
    }

    public void writeProcessingInstruction(String target, String data, Object source) throws XMLStreamException {
        boolean rootElement = isRootElement(source);
        if (rootElement) {
            super.writeProcessingInstruction(target, data);
            writeNewline();
        }  else  {
            writeNewline();
            super.writeProcessingInstruction(target, data);
        }
    }

    private void writeNewline() throws XMLStreamException {
        writeCharacters("\n"); // IndentingXMLStreamWriter uses \n
    }

    private boolean isRootElement(Object source)  {
        return source != null && baseType == source.getClass();
    }

}
