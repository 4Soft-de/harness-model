package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponentCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentSlotOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecConnectorHousingRole;
import com.foursoft.harness.vec.v2x.VecHousingComponentReference;
import com.foursoft.harness.vec.v2x.VecPinComponentReference;

public class HousingCompFromCompSlotReferenceTransformer
        implements Transformer<KblComponentSlotOccurrence, VecHousingComponentReference> {

    @Override
    public TransformationResult<VecHousingComponentReference> transform(final TransformationContext context,
                                                                        final KblComponentSlotOccurrence source) {
        final VecHousingComponentReference destination = new VecHousingComponentReference();
        destination.setIdentification(source.getId());

        return TransformationResult.from(destination)
                .withDownstream(KblComponentCavityOccurrence.class, VecPinComponentReference.class,
                                Query.fromLists(source.getComponentCavities()),
                                VecHousingComponentReference::getPinComponentReves)
                .withDownstream(KblComponentSlotOccurrence.class, VecConnectorHousingRole.class, Query.of(source),
                                VecHousingComponentReference::setConnectorHousingRole)
                .build();
    }
}
