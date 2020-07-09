/*-
 * ========================LICENSE_START=================================
 * kbl24
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
package com.foursoft.kblmodel.kbl24;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.OutputStream;
import java.io.StringWriter;

public class KblWriter {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(KblWriter.class.getName());

    private KblWriter() {
    }

    public static boolean writeKbl(final String fileName, final KBLContainer container) {
        final File file = new File(fileName);
        return writeKbl(file, container);
    }

    public static boolean writeKbl(final File file, final KBLContainer container) {

        try {
            final JAXBContext jc = JAXBContext.newInstance(KBLContainer.class);
            final Marshaller mc = jc.createMarshaller();
            mc.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            mc.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            mc.marshal(container, file);
        } catch (final JAXBException me) {
            logger.error("Error writing Kbl.", me); //$NON-NLS-1$
            return false;
        }
        return true;
    }

    /**
     * Gibt den Kbl content als XML-String zurück
     *
     * @param container
     * @return
     */
    public static String writeKbl(final KBLContainer container) {
        final StringWriter sw = new StringWriter();
        try {
            final JAXBContext jc = JAXBContext.newInstance(KBLContainer.class);
            final Marshaller mc = jc.createMarshaller();
            mc.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            mc.marshal(container, sw);
        } catch (final JAXBException me) {
            logger.error("Error writing Kbl.", me);
            throw new DataBindingException(me);
        }
        return sw.toString();
    }

    public static void writeKbl(final KBLContainer container, final OutputStream os) {
        try {
            final JAXBContext jc = JAXBContext.newInstance(KBLContainer.class);
            final Marshaller mc = jc.createMarshaller();
            mc.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            mc.marshal(container, os);
        } catch (final JAXBException me) {
            logger.error("Error writing Kbl.", me);
            throw new DataBindingException(me);
        }
    }
}
