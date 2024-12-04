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
import com.foursoft.harness.vec.v2x.VecCoreSpecification;
import com.foursoft.harness.vec.v2x.VecInsulationSpecification;
import com.foursoft.harness.vec.v2x.VecWireSpecification;

public class WireSingleCoreBuilder extends PartOrUsageRelatedSpecificationBuilder<VecWireSpecification> {

    private final CoreSpecificationBuilder coreSpecificationBuilder;

    private final WireElementBuilder wireElementBuilder;
    private final InsulationSpecificationBuilder
            insulationSpecificationBuilder;
    private final VecSession session;

    private final WireSpecificationBuilder wireSpecificationBuilder;

    private final SpecificationRegistry registry;

    public WireSingleCoreBuilder(VecSession session, final String partNumber, SpecificationRegistry registry,
                                 SpecificationLocator locator) {
        this.session = session;
        this.registry = registry;

        coreSpecificationBuilder = new CoreSpecificationBuilder(this.session, "Core-" + partNumber);
        insulationSpecificationBuilder = new InsulationSpecificationBuilder(this.session, "Ins-" + partNumber);
        wireSpecificationBuilder = new WireSpecificationBuilder(session, partNumber, registry, locator);

        wireElementBuilder = new WireElementBuilder(session, "1", registry, locator);
    }

    public WireSingleCoreBuilder withCSA(double csa) {
        this.coreSpecificationBuilder.withCSA(csa);
        return this;
    }

    public WireSingleCoreBuilder withOutsideDiameter(double diameter, double lowerTolerance, double upperTolerance) {
        this.wireElementBuilder.withOutsideDiameter(diameter, lowerTolerance, upperTolerance);
        return this;
    }

    public WireSingleCoreBuilder withInsulationThickness(double thickness) {
        this.insulationSpecificationBuilder.withThickness(thickness);
        return this;
    }

    public WireSingleCoreBuilder withDin76722WireType(String wireType) {
        this.wireElementBuilder.withDin76722WireType(wireType);
        return this;
    }

    public WireSingleCoreBuilder withNumberOfStrands(final int numberOfStrands) {
        this.coreSpecificationBuilder.withNumberOfStrands(numberOfStrands);
        return this;
    }

    public WireSingleCoreBuilder withStrandDiameter(final double diameter) {
        this.coreSpecificationBuilder.withStrandDiameter(diameter);
        return this;
    }

    @Override public VecWireSpecification build() {
        VecCoreSpecification coreSpecification = this.coreSpecificationBuilder.build();
        registry.register(coreSpecification);
        VecInsulationSpecification insulationSpecification = this.insulationSpecificationBuilder.build();
        registry.register(insulationSpecification);

        this.wireElementBuilder.withConductorSpecification(coreSpecification);
        this.wireElementBuilder.withInsulationSpecification(insulationSpecification);

        wireSpecificationBuilder.withWireElement(this.wireElementBuilder.build());

        return wireSpecificationBuilder.build();
    }
}
