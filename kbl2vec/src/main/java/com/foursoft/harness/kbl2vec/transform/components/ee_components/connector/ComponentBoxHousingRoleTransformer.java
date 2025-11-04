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
package com.foursoft.harness.kbl2vec.transform.components.ee_components.connector;

import com.foursoft.harness.kbl.v25.KblComponentBoxConnectorOccurrence;
import com.foursoft.harness.kbl.v25.KblSlotOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v2x.VecConnectorHousingRole;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v2x.VecSlotReference;

import java.util.List;

public class ComponentBoxHousingRoleTransformer
        implements Transformer<KblComponentBoxConnectorOccurrence, VecConnectorHousingRole> {

    @Override
    public TransformationResult<VecConnectorHousingRole> transform(final TransformationContext context,
                                                                   final KblComponentBoxConnectorOccurrence source) {
        final VecConnectorHousingRole destination = new VecConnectorHousingRole();
        destination.setIdentification(source.getPart().getId());

        return TransformationResult.from(destination)
                .withDownstream(KblSlotOccurrence.class, VecSlotReference.class,
                                Query.fromLists(getSlotOccurrences(source)), VecConnectorHousingRole::getSlotReferences)
                .withLinker(Query.of(source::getPart), VecConnectorHousingSpecification.class,
                            VecConnectorHousingRole::setConnectorHousingSpecification)
                .build();
    }

    private List<KblSlotOccurrence> getSlotOccurrences(final KblComponentBoxConnectorOccurrence source) {
        return source.getSlots().stream()
                .flatMap(StreamUtils.ofClass(KblSlotOccurrence.class))
                .toList();
    }
}
