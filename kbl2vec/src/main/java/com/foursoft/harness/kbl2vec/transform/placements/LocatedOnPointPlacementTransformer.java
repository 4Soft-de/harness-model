package com.foursoft.harness.kbl2vec.transform.placements;

import com.foursoft.harness.kbl.common.HasIdentification;
import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.*;
import com.foursoft.harness.vec.v2x.*;

public class LocatedOnPointPlacementTransformer implements Transformer<LocatedComponent, VecOnPointPlacement> {

    @Override
    public TransformationResult<VecOnPointPlacement> transform(final TransformationContext context,
                                                               final LocatedComponent source) {
        if (source.getRefNode() == null || source instanceof KblComponentBoxConnectorOccurrence) {
            return TransformationResult.noResult();
        }

        final VecOnPointPlacement destination = new VecOnPointPlacement();

        if (source instanceof final HasIdentification hasIdentification) {
            destination.setIdentification(hasIdentification.getId());
        }

        return TransformationResult.from(destination)
                .withDownstream(LocatedComponent.class, VecNodeLocation.class, Query.of(source),
                                VecOnPointPlacement::getLocations)
                .withLinker(Query.of(source), VecPlaceableElementRole.class, VecOnPointPlacement::getPlacedElement)
                .build();
    }
}
