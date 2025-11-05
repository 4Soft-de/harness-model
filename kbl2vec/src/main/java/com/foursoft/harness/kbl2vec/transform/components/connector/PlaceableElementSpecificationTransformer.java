package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.KblConnectorHousing;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecPlaceableElementSpecification;
import com.foursoft.harness.vec.v2x.VecPlacementType;

import static com.foursoft.harness.kbl2vec.transform.Fragments.commonSpecificationAttributes;

public class PlaceableElementSpecificationTransformer
        implements Transformer<KblConnectorHousing, VecPlaceableElementSpecification> {

    @Override
    public TransformationResult<VecPlaceableElementSpecification> transform(final TransformationContext context,
                                                                            final KblConnectorHousing source) {
        final VecPlaceableElementSpecification destination = new VecPlaceableElementSpecification();
        destination.setIdentification(source.getPartNumber());
        destination.getValidPlacementTypes().add(VecPlacementType.ON_POINT);

        return TransformationResult.from(destination)
                .withFragment(commonSpecificationAttributes(source))
                .build();
    }
}
