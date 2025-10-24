package com.foursoft.harness.kbl2vec.transform.topology.placements.wire_protection;

import com.foursoft.harness.kbl.v25.KblWireProtectionOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecOnWayPlacement;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;

public class OnWayPlacementTransformer implements Transformer<KblWireProtectionOccurrence, VecOnWayPlacement> {

    @Override
    public TransformationResult<VecOnWayPlacement> transform(final TransformationContext context,
                                                             final KblWireProtectionOccurrence source) {
        final VecOnWayPlacement destination = new VecOnWayPlacement();
        destination.setIdentification(source.getId());

        return TransformationResult.from(destination)
                .withLinker(Query.of(source), VecPlaceableElementRole.class, VecOnWayPlacement::getPlacedElement)
                .build();
    }
}
