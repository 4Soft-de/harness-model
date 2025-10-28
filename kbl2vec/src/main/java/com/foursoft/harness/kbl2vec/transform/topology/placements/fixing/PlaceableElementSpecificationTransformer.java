package com.foursoft.harness.kbl2vec.transform.topology.placements.fixing;

import com.foursoft.harness.kbl.v25.KblFixing;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecPlaceableElementSpecification;
import com.foursoft.harness.vec.v2x.VecPlacementType;

public class PlaceableElementSpecificationTransformer
        implements Transformer<KblFixing, VecPlaceableElementSpecification> {

    @Override
    public TransformationResult<VecPlaceableElementSpecification> transform(final TransformationContext context,
                                                                            final KblFixing source) {
        final VecPlaceableElementSpecification destination = new VecPlaceableElementSpecification();
        destination.getValidPlacementTypes().add(VecPlacementType.ON_POINT);

        return TransformationResult.of(destination);
    }
}
