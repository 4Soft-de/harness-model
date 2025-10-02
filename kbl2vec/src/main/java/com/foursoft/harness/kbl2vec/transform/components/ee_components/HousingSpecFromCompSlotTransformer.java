package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponentSlot;
import com.foursoft.harness.kbl.v25.KblSlot;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v2x.VecSlot;

import java.util.List;

public class HousingSpecFromCompSlotTransformer
        implements Transformer<KblComponentSlot, VecConnectorHousingSpecification> {

    @Override
    public TransformationResult<VecConnectorHousingSpecification> transform(final TransformationContext context,
                                                                            final KblComponentSlot source) {
        final VecConnectorHousingSpecification destination = new VecConnectorHousingSpecification();
        destination.setIdentification(source.getId());

        final List<KblSlot> slots = source.getComponentCavities().stream()
                .map(c -> new KblSlot())
                .toList();

        return TransformationResult.from(destination)
                .withDownstream(KblSlot.class, VecSlot.class, Query.fromLists(slots),
                                VecConnectorHousingSpecification::getSlots)
                .build();
    }
}
