/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
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
package com.foursoft.harness.vec.scripting;

import com.foursoft.harness.vec.scripting.core.SpecificationRegistry;
import com.foursoft.harness.vec.v2x.*;

import static com.foursoft.harness.vec.scripting.factories.NumericalValueFactory.value;
import static com.foursoft.harness.vec.scripting.factories.NumericalValueFactory.valueWithTolerance;

public class PluggableTerminalSpecificationBuilder
        extends PartOrUsageRelatedSpecificationBuilder<VecPluggableTerminalSpecification> {

    private final VecPluggableTerminalSpecification element;
    private final VecWireReception wireReception;
    private final VecTerminalReception terminalReception;
    private final VecWireReceptionSpecification wireReceptionSpecification;
    private final VecTerminalReceptionSpecification terminalReceptionSpecification;
    private final VecSession session;
    private final SpecificationRegistry specificationRegistry;

    PluggableTerminalSpecificationBuilder(VecSession session, SpecificationRegistry specificationRegistry,
                                          final String partNumber) {
        this.session = session;
        this.specificationRegistry = specificationRegistry;

        wireReceptionSpecification = new VecWireReceptionSpecification();
        wireReceptionSpecification.setIdentification("WireRec-" + partNumber);
        terminalReceptionSpecification = new VecTerminalReceptionSpecification();
        terminalReceptionSpecification.setIdentification("TermRec-" + partNumber);

        wireReception = new VecWireReception();
        wireReception.setIdentification("WR1");
        wireReception.setWireReceptionSpecification(wireReceptionSpecification);
        terminalReception = new VecTerminalReception();
        terminalReception.setIdentification("TR1");
        terminalReception.setTerminalReceptionSpecification(terminalReceptionSpecification);

        VecInternalTerminalConnection terminalConnection = new VecInternalTerminalConnection();
        terminalConnection.setIdentification("IntCon-" + partNumber);
        terminalConnection.getTerminalReception().add(terminalReception);
        terminalConnection.getWireReception().add(wireReception);

        element = initializeSpecification(VecPluggableTerminalSpecification.class, partNumber);
        element.setTerminalType("Standard");

        element.getTerminalReceptions().add(terminalReception);
        element.getWireReceptions().add(wireReception);
        element.getInternalTerminalConnections().add(terminalConnection);
    }

    public PluggableTerminalSpecificationBuilder withInsulationDisplacementLength(double value, double lowerTolerance,
                                                                                  double upperTolerance) {
        this.wireReceptionSpecification.setInsulationDisplacementLength(
                valueWithTolerance(value, lowerTolerance, upperTolerance, session.mm()));
        return this;
    }

    public PluggableTerminalSpecificationBuilder withSealable(boolean sealable) {
        this.wireReceptionSpecification.setSealable(sealable);
        return this;
    }

    public PluggableTerminalSpecificationBuilder withConnectionBLength(double value) {
        this.wireReceptionSpecification.setConnectionBLength(value(value, session.mm()));
        return this;
    }

    public PluggableTerminalSpecificationBuilder withRearBellMouth(double value) {
        VecNumericalValue numericalValue = value(value, session.mm());
        session.addXmlComment(numericalValue, " Excelsheet has Min- & MaxBackBellmouth. How is this connected?");
        this.wireReceptionSpecification.setRearBellMouthLength(numericalValue);
        return this;
    }

    public PluggableTerminalSpecificationBuilder withTerminalLengthOverall(double value) {
        VecNumericalValueProperty terminalLength = new VecNumericalValueProperty();
        terminalLength.setPropertyType("TerminalLengthOverall");
        terminalLength.setValue(value(value, session.mm()));

        this.element.getCustomProperties().add(terminalLength);
        session.addXmlComment(terminalLength,
                              " Can be calculated from: TerminalReceptionSpecification.ContactRangeLength + " +
                                      "TerminalSpecification.ConnectionALength WireReceptionSpecification" +
                                      ".CrimpConnectionLength");

        return this;
    }

    public PluggableTerminalSpecificationBuilder withMissingAttributesComment() {
        session.addXmlComment(this.element,
                              " The following attributes are currently not mapped in the VEC, issues should be " +
                                      "created (custom properties would be possible): \n" +
                                      "SealPosition + Tolerance, ShapeOfInsulationCrimp, " +
                                      "ThicknessOfTerminalMaterial, LegLengthOfInsulationCrimp, MaxTerminalWidth" +
                                      "MinWireTipProtrusion" +
                                      "MaxWiretipProtrusion ");
        return this;
    }

    public PluggableTerminalSpecificationBuilder withCrimpDetailsExample() {
        createCoreCrimpDetailsExample();
        createInsulationCrimpDetailsExample();

        VecNumericalValueProperty pullOffForce = new VecNumericalValueProperty();
        pullOffForce.setPropertyType("PullOffForce");
        pullOffForce.setValue(value(50, session.newton()));
        this.wireReceptionSpecification.getCustomProperties().add(pullOffForce);

        session.addXmlComment(pullOffForce,
                              " Workaround until KBLFRM-1176 is resolved.");

        return this;
    }

    @Override public VecPluggableTerminalSpecification build() {
        specificationRegistry.register(wireReceptionSpecification);
        specificationRegistry.register(terminalReceptionSpecification);

        return this.element;
    }

    private void createInsulationCrimpDetailsExample() {
        VecComplexProperty root = new VecComplexProperty();
        root.setPropertyType("InsulationCrimpDetails");
        session.addXmlComment(root,
                              " Workaround until KBLFRM-1085 is resolved. (Multiple entries with " +
                                      "InsulationCrimpDetails possible)");

        VecNumericalValueProperty outsideDiameter = new VecNumericalValueProperty();
        outsideDiameter.setPropertyType("WireElementOutsideDiameter");
        outsideDiameter.setValue(value(1.3, session.mm()));
        root.getCustomProperties().add(outsideDiameter);

        VecNumericalValueProperty crimpHeight = new VecNumericalValueProperty();
        crimpHeight.setPropertyType("CrimpHeight");
        crimpHeight.setValue(valueWithTolerance(1.5, -0.1, 0.1, session.mm()));
        root.getCustomProperties().add(crimpHeight);

        VecNumericalValueProperty crimpWidth = new VecNumericalValueProperty();
        crimpWidth.setPropertyType("CrimpWidth");
        crimpWidth.setValue(valueWithTolerance(1.07, 0, 0.1, session.mm()));
        root.getCustomProperties().add(crimpWidth);

        VecSimpleValueProperty material = new VecSimpleValueProperty();
        material.setPropertyType("InsulationMaterial");
        material.setValue("PVC");
        root.getCustomProperties().add(material);

        VecSimpleValueProperty wireType = new VecSimpleValueProperty();
        wireType.setPropertyType("WireType");
        wireType.setValue("FLRY-A");
        root.getCustomProperties().add(wireType);

        this.wireReceptionSpecification.getCustomProperties().add(root);
    }

    private void createCoreCrimpDetailsExample() {
        VecComplexProperty root = new VecComplexProperty();
        root.setPropertyType("CoreCrimpDetails");
        session.addXmlComment(root,
                              " Workaround until KBLFRM-1085 is resolved. (Multiple entries with " +
                                      "CoreCrimpDetails possible) ");

        VecNumericalValueProperty crossSection = new VecNumericalValueProperty();
        crossSection.setPropertyType("CoreCrossSectionArea");
        crossSection.setValue(value(0.35, session.squareMM()));
        root.getCustomProperties().add(crossSection);

        VecNumericalValueProperty crimpHeight = new VecNumericalValueProperty();
        crimpHeight.setPropertyType("CrimpHeight");
        crimpHeight.setValue(valueWithTolerance(0.76, -0.03, 0.03, session.mm()));
        root.getCustomProperties().add(crimpHeight);

        VecNumericalValueProperty crimpWidth = new VecNumericalValueProperty();
        crimpWidth.setPropertyType("CrimpWidth");
        crimpWidth.setValue(valueWithTolerance(1.07, 0, 0.1, session.mm()));
        root.getCustomProperties().add(crimpWidth);

        VecSimpleValueProperty material = new VecSimpleValueProperty();
        material.setPropertyType("CoreMaterial");
        material.setValue("Cu-ETP1");
        root.getCustomProperties().add(material);

        VecSimpleValueProperty wireType = new VecSimpleValueProperty();
        wireType.setPropertyType("WireType");
        wireType.setValue("FLRY-A");
        root.getCustomProperties().add(wireType);

        this.wireReceptionSpecification.getCustomProperties().add(root);
    }

}
