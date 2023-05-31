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
import com.foursoft.harness.vec.v2x.VecSpecification;
import com.foursoft.harness.vec.v2x.VecWireElement;
import com.foursoft.harness.vec.v2x.VecWireSpecification;

public class WireSpecificationBuilder extends PartOrUsageRelatedSpecificationBuilder {

    private final VecWireSpecification wireSpecification;

    WireSpecificationBuilder(ComponentMasterDataBuilder parent,
                             final VecDocumentVersion partMasterDocument) {
        super(parent, partMasterDocument);

        wireSpecification = initializeSpecification(VecWireSpecification.class);
    }

    public WireElementBuilder<WireSpecificationBuilder> withWireElement(String identification) {
        return new WireElementBuilder<>(this, new WireSpecificationContext(), identification);
    }

    private class WireSpecificationContext implements WireElementBuilderContext {

        @Override public VecDocumentVersion partMasterDocument() {
            return partMasterDocument;
        }

        @Override public void addSpecification(final VecSpecification specification) {
            partMasterDocument.getSpecifications().add(specification);
        }

        @Override public void addWireElement(final VecWireElement element) {
            wireSpecification.setWireElement(element);
            wireSpecification.setWireElementSpecification(element.getWireElementSpecification());
        }
    }

}
