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
import com.foursoft.harness.vec.v2x.VecWireElement;
import com.foursoft.harness.vec.v2x.VecWireSpecification;

public class WireSpecificationBuilder extends PartOrUsageRelatedSpecificationBuilder<VecWireSpecification> {

    private final VecSession session;
    private final VecWireSpecification wireSpecification;
    private final SpecificationLocator specificationLocator;
    private final SpecificationRegistry specificationRegistry;

    WireSpecificationBuilder(VecSession session, final String partNumber,
                             SpecificationRegistry specificationRegistry, SpecificationLocator specificationLocator) {
        this.session = session;
        this.specificationRegistry = specificationRegistry;
        this.specificationLocator = specificationLocator;

        wireSpecification = initializeSpecification(VecWireSpecification.class, partNumber);
    }

    public WireSpecificationBuilder withWireElement(String identification, Customizer<WireElementBuilder> customizer) {
        WireElementBuilder builder = new WireElementBuilder(session, identification, specificationRegistry,
                                                            specificationLocator);

        customizer.customize(builder);

        final VecWireElement wireElement = builder.build();

        return withWireElement(wireElement);
    }

    WireSpecificationBuilder withWireElement(VecWireElement wireElement) {
        wireSpecification.setWireElement(wireElement);

        wireSpecification.setWireElementSpecification(wireElement.getWireElementSpecification());

        return this;
    }

    @Override public VecWireSpecification build() {
        return wireSpecification;
    }

}
