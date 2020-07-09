package com.foursoft.xml.io.write;

import com.foursoft.xml.io.XMLIOException;

import javax.xml.bind.*;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.function.Consumer;

/**
 * extended VEC Writer
 * - Can add comments the xml file
 * - formatted output
 * - logs the created XML with error messages
 */
public class XMLWriter<T> {

    private final Class<T> baseType;
    private final Marshaller marshaller;

    XMLWriter(final Class<T> baseType,
              final Consumer<ValidationEvent> validationEventConsumer) {
        this.baseType = baseType;
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(this.baseType);
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            addEventHandler(marshaller, validationEventConsumer);
        } catch (final Exception e) {
            throw new XMLIOException("Cannot initialize unmarshaller.", e);
        }
    }

    public String writeToString(final T container, final Comments comments) {
        try (final StringWriter stringWriter = new StringWriter()) {
            write(container, comments, stringWriter);
            return stringWriter.toString();
        } catch (final Exception e) {
            throw new XMLIOException("Error serializing VEC File.", e);
        }
    }

    public String writeToString(final T container) {
        try (final StringWriter stringWriter = new StringWriter()) {
            write(container, stringWriter);
            return stringWriter.toString();
        } catch (final IOException e) {
            throw new XMLIOException("Error serializing VEC File.", e);
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

    //TODO Validation
    private void write(final T container, final Comments comments, final Writer output) {
        final XMLOutputFactory xof = XMLOutputFactory.newFactory();
        try {
            final CommentAwareXMLStreamWriter xsw = new CommentAwareXMLStreamWriter(xof.createXMLStreamWriter(output));

            marshaller.setListener(new CommentAdderListener(xsw, comments));

            marshaller.marshal(container, xsw);
            xsw.close();
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
