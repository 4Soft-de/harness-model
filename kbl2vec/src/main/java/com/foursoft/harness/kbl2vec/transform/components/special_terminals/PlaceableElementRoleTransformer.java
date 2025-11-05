package com.foursoft.harness.kbl2vec.transform.components.special_terminals;

import com.foursoft.harness.kbl.v25.KblSpecialTerminalOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v2x.VecPlaceableElementSpecification;

public class PlaceableElementRoleTransformer
        implements Transformer<KblSpecialTerminalOccurrence, VecPlaceableElementRole> {

    @Override
    public TransformationResult<VecPlaceableElementRole> transform(final TransformationContext context,
                                                                   final KblSpecialTerminalOccurrence source) {
        final VecPlaceableElementRole destination = new VecPlaceableElementRole();
        destination.setIdentification(source.getId());

        return TransformationResult.from(destination)
                .withLinker(Query.of(source::getPart), VecPlaceableElementSpecification.class,
                            VecPlaceableElementRole::setPlaceableElementSpecification)
                .build();
    }
}
