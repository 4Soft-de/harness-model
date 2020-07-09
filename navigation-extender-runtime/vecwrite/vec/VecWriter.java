/*-
 * ========================LICENSE_START=================================
 * vec113
 * %%
 * Copyright (C) 2020 4Soft GmbH
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
package com.foursoft.vecmodel.vec120;

import java.io.*;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class VecWriter {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(VecWriter.class.getName());

    private VecWriter() {
    }

    public static boolean writeVec(final String fileName, final VecContent container) {
        final File file = new File(fileName);
        return writeVec(file, container);
    }

    public static boolean writeVec(final File file, final VecContent container) {
        try {
            final OutputStream os = new FileOutputStream(file);
            writeVec(container, os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Returns the VecContent as a XML string.
     *
     * @param container VecContainer used as the jaxbElement to marshal its content tree into a Writer
     * @return Never null String containing the content tree of the given container
     * @throws DataBindingException in case of a JAXBException
     */
    public static String writeVec(final VecContent container) {
        final StringWriter sw = new StringWriter();
        return writeVec(container, sw);
    }

    public static void writeVec(final VecContent container, final OutputStream os) {
        Writer wr = new OutputStreamWriter(os);
        writeVec(container, wr);
    }

    private static String writeVec(final VecContent container, final Writer wr)  {
        try {
            final JAXBContext jc = JAXBContext.newInstance(VecContent.class);
            final Marshaller mc = jc.createMarshaller();
            mc.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            mc.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            mc.marshal(container, wr);
        } catch (final JAXBException me) {
            logger.error("Error writing Vec.", me);
            throw new DataBindingException(me);
        }
        return wr.toString();
    }

}
