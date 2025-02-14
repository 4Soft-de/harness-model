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

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.core.SpecificationLocator;
import com.foursoft.harness.vec.scripting.core.SpecificationRegistry;
import com.foursoft.harness.vec.v2x.*;

import static com.foursoft.harness.vec.scripting.factories.NumericalValueFactory.value;
import static com.foursoft.harness.vec.scripting.factories.NumericalValueFactory.valueWithTolerance;
import static com.foursoft.harness.vec.scripting.factories.WireTypeFactory.din76722;

public class WireElementBuilder implements Builder<VecWireElement> {
    private final VecWireElement wireElement = new VecWireElement();
    private final VecWireElementSpecification wireElementSpecification = new VecWireElementSpecification();
    private final VecSession session;
    private final SpecificationRegistry specificationRegistry;
    private final SpecificationLocator specificationLocator;

    WireElementBuilder(final VecSession session, final String identification,
                       final SpecificationRegistry specificationRegistry,
                       final SpecificationLocator specificationLocator) {
        this.session = session;
        this.specificationRegistry = specificationRegistry;
        this.specificationLocator = specificationLocator;

        wireElement.setIdentification(identification);
        wireElementSpecification.setIdentification(wireElement.getIdentification());
        wireElement.setWireElementSpecification(wireElementSpecification);
    }

    public WireElementBuilder addSubWireElement(final String identification,
                                                final Customizer<WireElementBuilder> customizer) {
        final WireElementBuilder builder = new WireElementBuilder(this.session, identification,
                                                                  this.specificationRegistry,
                                                                  this.specificationLocator);

        customizer.customize(builder);

        wireElement.getSubWireElements().add(builder.build());

        return this;
    }

    public WireElementBuilder withCoreSpecification(final String identification) {
        final VecConductorSpecification coreSpecification = this.specificationLocator.find(
                        VecConductorSpecification.class,
                        identification)
                .orElseThrow(IllegalArgumentException::new);

        return withConductorSpecification(coreSpecification);
    }

    public WireElementBuilder withConductorSpecification(final VecConductorSpecification conductorSpecification) {
        wireElementSpecification.setConductorSpecification(conductorSpecification);

        return this;
    }

    public WireElementBuilder addInsulationSpecification(final String identification,
                                                         final Customizer<InsulationSpecificationBuilder> customizer) {
        final InsulationSpecificationBuilder builder = new InsulationSpecificationBuilder(session,
                                                                                          identification);
        customizer.customize(builder);

        return withInsulationSpecification(builder.build());
    }

    public WireElementBuilder withInsulationSpecification(final VecInsulationSpecification specification) {
        specificationRegistry.register(specification);
        wireElementSpecification.setInsulationSpecification(specification);

        return this;
    }

    public WireElementBuilder withDin76722WireType(final String wireType) {
        this.wireElementSpecification.getTypes().add(din76722(wireType));

        return this;
    }

    public WireElementBuilder withMinBendRadiusDynamic(final double radius) {
        this.wireElementSpecification.setMinBendRadiusDynamic(value(radius, session.mm()));

        return this;
    }

    public WireElementBuilder withMinBendRadiusStatic(final double radius) {
        this.wireElementSpecification.setMinBendRadiusStatic(value(radius, session.mm()));

        return this;
    }

    public WireElementBuilder withInductance(final double inductance) {
        final VecDoubleValueProperty cs = new VecDoubleValueProperty();
        cs.setPropertyType("Inductance");
        cs.setValue(inductance);

        wireElementSpecification.getCustomProperties().add(cs);
        session.addXmlComment(cs,
                              "Currently missing in VEC, could be represented with unit with a NumericalValueProperty");
        return this;
    }

    public WireElementBuilder withCapacitance(final double capacitance) {
        final VecDoubleValueProperty cs = new VecDoubleValueProperty();
        cs.setPropertyType("Capacitance");
        cs.setValue(capacitance);

        wireElementSpecification.getCustomProperties().add(cs);
        session.addXmlComment(cs,
                              "Currently missing in VEC, could be represented with unit with a NumericalValueProperty");
        return this;
    }

    public WireElementBuilder withImpedance(final double impedance) {
        this.wireElementSpecification.setImpedance(value(impedance, session.ohm()));

        return this;
    }

    public WireElementBuilder withOutsideDiameter(final double diameter, final double lowerTolerance,
                                                  final double upperTolerance) {
        this.wireElementSpecification.setOutsideDiameter(valueWithTolerance(diameter, lowerTolerance, upperTolerance,
                                                                            session.mm()));

        return this;
    }

    @Override public VecWireElement build() {
        specificationRegistry.register(wireElementSpecification);

        return this.wireElement;
    }

}
