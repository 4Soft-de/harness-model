/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2024 4Soft GmbH
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
package com.foursoft.harness.vec.scripting.harness;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.scripting.Locator;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.components.PartUsageBuilder;
import com.foursoft.harness.vec.scripting.core.SpecificationLocator;
import com.foursoft.harness.vec.v2x.VecConnection;
import com.foursoft.harness.vec.v2x.VecPartStructureSpecification;
import com.foursoft.harness.vec.v2x.VecPartUsage;
import com.foursoft.harness.vec.v2x.VecPartUsageSpecification;

public class VirtualPartStructureBuilder implements Builder<VirtualPartStructureBuilder.VirtualPartStructureResult> {

    private final VecPartStructureSpecification partStructureSpecification;
    private final VecPartUsageSpecification partUsageSpecification;
    private final VecSession session;
    private final SpecificationLocator specificationLocator;
    private final Locator<VecConnection> connectionLookup;

    public VirtualPartStructureBuilder(VecSession session, SpecificationLocator specificationLocator,
                                       Locator<VecConnection> connectionLookup) {
        this.session = session;
        this.specificationLocator = specificationLocator;
        this.connectionLookup = connectionLookup;
        partStructureSpecification = initializePartStructureSpecification();
        partUsageSpecification = initalizePartUsageSpecification();
    }

    @Override public VirtualPartStructureResult build() {
        return new VirtualPartStructureResult(partStructureSpecification, partUsageSpecification);
    }

    public VirtualPartStructureBuilder addPartUsage(String identification, Customizer<PartUsageBuilder> customizer) {
        PartUsageBuilder builder = new PartUsageBuilder(session, identification, specificationLocator,
                                                        connectionLookup);

        customizer.customize(builder);

        VecPartUsage result = builder.build();

        partUsageSpecification.getPartUsages().add(result);
        partStructureSpecification.getInBillOfMaterial().add(result);

        return this;
    }

    private VecPartUsageSpecification initalizePartUsageSpecification() {
        VecPartUsageSpecification result = new VecPartUsageSpecification();
        result.setIdentification("VIRTUAL COMPONENTS");
        return result;
    }

    private VecPartStructureSpecification initializePartStructureSpecification() {
        VecPartStructureSpecification result = new VecPartStructureSpecification();
        result.setIdentification("STRUCTURE");
        return result;
    }

    public record VirtualPartStructureResult(VecPartStructureSpecification bom, VecPartUsageSpecification usages) {
    }

}
