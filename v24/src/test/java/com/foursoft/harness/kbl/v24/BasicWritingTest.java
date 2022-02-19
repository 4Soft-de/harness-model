/*-
 * ========================LICENSE_START=================================
 * kbl-v24
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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BasicWritingTest {

    @Test
    void testWriteModel() {
        final KBLContainer root = new KBLContainer();
        root.setXmlId("ID000");
        root.setVersionId("version_id0");

        final KblHarness harness = new KblHarness();
        harness.setXmlId("I1397");

        root.setHarness(harness);

        final KblConnectorOccurrence connectorOccurrence = new KblConnectorOccurrence();
        connectorOccurrence.setXmlId("I1616");
        final KblTerminalOccurrence terminalOccurrence = new KblTerminalOccurrence();
        terminalOccurrence.setXmlId("id_4711");
        final KblTerminalOccurrence terminalOccurrence2 = new KblTerminalOccurrence();
        terminalOccurrence2.setXmlId("id_4712");

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

        final KblWriter kblWriter = new KblWriter();
        final String result = kblWriter.writeToString(root);

        System.out.println(result);
        assertThat(result)
                .isEqualToIgnoringWhitespace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                                                     + "<kbl:KBL_container id=\"ID000\" version_id=\"version_id0\""
                                                     + " xmlns:kbl=\"http://www.prostep.org/Car_electric_container/KBL2.3/KBLSchema\">\n"
                                                     + "    <Harness id=\"I1397\">\n"
                                                     + "        <Connector_occurrence id=\"I1616\">\n"
                                                     + "            <Contact_points id=\"id_1234\">\n"
                                                     + "                <Id>SCHNUPSI</Id>\n"
                                                     + "                <Associated_parts>id_4711</Associated_parts>\n"
                                                     + "            </Contact_points>\n"
                                                     + "            <Contact_points id=\"id_1235\">\n"
                                                     + "                <Id>SCHNUPSI</Id>\n"
                                                     + "            </Contact_points>\n"
                                                     + "            <Contact_points id=\"id_1236\">\n"
                                                     + "                <Id>SCHNUPSI</Id>\n"
                                                     + "            </Contact_points>\n"
                                                     + "        </Connector_occurrence>\n"
                                                     + "        <Terminal_occurrence id=\"id_4711\"/>\n"
                                                     + "        <Terminal_occurrence id=\"id_4712\"/>\n"
                                                     + "    </Harness>\n"
                                                     + "</kbl:KBL_container>");

    }

}
