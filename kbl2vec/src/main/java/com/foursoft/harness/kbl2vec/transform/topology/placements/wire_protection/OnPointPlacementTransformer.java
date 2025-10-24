package com.foursoft.harness.kbl2vec.transform.topology.placements.wire_protection;

import com.foursoft.harness.kbl.v25.KblWireProtectionOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecOnPointPlacement;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;

public class OnPointPlacementTransformer implements Transformer<KblWireProtectionOccurrence, VecOnPointPlacement> {

    @Override
    public TransformationResult<VecOnPointPlacement> transform(final TransformationContext context,
                                                               final KblWireProtectionOccurrence source) {
        final VecOnPointPlacement destination = new VecOnPointPlacement();
        destination.setIdentification(source.getId());

        return TransformationResult.from(destination)
                .withLinker(Query.of(source), VecPlaceableElementRole.class, VecOnPointPlacement::getPlacedElement)
                .build();
    }
}
