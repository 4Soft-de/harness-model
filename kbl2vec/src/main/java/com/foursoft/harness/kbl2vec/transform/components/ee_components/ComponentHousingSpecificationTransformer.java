/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 - 2026 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponent;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.kbl2vec.transform.components.ee_components.ComponentCavities.ComponentSlot;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v2x.VecSlot;

import java.util.List;

import static com.foursoft.harness.vec.common.util.ModelClassNames.abbreviatedModelClassName;

/**
 * The housing of the single {@link ComponentHousingTransformer housing component} of a {@link KblComponent}. KBL
 * describes no slots or cavities of a component. If its occurrences are mounted on cavities, the housing gets a single
 * slot with the derived cavities (see {@link ComponentCavities}), otherwise it has none.
 */
public class ComponentHousingSpecificationTransformer
        implements Transformer<KblComponent, VecConnectorHousingSpecification> {

    @Override
    public TransformationResult<VecConnectorHousingSpecification> transform(final TransformationContext context,
                                                                            final KblComponent source) {
        final VecConnectorHousingSpecification destination = new VecConnectorHousingSpecification();
        destination.setIdentification(abbreviatedModelClassName(destination.getClass()) + "-" + source.getPartNumber());
        final List<ComponentSlot> slots = ComponentCavities.cavities(source).isEmpty()
                ? List.of()
                : List.of(new ComponentSlot(source));
        return TransformationResult.from(destination)
                .withDownstream(ComponentSlot.class, VecSlot.class, Query.fromLists(slots),
                                VecConnectorHousingSpecification::getSlots)
                .build();
    }
}
