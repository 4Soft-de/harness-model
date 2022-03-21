package com.foursoft.jaxb.navext.runtime.io.write.xmlmeta.processinginstructions;/*-
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

import com.foursoft.xml.io.write.xmlmeta.XMLMetaAwareXMLStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.Marshaller.Listener;
import javax.xml.stream.XMLStreamException;

public class ProcessingInstructionAdderListener extends Listener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingInstructionAdderListener.class);
    private final XMLMetaAwareXMLStreamWriter xsw;
    private final ProcessingInstructions processingInstructions;

    /**
     * @param xsw                    the xml stream writer
     * @param processingInstructions list of xjc objects and comment strings
     */
    public ProcessingInstructionAdderListener(final XMLMetaAwareXMLStreamWriter xsw, final ProcessingInstructions processingInstructions) {
        this.xsw = xsw;
        this.processingInstructions = processingInstructions;
    }

    @Override
    public void beforeMarshal(final Object source) {
        processingInstructions.get(source).forEach(processingInstruction -> {
            final String target = processingInstruction.getTarget();
            final String data = processingInstruction.getData();

            try {
                xsw.writeProcessingInstruction(target, data, source);
            } catch (final XMLStreamException e) {
                LOGGER.warn("Ignored Exception while writing processingInstruction:", e);
            }
        });
    }

}
