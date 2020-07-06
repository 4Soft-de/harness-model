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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

public class BasicWritingTest {

    @Test
    public void testWriteModel() throws JAXBException, TransformerFactoryConfigurationError {
        final JAXBContext jc = JAXBContext.newInstance(KBLContainer.class);

        final KBLContainer root = new KBLContainer();

        final KblHarness harness = new KblHarness();

        root.setHarness(harness);

        final KblConnectorOccurrence connectorOccurrence = new KblConnectorOccurrence();
        final KblTerminalOccurrence terminalOccurrence = new KblTerminalOccurrence();
        terminalOccurrence.setXmlId("id_4711");
        final KblTerminalOccurrence terminalOccurrence2 = new KblTerminalOccurrence();
        terminalOccurrence2.setXmlId("id_4711");

        harness.getConnectorOccurrences()
                .add(connectorOccurrence);
        harness.getTerminalOccurrences()
                .add(terminalOccurrence);
        harness.getTerminalOccurrences()
                .add(terminalOccurrence2);

        final KblContactPoint contactPoint = new KblContactPoint();

        connectorOccurrence.getContactPoints()
                .add(contactPoint);

        contactPoint.setId("SCHNUPSI");
        contactPoint.setXmlId("id_1234");
        contactPoint.getAssociatedParts()
                .add(terminalOccurrence);

        final KblContactPoint contactPoint2 = new KblContactPoint();

        connectorOccurrence.getContactPoints()
                .add(contactPoint2);

        contactPoint2.setId("SCHNUPSI");
        contactPoint2.setXmlId("id_1235");

        // Access to getter = Lazy Init to of List = EmptyList =
        // <AssociatedParts></AssociatedParts>
        contactPoint2.getAssociatedParts();

        final KblContactPoint contactPoint3 = new KblContactPoint();

        connectorOccurrence.getContactPoints()
                .add(contactPoint3);

        contactPoint3.setId("SCHNUPSI");
        contactPoint3.setXmlId("id_1236");

        final Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        final StringWriter stringWriter = new StringWriter();

        marshaller.marshal(root, stringWriter);

        final String result = stringWriter.toString();
        System.out.println(result);
        assertThat(result)
                .isEqualToIgnoringWhitespace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n"
                        + "<kbl:KBL_container xmlns:kbl=\"http://www.prostep.org/Car_electric_container/KBL2.3/KBLSchema\">\r\n"
                        + "    <Harness>\r\n" + "        <Connector_occurrence>\r\n"
                        + "            <Contact_points id=\"id_1234\">\r\n" + "                <Id>SCHNUPSI</Id>\r\n"
                        + "                <Associated_parts>id_4711</Associated_parts>\r\n"
                        + "            </Contact_points>\r\n" + "            <Contact_points id=\"id_1235\">\r\n"
                        + "                <Id>SCHNUPSI</Id>\r\n" + "            </Contact_points>\r\n"
                        + "            <Contact_points id=\"id_1236\">\r\n" + "                <Id>SCHNUPSI</Id>\r\n"
                        + "            </Contact_points>\r\n" + "        </Connector_occurrence>\r\n"
                        + "        <Terminal_occurrence id=\"id_4711\"/>\r\n"
                        + "        <Terminal_occurrence id=\"id_4711\"/>\r\n" + "    </Harness>\r\n"
                        + "</kbl:KBL_container>");

    }

}
