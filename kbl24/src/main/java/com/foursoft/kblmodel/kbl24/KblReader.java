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

import com.foursoft.xml.model.Identifiable;
import com.foursoft.xml.ExtendedUnmarshaller;
import com.foursoft.xml.JaxbModel;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KblReader {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(KblReader.class);

    private KblReader() {

    }

    /**
     * Builds the complete JAXB tree structure of a Kbl xml file.
     *
     * @param filename the path name of the KBL file.
     * @return the JAXB object structure representing the KBL.
     * @throws Exception
     */
    public static KBLContainer read(final String filename) {
        try (final InputStream is = new BufferedInputStream(Files.newInputStream(Paths.get(filename)))) {
            return read(is);
        } catch (final IOException e) {
            throw new DataBindingException(e);
        }
    }

    /**
     * Builds the complete JAXB tree structure of a KBL xml file.
     *
     * @return the JAXB object structure representing the KBL.
     * @throws Exception
     */
    public static KBLContainer read(final InputStream inputStream) {
        return readModel(inputStream).getRootElement();
    }

    public static JaxbModel<KBLContainer, Identifiable> readModel(final InputStream inputStream) {

        try {
            final long start = System.currentTimeMillis();
            LOGGER.info("Start loading KBL file.");
            final JaxbModel<KBLContainer, Identifiable> result = new ExtendedUnmarshaller<KBLContainer, Identifiable>(
                    KBLContainer.class).withBackReferences()
                    .withIdMapper(Identifiable.class, Identifiable::getXmlId)
                    .withEventLogging(event -> {
                        final String message = "Error while reading kbl file. {}";
                        if(event.getSeverity() == ValidationEvent.WARNING) {
                            LOGGER.warn(message, event);
                        } else { //ERROR || FATAL_ERROR
                            LOGGER.error(message, event);
                        }
                    })
                    .unmarshall(inputStream);
            LOGGER.info("Finished loading KBL file. Took {} ms", System.currentTimeMillis() - start);
            return result;
        } catch (final JAXBException e) {
            // Behaviour consistent to JAXB.unmarshall();
            throw new DataBindingException(e);
        }

    }
}
