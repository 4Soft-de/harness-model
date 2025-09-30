package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponentCavity;
import com.foursoft.harness.kbl.v25.KblComponentSlot;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecHousingComponent;
import com.foursoft.harness.vec.v2x.VecPinComponent;

public class HousingComponentFromComponentSlotTransformer implements Transformer<KblComponentSlot, VecHousingComponent> {

    @Override
    public TransformationResult<VecHousingComponent> transform(final TransformationContext context,
                                                               final KblComponentSlot source) {
        final VecHousingComponent destination = new VecHousingComponent();
        destination.setIdentification(source.getId());

        return TransformationResult.from(destination)
                .withDownstream(KblComponentCavity.class, VecPinComponent.class,
                                Query.fromLists(source.getComponentCavities()),
                                VecHousingComponent::getPinComponents)
                .build();
    }
}
