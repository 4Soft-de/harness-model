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

import com.foursoft.harness.vec.v2x.VecDocumentVersion;

public class WireSingleCoreBuilder extends AbstractChildBuilder<ComponentMasterDataBuilder> {

    private final VecDocumentVersion partMasterDocument;
    private final CoreSpecificationBuilder coreSpecificationBuilder;
    private final WireSpecificationBuilder wireSpecificationBuilder;
    private final WireElementBuilder<WireSpecificationBuilder> wireElementBuilder;
    private final InsulationSpecificationBuilder<WireElementBuilder<WireSpecificationBuilder>>
            insulationSpecificationBuilder;

    public WireSingleCoreBuilder(final ComponentMasterDataBuilder parent, final String partNumber,
                                 final VecDocumentVersion partMasterDocument) {
        super(parent);
        this.partMasterDocument = partMasterDocument;

        String coreId = "Core-" + partNumber;
        coreSpecificationBuilder = parent.addCoreSpecification(coreId);
        wireSpecificationBuilder = parent.addWireSpecification();

        wireElementBuilder = wireSpecificationBuilder.withWireElement("1").withCoreSpecification(coreId);
        insulationSpecificationBuilder = wireElementBuilder.addInsulationSpecification(
                "Ins-" + partNumber);

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
}