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
package com.foursoft.vecmodel.vec113;

import com.foursoft.xml.model.Identifiable;
import com.foursoft.vecmodel.vec113.common.EventConsumer;
import com.foursoft.xml.ExtendedUnmarshaller;
import com.foursoft.xml.JaxbModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

public final class VecReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(VecReader.class);

    private static final ThreadLocal<VecReader> localReader = ThreadLocal.withInitial(VecReader::new);

    private final ExtendedUnmarshaller<VecContent, Identifiable> unmarshaller;

    private VecReader() {
        try {
            unmarshaller = new ExtendedUnmarshaller<VecContent, Identifiable>(VecContent.class)
                    .withBackReferences()
                    .withIdMapper(Identifiable.class, Identifiable::getXmlId)
                    .withEventLogging(new EventConsumer());
        } catch (final Exception e) {
            throw new IllegalArgumentException("Cannot initialize VEC unmarshaller", e);
        }
    }

    /**
     * Builds the complete JAXB tree structure of a Vec xml file.
     *
     * @param filename the path name of the VEC file.
     * @return the JAXB object structure representing the KBL.
     * @throws DataBindingException in case of an IOException
     */
    public static VecContent read(final String filename) {
        try (final InputStream is = new BufferedInputStream(Files.newInputStream(Paths.get(filename)))) {
            return read(is);
        } catch (final IOException e) {
            throw new DataBindingException(e);
        }
    }

    /**
     * Builds the complete JAXB tree structure of a Vec xml file.
     *
     * @return the JAXB object structure representing the VEC.
     * @throws DataBindingException in case of a JAXBException
     */
    public static VecContent read(final InputStream inputStream) {
        return readModel(inputStream).getRootElement();
    }

    public static VecContent read(final InputStream inputStream, final Consumer<ValidationEvent> eventConsumer) {
        return readModel(inputStream, eventConsumer).getRootElement();
    }

    public static JaxbModel<VecContent, Identifiable> readModel(final InputStream inputStream,
                                                                final Consumer<ValidationEvent> eventConsumer) {
        try {
            localReader.get().unmarshaller.withEventLogging(eventConsumer);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Cannot update VEC unmarshaller", e);
        }

        return readModel(inputStream);
    }

    public static JaxbModel<VecContent, Identifiable> readModel(final InputStream inputStream) {
        try {
            final long start = System.currentTimeMillis();
            LOGGER.info("Start loading VEC file.");
            final JaxbModel<VecContent, Identifiable> result = localReader.get().unmarshaller.unmarshall(inputStream);
            LOGGER.info("Finished loading VEC file. Took {} ms", System.currentTimeMillis() - start);
            return result;
        } catch (final JAXBException e) {
            // Behaviour consistent to JAXB.unmarshall();
            throw new DataBindingException(e);
        }
    }
}
