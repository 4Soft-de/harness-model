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
package com.foursoft.xml.io.write;

import com.foursoft.xml.JaxbContextFactory;
import com.foursoft.xml.io.utils.ValidationEventLogger;
import com.foursoft.xml.io.utils.XMLIOException;
import com.foursoft.xml.io.write.xmlmeta.MarshallerListener;
import com.foursoft.xml.io.write.xmlmeta.XMLMeta;
import com.foursoft.xml.io.write.xmlmeta.XMLMetaAwareXMLStreamWriter;
import com.foursoft.xml.io.write.xmlmeta.comments.CommentAdderListener;
import com.foursoft.xml.io.write.xmlmeta.comments.Comments;
import com.foursoft.xml.io.write.xmlmeta.processinginstructions.ProcessingInstructionAdderListener;

import javax.xml.bind.*;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * Serializes a valid JAXB object structure to XML with the following additional features:
 * - Can add comments to the xml file
 * - Formats the XML output
 */
public class XMLWriter<T> {

    private static final String NAMESPACE_PREFIX_MAPPER = "com.sun.xml.bind.namespacePrefixMapper";

    private final Class<T> baseType;
    private final Marshaller marshaller;

    public XMLWriter(final Class<T> baseType) {
        this(baseType, new ValidationEventLogger());
    }

    public XMLWriter(final Class<T> baseType,
                     final Consumer<ValidationEvent> validationEventConsumer) {
        this.baseType = baseType;
        try {
            final String packageName = this.baseType.getPackage().getName();
            final JAXBContext jaxbContext = JaxbContextFactory.initializeContext(packageName, NamespacePrefixMapperImpl.class.getClassLoader());
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(NAMESPACE_PREFIX_MAPPER, new NamespacePrefixMapperImpl());
            addEventHandler(marshaller, validationEventConsumer);
            configureMarshaller(marshaller);
        } catch (final Exception e) {
            throw new XMLIOException("Cannot initialize marshaller.", e);
        }
    }

    private static void addEventHandler(final Marshaller marshaller,
                                        final Consumer<ValidationEvent> validationEventConsumer)
            throws JAXBException {
        final ValidationEventHandler eventHandler = marshaller.getEventHandler();
        marshaller.setEventHandler(event -> {
            validationEventConsumer.accept(event);
            return eventHandler.handleEvent(event);
        });

    }

    /**
     * Method which can be overridden for further configuration on the marshaller.
     *
     * @param marshaller Marshaller to configure.
     * @throws Exception In case something went wrong.
     */
    protected void configureMarshaller(final Marshaller marshaller) throws Exception {
        // default empty impl
    }

    /**
     * write the JAXB model to an output stream
     *
     * @param container    the jaxb model to deserialize into the given stream
     * @param outputStream the output to write to
     */
    public void write(final T container, final OutputStream outputStream) {
        write(container, new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
    }

    /**
     * write the JAXB model to an output stream
     *
     * @param container    the jaxb model to deserialize into the given stream
     * @param outputStream the output to write to
     * @param meta         additional meta information which should be added to output {@link XMLMeta}
     */
    public void write(final T container, final XMLMeta meta, final OutputStream outputStream) {
        write(container, meta, new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
    }

    /**
     * write the JAXB model to an output stream
     *
     * @param container    the jaxb model to deserialize into the given stream
     * @param outputStream the output to write to
     * @param comments     additional comments which should be added to output {@link Comments}
     * @deprecated Use {@link #write(T, XMLMeta, OutputStream)} and {@link XMLMeta#setComments(Comments)} instead.
     */
    @Deprecated
    public void write(final T container, final Comments comments, final OutputStream outputStream) {
        final XMLMeta meta = new XMLMeta();
        meta.setComments(comments);
        write(container, meta, new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
    }

    /**
     * write the JAXB model to a string
     *
     * @param container the jaxb model to deserialize into the given stream
     * @param comments  additional comments which should be added to output {@link Comments}
     * @return the model as xml string
     * @deprecated Use {@link #writeToString(T, XMLMeta)} and {@link XMLMeta#setComments(Comments)} instead.
     */
    @Deprecated
    public String writeToString(final T container, final Comments comments) {
        final XMLMeta meta = new XMLMeta();
        meta.setComments(comments);
        return writeToString(container, meta);
    }

    /**
     * write the JAXB model to a string
     *
     * @param container the jaxb model to deserialize into the given stream
     * @return the model as xml string
     */
    public String writeToString(final T container) {
        final StringWriter stringWriter = new StringWriter();
        write(container, stringWriter);
        return stringWriter.toString();
    }

    /**
     * Write the JAXB model to a String. Allows Meta Data as like as comments to be passed with.
     *
     * @param container the jaxb model to deserialize into the given stream
     * @param meta      additional meta information which should be added to output {@link XMLMeta}
     * @return the model as xml string
     */
    public String writeToString(final T container, final XMLMeta meta) {
        final StringWriter stringWriter = new StringWriter();
        write(container, meta, stringWriter);
        return stringWriter.toString();
    }

    private void write(final T container, final XMLMeta meta, final Writer output) {
        final XMLOutputFactory xof = XMLOutputFactory.newFactory();
        try {
            final XMLStreamWriter xmlStreamWriter = xof.createXMLStreamWriter(output);


            final XMLMetaAwareXMLStreamWriter xsw = new XMLMetaAwareXMLStreamWriter(xmlStreamWriter);

            final MarshallerListener marshallerListener = new MarshallerListener();
            marshaller.setListener(marshallerListener);
            meta.getComments().ifPresent(c -> marshallerListener.addListener(new CommentAdderListener(xsw, c)));
            meta.getProcessingInstructions()
                    .ifPresent(c -> marshallerListener.addListener(new ProcessingInstructionAdderListener(xmlStreamWriter, c)));

            marshaller.marshal(container, xsw);
            xsw.close();
            marshallerListener.clear();

        } catch (final XMLStreamException | JAXBException e) {
            throw new XMLIOException("Error serializing XML file.", e);
        }
    }

    private void write(final T container, final Writer output) {
        try {
            marshaller.marshal(container, output);
        } catch (final JAXBException e) {
            throw new XMLIOException("Error serializing XML file.", e);
        }
    }

}
