package com.foursoft.harness.kbl2vec.transform.components.accessory;

import com.foursoft.harness.kbl.v25.KblAccessoryOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v2x.VecPlaceableElementSpecification;

public class PlaceableElementRoleTransformer implements Transformer<KblAccessoryOccurrence, VecPlaceableElementRole> {

    @Override
    public TransformationResult<VecPlaceableElementRole> transform(final TransformationContext context,
                                                                   final KblAccessoryOccurrence source) {
        if (source.getRefFixingAssignment().isEmpty()) {
            return TransformationResult.noResult();
        }

        final VecPlaceableElementRole destination = new VecPlaceableElementRole();
        destination.setIdentification(source.getId());

        return TransformationResult.from(destination)
                .withLinker(Query.of(source::getPart), VecPlaceableElementSpecification.class,
                            VecPlaceableElementRole::setPlaceableElementSpecification)
                .build();
    }
}
