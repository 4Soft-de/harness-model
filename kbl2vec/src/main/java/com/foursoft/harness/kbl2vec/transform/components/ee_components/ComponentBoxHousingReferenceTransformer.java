/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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

import com.foursoft.harness.kbl.v25.KblCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentBoxConnectorOccurrence;
import com.foursoft.harness.kbl.v25.KblSlotOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecConnectorHousingRole;
import com.foursoft.harness.vec.v2x.VecHousingComponent;
import com.foursoft.harness.vec.v2x.VecHousingComponentReference;
import com.foursoft.harness.vec.v2x.VecPinComponentReference;

import java.util.List;

public class ComponentBoxHousingReferenceTransformer
        implements Transformer<KblComponentBoxConnectorOccurrence, VecHousingComponentReference> {

    @Override
    public TransformationResult<VecHousingComponentReference> transform(final TransformationContext context,
                                                                        final KblComponentBoxConnectorOccurrence source) {
        final VecHousingComponentReference destination = new VecHousingComponentReference();
        destination.setIdentification(source.getPart().getId());

        final List<KblCavityOccurrence> cavityOccurrences = source.getSlots().stream()
                .filter(KblSlotOccurrence.class::isInstance)
                .map(KblSlotOccurrence.class::cast)
                .flatMap(slotOccurrence -> slotOccurrence.getCavities().stream())
                .toList();

        return TransformationResult.from(destination)
                .withDownstream(KblCavityOccurrence.class, VecPinComponentReference.class,
                                Query.fromLists(cavityOccurrences), VecHousingComponentReference::getPinComponentReves)
                .withDownstream(KblComponentBoxConnectorOccurrence.class, VecConnectorHousingRole.class,
                                Query.of(source), VecHousingComponentReference::setConnectorHousingRole)
                .withLinker(Query.of(source::getPart), VecHousingComponent.class,
                            VecHousingComponentReference::setHousingComponent)
                .build();
    }
}
