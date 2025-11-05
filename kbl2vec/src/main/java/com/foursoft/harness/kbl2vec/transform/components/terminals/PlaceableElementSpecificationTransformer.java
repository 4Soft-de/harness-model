package com.foursoft.harness.kbl2vec.transform.components.terminals;

import com.foursoft.harness.kbl.v25.KblGeneralTerminal;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecPlaceableElementSpecification;
import com.foursoft.harness.vec.v2x.VecPlacementType;

import static com.foursoft.harness.kbl2vec.transform.Fragments.commonSpecificationAttributes;

public class PlaceableElementSpecificationTransformer
        implements Transformer<KblGeneralTerminal, VecPlaceableElementSpecification> {

    @Override
    public TransformationResult<VecPlaceableElementSpecification> transform(final TransformationContext context,
                                                                            final KblGeneralTerminal source) {
        final VecPlaceableElementSpecification destination = new VecPlaceableElementSpecification();
        destination.setIdentification(source.getPartNumber());
        destination.getValidPlacementTypes().add(VecPlacementType.ON_POINT);

        return TransformationResult.from(destination)
                .withFragment(commonSpecificationAttributes(source))
                .build();
    }
}
