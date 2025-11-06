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
package com.foursoft.harness.kbl2vec.transform.placements;

import com.foursoft.harness.kbl.common.HasIdentification;
import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecNodeLocation;
import com.foursoft.harness.vec.v2x.VecOnPointPlacement;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v2x.VecSegmentLocation;

public class OnPointPlacementTransformer implements Transformer<ConnectionOrOccurrence, VecOnPointPlacement> {

    @Override
    public TransformationResult<VecOnPointPlacement> transform(final TransformationContext context,
                                                               final ConnectionOrOccurrence source) {
        if (!(source instanceof FixedComponent || source instanceof LocatedComponent)) {
            return TransformationResult.noResult();
        }

        final VecOnPointPlacement destination = new VecOnPointPlacement();
        if (source instanceof final HasIdentification hasIdentification) {
            destination.setIdentification(hasIdentification.getId());
        } else {
            destination.setIdentification("FIXING_PLACEMENT");
        }

        final TransformationResult.Builder<VecOnPointPlacement> builder = TransformationResult.from(destination);
        if (source instanceof final FixedComponent fixing && !fixing.getRefFixingAssignment().isEmpty()) {
            builder.withDownstream(KblFixingAssignment.class, VecSegmentLocation.class,
                                   Query.fromLists(fixing.getRefFixingAssignment().stream().toList()),
                                   VecOnPointPlacement::getLocations);
        }

        if (source instanceof final LocatedComponent locatedComponent && !locatedComponent.getRefNode().isEmpty()) {
            builder.withDownstream(KblNode.class, VecNodeLocation.class,
                                   Query.fromLists(locatedComponent.getRefNode().stream().toList()),
                                   VecOnPointPlacement::getLocations);
        }

        return builder
                .withLinker(Query.of(source), VecPlaceableElementRole.class, VecOnPointPlacement::getPlacedElement)
                .build();
    }
}
