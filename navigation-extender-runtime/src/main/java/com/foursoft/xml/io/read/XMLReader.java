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
package com.foursoft.xml.io.read;

import com.foursoft.xml.ExtendedUnmarshaller;
import com.foursoft.xml.JaxbModel;
import com.foursoft.xml.io.utils.ValidationEventLogger;
import com.foursoft.xml.io.utils.XMLIOException;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.ValidationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A generic xml reader which includes the back references, id mapper and a simple event log.
 *
 * @param <T> The root class to deserialize
 * @param <I> The class to identify xml objects
 */
public class XMLReader<T, I> {
    private static final Logger LOGGER = LoggerFactory.getLogger(XMLReader.class);
    private final ExtendedUnmarshaller<T, I> unmarshaller;

    /**
     * It will log warnings, errors and fatal validation events to the slf4j, see @{@link ValidationEventLogger}
     *
     * @param rootTypeClass     the root class of the jaxb model
     * @param identifiableClass the class of the identifiable element
     * @param idMapper          the mapper function to get the id of each element
     * @see ExtendedUnmarshaller
     */
    public XMLReader(final Class<T> rootTypeClass,
                     final Class<I> identifiableClass,
                     final Function<I, String> idMapper) {
        this(rootTypeClass, identifiableClass, idMapper, new ValidationEventLogger());
    }

    /**
     * @param rootTypeClass           the root class of the jaxb model
     * @param identifiableClass       the class of the identifiable element
     * @param idMapper                the mapper function to get the id of each element
     * @param validationEventConsumer a consumer for validation events
     * @see ExtendedUnmarshaller
     */
    public XMLReader(final Class<T> rootTypeClass,
                     final Class<I> identifiableClass,
                     final Function<I, String> idMapper,
                     final Consumer<ValidationEvent> validationEventConsumer) {
        try {
            unmarshaller = new ExtendedUnmarshaller<T, I>(rootTypeClass)
                    .withBackReferences()
                    .withIdMapper(identifiableClass, idMapper)
                    .withEventLogging(validationEventConsumer);
        } catch (final Exception e) {
            throw new XMLIOException("Cannot initialize unmarshaller.", e);
        }
    }

    /**
     * Builds the complete JAXB tree structure of an xml file.
     * Uses the already registered event consumer for validation events
     *
     * @param filename the path name of the XML file.
     * @return the JAXB object structure representing the xml file
     * @throws XMLIOException in case of an IOException
     */
    public T read(final String filename) {
        Objects.requireNonNull(filename);
        final Path path = Paths.get(filename).toAbsolutePath();
        if (!Files.exists(path)) {
            throw new XMLIOException("Given path: " + path + " doesn't exists.");
        }
        if (!Files.isReadable(path)) {
            throw new XMLIOException("Given path: " + path + " isn't readable.");
        }
        try (final InputStream is = new BufferedInputStream(Files.newInputStream(path))) {
            return read(is);
        } catch (final IOException e) {
            throw new XMLIOException("Error reading file " + filename, e);
        }
    }

    /**
     * Builds the complete JAXB tree structure of an xml stream.
     * Uses the already registered event consumer for validation events
     *
     * @param inputStream the input stream to read from
     * @return the JAXB object structure representing the xml file.
     * @throws XMLIOException in case of a JAXBException
     */
    public T read(final InputStream inputStream) {
        Objects.requireNonNull(inputStream);
        return readModel(inputStream).getRootElement();
    }

    /**
     * Builds the complete JAXB tree structure of an xml stream.
     * Uses the already registered event consumer for validation events
     *
     * @param inputStream the input stream to read from
     * @return the JAXB object structure representing the xml file.
     * @throws XMLIOException in case of a JAXBException
     */
    public JaxbModel<T, I> readModel(final InputStream inputStream) {
        Objects.requireNonNull(inputStream);
        try {
            final long start = System.currentTimeMillis();
            final JaxbModel<T, I> result = unmarshaller.unmarshall(inputStream);
            LOGGER.trace("Finished loading XML. Took {} ms", System.currentTimeMillis() - start);
            return result;
        } catch (final JAXBException e) {
            throw new XMLIOException(e.getMessage(), e);
        }
    }
}


