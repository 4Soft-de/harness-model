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
import com.foursoft.harness.vec.scripting.Locator;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.core.SpecificationLocator;
import com.foursoft.harness.vec.v2x.*;

public class PartUsageBuilder implements Builder<VecPartUsage> {

    private final VecSession session;
    private final SpecificationLocator specificationLocator;
    private final Locator<VecConnection> connectionLookup;
    private final VecPartUsage partUsage = new VecPartUsage();

    public PartUsageBuilder(VecSession session, String identification, SpecificationLocator specificationLocator,
                            Locator<VecConnection> connectionLookup) {
        this.session = session;
        this.specificationLocator = specificationLocator;
        this.connectionLookup = connectionLookup;
        this.partUsage.setIdentification(identification);

    }

    public PartUsageBuilder addWireSpecification(String specificationIdentification,
                                                 Customizer<WireRoleBuilder> customizer) {
        VecWireSpecification wireSpecification = specificationLocator.find(VecWireSpecification.class,
                                                                           specificationIdentification).orElseThrow();

        partUsage.getPartOrUsageRelatedSpecification().add(wireSpecification);

        WireRoleBuilder wireRoleBuilder = new WireRoleBuilder(session, partUsage.getIdentification(),
                                                              wireSpecification, connectionLookup);

        customizer.customize(wireRoleBuilder);

        VecWireRole wireRole = wireRoleBuilder.build();

        partUsage.getRoles().add(wireRole);

        if (partUsage.getPrimaryPartUsageType() == null) {
            partUsage.setPrimaryPartUsageType(VecPrimaryPartType.WIRE);
        }

        return this;
    }

    @Override
    public VecPartUsage build() {
        if (partUsage.getPrimaryPartUsageType() == null) {
            partUsage.setPrimaryPartUsageType(VecPrimaryPartType.EE_COMPONENT);
        }

        return partUsage;
    }

}
