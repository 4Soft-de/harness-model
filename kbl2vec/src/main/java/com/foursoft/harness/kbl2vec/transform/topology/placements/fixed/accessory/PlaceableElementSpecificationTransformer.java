package com.foursoft.harness.kbl2vec.transform.topology.placements.fixed.accessory;

import com.foursoft.harness.kbl.v25.KblAccessory;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecPlaceableElementSpecification;
import com.foursoft.harness.vec.v2x.VecPlacementType;

import static com.foursoft.harness.kbl2vec.transform.Fragments.commonSpecificationAttributes;

public class PlaceableElementSpecificationTransformer
        implements Transformer<KblAccessory, VecPlaceableElementSpecification> {

    @Override
    public TransformationResult<VecPlaceableElementSpecification> transform(final TransformationContext context,
                                                                            final KblAccessory source) {
        final VecPlaceableElementSpecification destination = new VecPlaceableElementSpecification();
        destination.getValidPlacementTypes().add(VecPlacementType.ON_POINT);

        return TransformationResult.from(destination)
                .withFragment(commonSpecificationAttributes(source))
                .build();
    }
}
