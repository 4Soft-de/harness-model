package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentBoxConnectorOccurrence;
import com.foursoft.harness.kbl.v25.KblSlotOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecConnectorHousingRole;
import com.foursoft.harness.vec.v2x.VecHousingComponentReference;
import com.foursoft.harness.vec.v2x.VecPinComponentReference;

import java.util.List;

public class HousingCompFromCompBoxConnReferenceTransformer
        implements Transformer<KblComponentBoxConnectorOccurrence, VecHousingComponentReference> {

    @Override
    public TransformationResult<VecHousingComponentReference> transform(final TransformationContext context,
                                                                        final KblComponentBoxConnectorOccurrence source) {
        final VecHousingComponentReference destination = new VecHousingComponentReference();

        final List<KblCavityOccurrence> cavityOccurrences = source.getSlots().stream()
                .filter(KblSlotOccurrence.class::isInstance)
                .map(KblSlotOccurrence.class::cast)
                .flatMap(slotOccurrence -> slotOccurrence.getCavities().stream())
                .toList();

        return TransformationResult.from(destination)
                .withDownstream(KblCavityOccurrence.class, VecPinComponentReference.class,
                                Query.fromLists(cavityOccurrences), VecHousingComponentReference::getPinComponentReves)
                .withDownstream(KblComponentBoxConnectorOccurrence.class, VecConnectorHousingRole.class,
                                Query.of(source), VecHousingComponentReference::setConnectorHousingRole)
                .build();
    }
}
