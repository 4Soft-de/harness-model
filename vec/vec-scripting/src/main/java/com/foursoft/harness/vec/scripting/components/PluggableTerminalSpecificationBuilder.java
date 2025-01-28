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
package com.foursoft.harness.vec.scripting.components;

import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.core.PartOrUsageRelatedSpecificationBuilder;
import com.foursoft.harness.vec.scripting.core.SpecificationLocator;
import com.foursoft.harness.vec.scripting.core.SpecificationRegistry;
import com.foursoft.harness.vec.v2x.*;

import static com.foursoft.harness.vec.scripting.factories.NumericalValueFactory.value;
import static com.foursoft.harness.vec.scripting.factories.NumericalValueFactory.valueWithTolerance;
import static com.foursoft.harness.vec.scripting.factories.ValueRangeFactory.valueRange;

public class PluggableTerminalSpecificationBuilder
        extends PartOrUsageRelatedSpecificationBuilder<VecPluggableTerminalSpecification> {

    private final VecPluggableTerminalSpecification element;
    private final VecWireReception wireReception;
    private final VecTerminalReception terminalReception;
    private final VecWireReceptionSpecification wireReceptionSpecification;
    private final VecTerminalReceptionSpecification terminalReceptionSpecification;
    private final VecSession session;
    private final SpecificationRegistry specificationRegistry;
    private final SpecificationLocator specificationLocator;

    PluggableTerminalSpecificationBuilder(final VecSession session, final SpecificationRegistry specificationRegistry,
                                          final String partNumber, final SpecificationLocator specificationLocator) {
        this.session = session;
        this.specificationRegistry = specificationRegistry;
        this.specificationLocator = specificationLocator;

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

        final VecInternalTerminalConnection terminalConnection = new VecInternalTerminalConnection();
        terminalConnection.setIdentification("IntCon-" + partNumber);
        terminalConnection.getTerminalReception().add(terminalReception);
        terminalConnection.getWireReception().add(wireReception);

        element = initializeSpecification(VecPluggableTerminalSpecification.class, partNumber);
        element.setTerminalType("Standard");

        element.getTerminalReceptions().add(terminalReception);
        element.getWireReceptions().add(wireReception);
        element.getInternalTerminalConnections().add(terminalConnection);
    }

    public PluggableTerminalSpecificationBuilder withInsulationDisplacementLength(final double value,
                                                                                  final double lowerTolerance,
                                                                                  final double upperTolerance) {
        this.wireReceptionSpecification.setInsulationDisplacementLength(
                valueWithTolerance(value, lowerTolerance, upperTolerance, session.mm()));
        return this;
    }

    public PluggableTerminalSpecificationBuilder withSealable(final boolean sealable) {
        this.wireReceptionSpecification.setSealable(sealable);
        return this;
    }

    public PluggableTerminalSpecificationBuilder withConnectionBLength(final double value) {
        this.wireReceptionSpecification.setConnectionBLength(value(value, session.mm()));
        return this;
    }

    public PluggableTerminalSpecificationBuilder withRearBellMouth(final double value) {
        final VecNumericalValue numericalValue = value(value, session.mm());
        session.addXmlComment(numericalValue, " Excelsheet has Min- & MaxBackBellmouth. How is this connected?");
        this.wireReceptionSpecification.setRearBellMouthLength(numericalValue);
        return this;
    }

    public PluggableTerminalSpecificationBuilder withTerminalLengthOverall(final double value) {
        this.element.setOverallLength(value(value, session.mm()));

        return this;
    }

    public PluggableTerminalSpecificationBuilder withInsulationCrimpLegHeight(final double value) {
        this.wireReceptionSpecification.setInsulationCrimpLegHeight(value(value, session.mm()));
        return this;
    }

    public PluggableTerminalSpecificationBuilder withSheetThickness(final double value) {
        this.wireReceptionSpecification.setSheetThickness(value(value, session.mm()));
        return this;
    }

    public PluggableTerminalSpecificationBuilder withInsulationCrimpShape(final String value) {
        this.wireReceptionSpecification.setInsulationCrimpShape(value);
        return this;
    }

    public PluggableTerminalSpecificationBuilder withWireTipProtrusion(final double min, final double max) {
        this.wireReceptionSpecification.setWireTipProtrusion(valueRange(min, max, session.mm()));
        return this;
    }

    public PluggableTerminalSpecificationBuilder withMissingAttributesComment() {
        session.addXmlComment(this.element,
                              " The following attributes are currently not mapped in the VEC, issues should be " +
                                      "created (custom properties would be possible): SealPosition + Tolerance " +
                                      "(KBLFRM-1237)");
        return this;
    }

    public PluggableTerminalSpecificationBuilder addCoreCrimpDetails(final String coreIdentification,
                                                                     final Customizer<CoreCrimpDetailBuilder> crimpDetailsCustomizer) {
        final CoreCrimpDetailBuilder crimpDetailsBuilder = new CoreCrimpDetailBuilder(session, specificationLocator,
                                                                                      coreIdentification);

        crimpDetailsCustomizer.customize(crimpDetailsBuilder);

        wireReceptionSpecification.getCoreCrimpDetails().add(crimpDetailsBuilder.build());

        return this;
    }

    @Override public VecPluggableTerminalSpecification build() {
        specificationRegistry.register(wireReceptionSpecification);
        specificationRegistry.register(terminalReceptionSpecification);

        return this.element;
    }

}
