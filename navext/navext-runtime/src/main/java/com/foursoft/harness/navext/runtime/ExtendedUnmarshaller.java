/*-
 * ========================LICENSE_START=================================
 * NavExt Runtime
 * %%
 * Copyright (C) 2019 - 2023 4Soft GmbH
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
package com.foursoft.harness.navext.runtime;

import com.foursoft.harness.navext.runtime.annotations.XmlBackReference;
import com.foursoft.harness.navext.runtime.annotations.XmlParent;
import com.foursoft.harness.navext.runtime.postprocessing.*;
import jakarta.xml.bind.*;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Provides extended unmarshalling capabilities for a JAXB model. Capabilities
 * are:
 * <ul>
 * <li>Initializing of parent associations and back references (see
 * {@link XmlParent} and {@link XmlBackReference}).</li>
 * <li>Creation of an {@link IdLookupProvider} to provide a fast id-based index
 * for the unmarshalled model.</li>
 * <li>Registration and execution of custom {@link ModelPostProcessor}.</li>
 * </ul>
 * This is a wrapper arround an {@link Unmarshaller}.
 * <p>
 * The {@link ExtendedUnmarshaller} is required in order to correctly integrate
 * the two phase model post processing (see {@link ModelPostProcessor} into the
 * unmarshalling process.
 *
 * @param <R> Type of the root element to unmarshall.
 * @param <I> Type of elements that are identifiable.
 * @author becker
 */
public class ExtendedUnmarshaller<R, I> {

    private final ModelPostProcessorRegistry postProcessorRegistry;
    private final List<IdLookupGeneratorPostProcessor<I>> idLookupGenerators = new ArrayList<>();
    private final Unmarshaller unmarshaller;
    private final Class<R> rootElement;

    /**
     * XMLReader that will be used to parse a document.
     */
    private XMLReader reader = null;

    public ExtendedUnmarshaller(final Class<R> rootElement) throws JAXBException {
        this.rootElement = rootElement;
        final String packageName = rootElement.getPackage().getName();
        final JAXBContext context = JaxbContextFactory.initializeContext(packageName, getClass().getClassLoader());
        postProcessorRegistry = new ModelPostProcessorRegistry(packageName);
        unmarshaller = context.createUnmarshaller();
    }

    /**
     * Turns on the capability to initialize parent associations and
     * backreferences.
     *
     * @return Returns <tt>this</tt> for method chaining / fluent
     * initialization.
     */
    public ExtendedUnmarshaller<R, I> withBackReferences() {
        postProcessorRegistry.withFactory(ReflectiveAssociationPostProcessor::new);
        return this;
    }

    /**
     * Adds a custom {@link ModelPostProcessor} to the deserialization.
     *
     * @param modelPostProcessor the model post processor to use. Must not be null.
     * @return this for fluent API
     */
    public ExtendedUnmarshaller<R, I> withCustomPostProcessor(final ModelPostProcessor modelPostProcessor) {
        postProcessorRegistry.addDefaultPostProcessor(modelPostProcessor);
        return this;
    }

    /**
     * The jaxb unmarshaller has an event handler which is intercepted to provide the event for the consumer.
     * This will reset the handler first to the default handler, and then add the given consumer to it
     *
     * @param eventConsumer the consumer for the jaxb validation event
     * @return this for fluent API
     * @throws JAXBException if the unmarshalling goes wrong
     */
    public ExtendedUnmarshaller<R, I> withEventLogging(final Consumer<ValidationEvent> eventConsumer)
            throws JAXBException {

        final ValidationEventHandler eventHandler = unmarshaller.getEventHandler();
        if (eventHandler instanceof InterceptEventHandler) {
            ((InterceptEventHandler) eventHandler).setEventConsumer(eventConsumer);
        } else {
            unmarshaller.setEventHandler(new InterceptEventHandler(eventConsumer, eventHandler));
        }

        return this;
    }

