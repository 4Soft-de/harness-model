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

import com.foursoft.harness.vec.scripting.core.SpecificationLocator;
import com.foursoft.harness.vec.scripting.core.SpecificationRegistry;
import com.foursoft.harness.vec.v2x.*;

import static com.foursoft.harness.vec.scripting.factories.NumericalValueFactory.valueWithTolerance;

public class WireElementBuilder implements Builder<VecWireElement> {
    private final VecWireElement wireElement = new VecWireElement();
    private final VecWireElementSpecification wireElementSpecification = new VecWireElementSpecification();
    private final VecSession session;
    private final SpecificationRegistry specificationRegistry;
    private final SpecificationLocator specificationLocator;

    WireElementBuilder(final VecSession session, final String identification,
                       SpecificationRegistry specificationRegistry, SpecificationLocator specificationLocator) {
        this.session = session;
        this.specificationRegistry = specificationRegistry;
        this.specificationLocator = specificationLocator;

        wireElement.setIdentification(identification);
        wireElementSpecification.setIdentification(wireElement.getIdentification());
        wireElement.setWireElementSpecification(wireElementSpecification);
    }

    public WireElementBuilder addSubWireElement(String identification, Customizer<WireElementBuilder> customizer) {
        WireElementBuilder builder = new WireElementBuilder(this.session, identification, this.specificationRegistry,
                                                            this.specificationLocator);

        customizer.customize(builder);

        wireElement.getSubWireElements().add(builder.build());

        return this;
    }

    public WireElementBuilder withCoreSpecification(String identification) {
        VecCoreSpecification coreSpecification = this.specificationLocator.find(VecCoreSpecification.class,
                                                                                identification)
                .orElseThrow(IllegalArgumentException::new);

        return withConductorSpecification(coreSpecification);
    }

    public WireElementBuilder withConductorSpecification(VecConductorSpecification conductorSpecification) {
        wireElementSpecification.setConductorSpecification(conductorSpecification);

        return this;
    }

    public WireElementBuilder addInsulationSpecification(String identification,
                                                         Customizer<InsulationSpecificationBuilder> customizer) {
        InsulationSpecificationBuilder builder = new InsulationSpecificationBuilder(session,
                                                                                    identification);
        customizer.customize(builder);

        return withInsulationSpecification(builder.build());
    }

    public WireElementBuilder withInsulationSpecification(VecInsulationSpecification specification) {
        specificationRegistry.register(specification);
        wireElementSpecification.setInsulationSpecification(specification);

        return this;
    }

    public WireElementBuilder withDin76722WireType(final String wireType) {
        VecWireType type = new VecWireType();
        type.setType(wireType);
        type.setReferenceSystem("DIN 76722");

        this.wireElementSpecification.getTypes().add(type);

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
