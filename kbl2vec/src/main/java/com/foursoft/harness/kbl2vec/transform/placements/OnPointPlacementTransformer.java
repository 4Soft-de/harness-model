package com.foursoft.harness.kbl2vec.transform.placements;

import com.foursoft.harness.kbl.v25.KblFixingAssignment;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecOnPointPlacement;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v2x.VecSegmentLocation;

public class OnPointPlacementTransformer implements Transformer<KblFixingAssignment, VecOnPointPlacement> {

    @Override
    public TransformationResult<VecOnPointPlacement> transform(final TransformationContext context,
                                                               final KblFixingAssignment source) {
        final VecOnPointPlacement destination = new VecOnPointPlacement();

        return TransformationResult.from(destination)
                .withDownstream(KblFixingAssignment.class, VecSegmentLocation.class, Query.of(source),
                                VecOnPointPlacement::getLocations)
                .withLinker(Query.of(source::getFixing), VecPlaceableElementRole.class,
                            VecOnPointPlacement::getPlacedElement)
                .build();
    }
}
