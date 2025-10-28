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
package com.foursoft.harness.kbl2vec.transform.topology.placements;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblProtectionArea;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecOnWayPlacement;
import com.foursoft.harness.vec.v2x.VecPlacementSpecification;

import java.util.List;

public class PlacementSpecificationTransformer implements Transformer<KblHarness, VecPlacementSpecification> {

    @Override
    public TransformationResult<VecPlacementSpecification> transform(final TransformationContext context,
                                                                     final KblHarness source) {
        final VecPlacementSpecification destination = new VecPlacementSpecification();
        destination.setIdentification("PLACEMENT");

        return TransformationResult.from(destination)
                .withDownstream(KblProtectionArea.class, VecOnWayPlacement.class,
                                Query.fromLists(protectionAreas(source)), VecPlacementSpecification::getPlacements)
                .build();
    }

    private List<KblProtectionArea> protectionAreas(final KblHarness source) {
        return source.getParentKBLContainer().getSegments().stream()
                .flatMap(s -> s.getProtectionAreas().stream())
                .toList();
    }
}
