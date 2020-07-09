package com.volkswagenag.daem.converter.vec.c2v.vecwrite;

import de.foursoft.harness.vec.model.vec120.VecContent;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;

/**
 * extended VEC Writer
 * - Can add comments the xml file
 * - formatted output
 * - logs the created XML with error messages
 */
public final class VecWriter {

    private VecWriter() {

    }

    public static String write(final VecContent container,
                               final boolean schemaCheck,
                               final boolean detailedCheckLog,
                               final Map<Object, String> comments) {
        Objects.requireNonNull(container);
        Objects.requireNonNull(comments);
        final String xmlString = marshall(container, comments);
        if (schemaCheck) {
            VecValidation.validateXML(xmlString, detailedCheckLog);
        }
        return xmlString;
    }

    private static String marshall(final VecContent container, final Map<Object, String> comments) {

        new InitializeFields().initializeFields(container);

        try (final StringWriter sw = new StringWriter()) {
            final JAXBContext jaxbContext = JAXBContext.newInstance(VecContent.class);
            final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            final JAXB2ValidationEventCollector validationCollector = new JAXB2ValidationEventCollector();
            jaxbMarshaller.setEventHandler(validationCollector);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            final XMLOutputFactory xof = XMLOutputFactory.newFactory();
            final MyIndentingXMLStreamWriter xsw = new MyIndentingXMLStreamWriter(xof.createXMLStreamWriter(sw));

            jaxbMarshaller.setListener(new CommentAdderListener(xsw, comments));

            jaxbMarshaller.marshal(container, xsw);
            xsw.close();
            validationCollector.logEvents();
            return sw.toString();
        } catch (final JAXBException | XMLStreamException | IOException e) {
            throw new RuntimeException("Error serializing VEC File.", e);
        }
    }

}
