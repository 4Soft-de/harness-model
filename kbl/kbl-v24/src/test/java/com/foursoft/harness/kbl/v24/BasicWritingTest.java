/*-
 * ========================LICENSE_START=================================
 * KBL 2.4
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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

import com.foursoft.harness.kbl.v24.validation.SchemaFactory;
import com.foursoft.harness.navext.runtime.io.validation.XMLValidation;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class BasicWritingTest {

    private KBLContainer createModel() {
        final KBLContainer root = new KBLContainer();
        root.setXmlId("ID000");
        root.setVersionId("version_id0");

        final KblHarness harness = new KblHarness();
        harness.setXmlId("I1397");
        harness.setPartNumber("123_456_789");
        harness.setVersion("3");
        harness.setCompanyName("Test Company");
        harness.setAbbreviation("Abbr");
        harness.setDescription("Desc");
        harness.setCarClassificationLevel2("CCL2");
        harness.setModelYear("my");
        harness.setContent(KblHarnessContent.HARNESS_COMPLETE_SET);

        root.setHarness(harness);

        final KblConnectorHousing connectorHousing = new KblConnectorHousing();
        connectorHousing.setXmlId("I1525");
        connectorHousing.setPartNumber("123_456_789");
        connectorHousing.setVersion("3");
        connectorHousing.setCompanyName("Test Company");
        connectorHousing.setAbbreviation("Abbr");
        connectorHousing.setDescription("Desc");

        root.getConnectorHousings().add(connectorHousing);

        final KblConnectorOccurrence connectorOccurrence = new KblConnectorOccurrence();
        connectorOccurrence.setXmlId("I1616");
        connectorOccurrence.setId("CON");
        connectorOccurrence.setPart(connectorHousing);

        final KblGeneralTerminal generalTerminal = new KblGeneralTerminal();
        generalTerminal.setXmlId("id_4600");
        generalTerminal.setPartNumber("123_456_789");
        generalTerminal.setVersion("3");
        generalTerminal.setCompanyName("Test Company");
        generalTerminal.setAbbreviation("Abbr");
        generalTerminal.setDescription("Desc");

        root.getGeneralTerminals().add(generalTerminal);

        final KblTerminalOccurrence terminalOccurrence = new KblTerminalOccurrence();
        terminalOccurrence.setXmlId("id_4711");
        terminalOccurrence.setPart(generalTerminal);

        final KblTerminalOccurrence terminalOccurrence2 = new KblTerminalOccurrence();
        terminalOccurrence2.setXmlId("id_4712");
        terminalOccurrence2.setPart(generalTerminal);

        harness.getConnectorOccurrences()
                .add(connectorOccurrence);
        harness.getTerminalOccurrences()
                .add(terminalOccurrence);
        harness.getTerminalOccurrences()
                .add(terminalOccurrence2);

        final KblSlotOccurrence slotOccurrence = new KblSlotOccurrence();
        slotOccurrence.setXmlId("id_1357");

        final KblSlot slot = new KblSlot();
        slot.setNumberOfCavities(BigInteger.ZERO);
        slot.setXmlId("id_1010");
        slotOccurrence.setPart(slot);
        connectorHousing.getSlots().add(slot);

        connectorOccurrence.getSlots().add(slotOccurrence);

        final KblCavityOccurrence cavityOccurrence = new KblCavityOccurrence();
        cavityOccurrence.setXmlId("id_1122");

        final KblCavity cavity = new KblCavity();
        cavity.setXmlId("id_1248");
        cavity.setCavityNumber("5");
        cavityOccurrence.setPart(cavity);
        slot.getCavities().add(cavity);

        slotOccurrence.getCavities().add(cavityOccurrence);

        final KblContactPoint contactPoint = new KblContactPoint();

        connectorOccurrence.getContactPoints()
                .add(contactPoint);

        contactPoint.setId("SCHNUPSI");
        contactPoint.setXmlId("id_1234");
        contactPoint.getContactedCavity().add(cavityOccurrence);

        contactPoint.getAssociatedParts()
                .add(terminalOccurrence);

        final KblContactPoint contactPoint2 = new KblContactPoint();

        connectorOccurrence.getContactPoints()
                .add(contactPoint2);

        contactPoint2.setId("SCHNUPSI");
        contactPoint2.setXmlId("id_1235");
        contactPoint2.getContactedCavity().add(cavityOccurrence);

        // Access to getter = Lazy Init to of List = EmptyList =
        // <AssociatedParts></AssociatedParts>
        contactPoint2.getAssociatedParts();

        final KblContactPoint contactPoint3 = new KblContactPoint();

        connectorOccurrence.getContactPoints()
                .add(contactPoint3);

        contactPoint3.setId("SCHNUPSI");
        contactPoint3.setXmlId("id_1236");
        contactPoint3.getContactedCavity().add(cavityOccurrence);

        return root;
    }

    @Test
    void testWriteModel() {
        final KBLContainer root = createModel();

        final KblWriter kblWriter = new KblWriter();
        final String result = kblWriter.writeToString(root);

        assertThat(result).isEqualToIgnoringWhitespace(
                """
                        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                        <kbl:KBL_container id="ID000" version_id="version_id0" xmlns:kbl="http://www.prostep.org/Car_electric_container/KBL2.3/KBLSchema">
                            <Connector_housing id="I1525">
                                <Part_number>123_456_789</Part_number>
                                <Company_name>Test Company</Company_name>
                                <Version>3</Version>
                                <Abbreviation>Abbr</Abbreviation>
                                <Description>Desc</Description>
                                <Slots id="id_1010">
                                    <Number_of_cavities>0</Number_of_cavities>
                                    <Cavities id="id_1248">
                                        <Cavity_number>5</Cavity_number>
                                    </Cavities>
                                </Slots>
                            </Connector_housing>
                            <General_terminal id="id_4600">
                                <Part_number>123_456_789</Part_number>
                                <Company_name>Test Company</Company_name>
                                <Version>3</Version>
                                <Abbreviation>Abbr</Abbreviation>
                                <Description>Desc</Description>
                            </General_terminal>
                            <Harness id="I1397">
                                <Part_number>123_456_789</Part_number>
                                <Company_name>Test Company</Company_name>
                                <Version>3</Version>
                                <Abbreviation>Abbr</Abbreviation>
                                <Description>Desc</Description>
                                <Car_classification_level_2>CCL2</Car_classification_level_2>
                                <Model_year>my</Model_year>
                                <Content>harness complete set</Content>
                                <Connector_occurrence id="I1616">
                                    <Id>CON</Id>
                                    <Part>I1525</Part>
                                    <Contact_points id="id_1234">
                                        <Id>SCHNUPSI</Id>
                                        <Associated_parts>id_4711</Associated_parts>
                                        <Contacted_cavity>id_1122</Contacted_cavity>
                                    </Contact_points>
                                    <Contact_points id="id_1235">
                                        <Id>SCHNUPSI</Id>
                                        <Contacted_cavity>id_1122</Contacted_cavity>
                                    </Contact_points>
                                    <Contact_points id="id_1236">
                                        <Id>SCHNUPSI</Id>
                                        <Contacted_cavity>id_1122</Contacted_cavity>
                                    </Contact_points>
                                    <Slots id="id_1357">
                                        <Part>id_1010</Part>
                                        <Cavities id="id_1122">
                                            <Part>id_1248</Part>
                                        </Cavities>
                                    </Slots>
                                </Connector_occurrence>
                                <Terminal_occurrence id="id_4711">
                                    <Part>id_4600</Part>
                                </Terminal_occurrence>
                                <Terminal_occurrence id="id_4712">
                                    <Part>id_4600</Part>
                                </Terminal_occurrence>
                            </Harness>
                        </kbl:KBL_container>
                        """
        );
    }

    @Test
    void testValidateModel() {
        final Collection<String> errors = new ArrayList<>();

        final KBLContainer model = createModel();
        final String xml = new KblWriter().writeToString(model);

        XMLValidation.validateXML(SchemaFactory.getSchema(), xml, errors::add);

        assertThat(errors)
                .isEmpty();
    }

}