    /**
     * Defines an id mapper to create a {@link IdLookupProvider} during the
     * unmarshalling process.
     * <p>
     * If no id mapper is defined, the resulting {@link JaxbModel} will have no
     * {@link IdLookupProvider} initialized.
     * <p>
     * If more than one id mapper is registered, all are processed and the
     * result is merged into one {@link IdLookupProvider}. This allows a common
     * the handling of all classes during the unmarshalling, even if the do not
     * have a shared superclass or interface.
     *
     * @param classOfIdentifiableElements the class of the identifiable element
     * @param idMapper                    the mapper function to get the id of each element
     * @return this for fluent API
     */
    public ExtendedUnmarshaller<R, I> withIdMapper(final Class<I> classOfIdentifiableElements,
                                                   final Function<I, String> idMapper) {
        final IdLookupGeneratorPostProcessor<I> idLookupGenerator = new IdLookupGeneratorPostProcessor<>(
                classOfIdentifiableElements, idMapper);

        idLookupGenerators.add(idLookupGenerator);
        postProcessorRegistry.addDefaultPostProcessor(idLookupGenerator);

        return this;
    }

    public JaxbModel<R, I> unmarshall(final Node node) throws JAXBException {
        return unmarshal(new DOMSource(node));
    }

    public JaxbModel<R, I> unmarshall(final InputStream resource) throws JAXBException {
        SAXSource source = new SAXSource(getXMLReader(), new InputSource(resource));

        return unmarshal(source);
    }

    public JaxbModel<R, I> unmarshal(final Source source) throws JAXBException {
        final ModelPostProcessorManager modelPostProcessorManager = new ModelPostProcessorManager(
                postProcessorRegistry);

        unmarshaller.setListener(modelPostProcessorManager);

        final R root = rootElement.cast(
                unmarshaller.unmarshal(source));

        modelPostProcessorManager.doPostProcessing();

        final Optional<IdLookupProvider<I>> idLookupProvider = idLookupGenerators.stream()
                .map(IdLookupGeneratorPostProcessor::createIdLookkup)
                .reduce(IdLookupProvider<I>::merge);

        cleanUp();

        return new JaxbModel<>(root, idLookupProvider.orElse(null));
    }

    /**
     * Provides access to the internal jax-b unmarshaller for further configuration. Use with caution.
     *
     * @return the internal jax-b unmarshaller
     */
    public Unmarshaller getUnmarshaller() {
        return unmarshaller;
    }

    private void cleanUp() {
        // Allow garbage collection.
        unmarshaller.setListener(null);
        postProcessorRegistry.clearStateOfPostProcessors();
    }

    /**
     * Obtains a configured XMLReader.
     * <p>
     * {@link Unmarshaller} is not re-entrant, so we will
     * only use one instance of XMLReader.
     * <p>
     * Overriden in order to fix potential security issue.
     * See jakarta.xml.bind.helpers.AbstractUnmarshallerImpl#getXMLReader()
     */
    protected XMLReader getXMLReader() throws JAXBException {
        if (reader == null) {
            try {
                final SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setNamespaceAware(true);
                factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                factory.setValidating(false);
                final SAXParser parser = factory.newSAXParser();
                parser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                parser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
                reader = parser.getXMLReader();
            } catch (final ParserConfigurationException | SAXException e) {
                throw new JAXBException(e);
            }
        }
        return reader;
    }

    public static class InterceptEventHandler implements ValidationEventHandler {
        private final ValidationEventHandler originalEventHandler;
        private Consumer<ValidationEvent> eventConsumer;

        public InterceptEventHandler(final Consumer<ValidationEvent> eventConsumer,
                                     final ValidationEventHandler originalEventHandler) {
            Objects.requireNonNull(originalEventHandler);
            Objects.requireNonNull(eventConsumer);
            this.originalEventHandler = originalEventHandler;
            this.eventConsumer = eventConsumer;
        }

        @Override
        public boolean handleEvent(final ValidationEvent event) {
            eventConsumer.accept(event);
            return originalEventHandler.handleEvent(event);
        }

        public void setEventConsumer(final Consumer<ValidationEvent> eventConsumer) {
            Objects.requireNonNull(eventConsumer);
            this.eventConsumer = eventConsumer;
        }
    }

}
