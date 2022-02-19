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
package com.foursoft.harness.kbl.v24;

import com.foursoft.jaxb.navext.runtime.ExtendedUnmarshaller;
import com.foursoft.jaxb.navext.runtime.JaxbModel;
import com.foursoft.jaxb.navext.runtime.model.Identifiable;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BasicLoadingTest {

    @Test
    void testLoadModel() throws JAXBException, IOException {
        try (final InputStream is = getClass().getClassLoader().getResourceAsStream("sample.kbl")) {
            final JaxbModel<KBLContainer, Identifiable> model
                    = new ExtendedUnmarshaller<KBLContainer, Identifiable>(KBLContainer.class)
                    .withBackReferences()
                    .withIdMapper(Identifiable.class, Identifiable::getXmlId)
                    .unmarshall(new BufferedInputStream(is));

            assertThat(model).isNotNull();
        }
    }

    @Test
    void testSelectorInheritance() throws JAXBException, IOException {
        try (final InputStream is = getClass().getClassLoader().getResourceAsStream("sample.kbl")) {
            final ExtendedUnmarshaller<KBLContainer, Identifiable> unmarshaller = new ExtendedUnmarshaller<KBLContainer, Identifiable>(
                    KBLContainer.class).withBackReferences()
                    .withIdMapper(Identifiable.class, Identifiable::getXmlId);

            final JaxbModel<KBLContainer, Identifiable> model = unmarshaller
                    .unmarshall(new BufferedInputStream(is));

            assertThat(model).isNotNull();

            final KblConnectorOccurrence occurrence = model.getIdLookup()
                    .findById(KblConnectorOccurrence.class, "I1616")
                    .get();

            final KblConnectorHousing part = model.getIdLookup()
                    .findById(KblConnectorHousing.class, "ch_part")
                    .get();

            assertThat(occurrence).isNotNull()
                    .hasFieldOrPropertyWithValue("part", part)
                    .returns(model.getRootElement().getHarness(), KblConnectorOccurrence::getParentHarness);

            assertThat(part).isNotNull()
                    .returns(Collections.singleton(occurrence), KblConnectorHousing::getRefConnectorOccurrence);
        }
    }

    @Test
    void validationTest() throws Exception {
        try (final InputStream is = getClass().getClassLoader().getResourceAsStream("sample.kbl")) {
            final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            final Source source = new StreamSource(getClass().getResourceAsStream("/kbl24/KBL24_SR1.xsd"));

            final Schema schema = schemaFactory.newSchema(source);

            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setNamespaceAware(true);
            dbf.setSchema(schema);

            final DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new ErrorHandler() {

                @Override
                public void warning(final SAXParseException exception) throws SAXException {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException(exception);
                }

                @Override
                public void fatalError(final SAXParseException exception) throws SAXException {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException(exception);
                }

                @Override
                public void error(final SAXParseException exception) throws SAXException {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException(exception);
                }
            });
            final Document d = db.parse(new BufferedInputStream(is));
        }
    }

    @Test
    void getBackReferences() throws IOException, JAXBException {
        try (final InputStream is = getClass().getClassLoader().getResourceAsStream("sample.kbl")) {
            assertThat(is)
                    .isNotNull();
            final ExtendedUnmarshaller<KBLContainer, Identifiable> unmarshaller =
                    new ExtendedUnmarshaller<KBLContainer, Identifiable>(KBLContainer.class)
                            .withBackReferences()
                            .withIdMapper(Identifiable.class, Identifiable::getXmlId);

            final JaxbModel<KBLContainer, Identifiable> model = unmarshaller
                    .unmarshall(new BufferedInputStream(is));

            final KBLContainer container = model.getRootElement();

            // KblConnectorHousing -> KblConnectorOccurrence
            final List<KblConnectorHousing> connectorHousings = container.getConnectorHousings();
            assertThat(connectorHousings)
                    .isNotEmpty();
            final KblConnectorHousing kblConnectorHousing = connectorHousings.get(0);

            final Set<KblConnectorOccurrence> refConnectorOccurrence = kblConnectorHousing.getRefConnectorOccurrence();
            final KblConnectorOccurrence kblConnectorOccurrence = refConnectorOccurrence.stream().findFirst().orElse(null);
            assertThat(kblConnectorOccurrence)
                    .isNotNull();

            // KblConnectorOccurrence -> KblHarness
            final KblHarness parentHarness = kblConnectorOccurrence.getParentHarness();
            // KblHarness -> KblConnectorOccurrence
            final KblConnectorOccurrence occurrenceByHarness = parentHarness.getConnectorOccurrences().stream()
                    .findFirst()
                    .orElse(null);
            assertThat(occurrenceByHarness)
                    .isNotNull()
                    .isEqualTo(kblConnectorOccurrence);

            final String xmlId = kblConnectorOccurrence.getXmlId();
            final KblConnectorOccurrence occurrenceByLookup = model.getIdLookup()
                    .findById(KblConnectorOccurrence.class, xmlId)
                    .orElse(null);
            assertThat(occurrenceByLookup)
                    .isNotNull()
                    .isEqualTo(kblConnectorOccurrence);
        }
    }
}
