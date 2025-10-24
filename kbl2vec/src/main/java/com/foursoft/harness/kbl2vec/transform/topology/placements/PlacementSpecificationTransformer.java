package com.foursoft.harness.kbl2vec.transform.topology.placements;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblWireProtectionOccurrence;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecOnPointPlacement;
import com.foursoft.harness.vec.v2x.VecOnWayPlacement;
import com.foursoft.harness.vec.v2x.VecPlacementSpecification;

public class PlacementSpecificationTransformer implements Transformer<KblHarness, VecPlacementSpecification> {

    @Override
    public TransformationResult<VecPlacementSpecification> transform(final TransformationContext context,
                                                                     final KblHarness source) {
        final VecPlacementSpecification destination = new VecPlacementSpecification();
        destination.setIdentification("PLACEMENT");

        return TransformationResult.from(destination)
                .withDownstream(KblWireProtectionOccurrence.class, VecOnPointPlacement.class,
                                source::getWireProtectionOccurrences, VecPlacementSpecification::getPlacements)
                .withDownstream(KblWireProtectionOccurrence.class, VecOnWayPlacement.class,
                                source::getWireProtectionOccurrences, VecPlacementSpecification::getPlacements)
                .build();
    }
}
