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

import com.foursoft.harness.vec.v2x.*;

public class WireElementBuilder<P extends Builder> extends AbstractChildBuilder<P> {
    private final WireElementBuilderContext context;

    private final VecWireElement wireElement = new VecWireElement();
    private final VecWireElementSpecification wireElementSpecification = new VecWireElementSpecification();

    WireElementBuilder(final P parent, WireElementBuilderContext context, final String identification) {
        super(parent);
        this.context = context;

        wireElement.setIdentification(identification);
        wireElementSpecification.setIdentification(wireElement.getIdentification());
        wireElement.setWireElementSpecification(wireElementSpecification);

        context.addWireElement(wireElement);
        context.addSpecification(wireElementSpecification);
    }

    public WireElementBuilder<WireElementBuilder<P>> addSubWireElement(String identification) {
        return new WireElementBuilder<>(this, new WireElementContext(), identification);
    }

    public WireElementBuilder<P> withCoreSpecification(String identification) {
        VecCoreSpecification coreSpecification = Queries.findSpecification(context.partMasterDocument(),
                                                                           identification,
                                                                           VecCoreSpecification.class)
                .orElseThrow(IllegalArgumentException::new);

        wireElementSpecification.setConductorSpecification(coreSpecification);

        return this;
    }

    public InsulationSpecificationBuilder<WireElementBuilder<P>> addInsulationSpecification(String identification) {
        return new InsulationSpecificationBuilder<>(this, this.context.partMasterDocument(), wireElementSpecification,
                                                    identification);
    }

    private class WireElementContext implements WireElementBuilderContext {

        @Override public VecDocumentVersion partMasterDocument() {
            return context.partMasterDocument();
        }

        @Override public void addSpecification(final VecSpecification specification) {
            context.addSpecification(specification);
        }

        @Override public void addWireElement(final VecWireElement element) {
            wireElement.getSubWireElements().add(element);
            wireElementSpecification.getSubWireElementSpecification().add(element.getWireElementSpecification());
        }
    }

}
