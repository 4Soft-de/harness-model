package com.foursoft.harness.kbl2vec.transform.topology.placements.fixed.fixing;

import com.foursoft.harness.kbl.v25.KblFixingOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v2x.VecPlaceableElementSpecification;

public class PlaceableElementRoleTransformer implements Transformer<KblFixingOccurrence, VecPlaceableElementRole> {

    @Override
    public TransformationResult<VecPlaceableElementRole> transform(final TransformationContext context,
                                                                   final KblFixingOccurrence source) {
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
