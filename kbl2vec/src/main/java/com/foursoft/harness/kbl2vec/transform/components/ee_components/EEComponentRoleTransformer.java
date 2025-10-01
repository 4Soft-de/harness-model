package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponentBoxConnectorOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentBoxOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentSlotOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecEEComponentRole;
import com.foursoft.harness.vec.v2x.VecEEComponentSpecification;
import com.foursoft.harness.vec.v2x.VecHousingComponentReference;

public class EEComponentRoleTransformer implements Transformer<KblComponentBoxOccurrence, VecEEComponentRole> {

    @Override
    public TransformationResult<VecEEComponentRole> transform(final TransformationContext context,
                                                              final KblComponentBoxOccurrence source) {
        final VecEEComponentRole destination = new VecEEComponentRole();
        destination.setIdentification(source.getId());

        return TransformationResult.from(destination)
                .withDownstream(KblComponentSlotOccurrence.class, VecHousingComponentReference.class,
                                Query.fromLists(source.getComponentSlots()),
                                VecEEComponentRole::getHousingComponentReves)
                .withDownstream(KblComponentBoxConnectorOccurrence.class, VecHousingComponentReference.class,
                                Query.fromLists(source.getComponentBoxConnectors()),
                                VecEEComponentRole::getHousingComponentReves)
                .withLinker(Query.of(source.getPart()), VecEEComponentSpecification.class,
                            VecEEComponentRole::setEEComponentSpecification)
                .build();
    }
}
