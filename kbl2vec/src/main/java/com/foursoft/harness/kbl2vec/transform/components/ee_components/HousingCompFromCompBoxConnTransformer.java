package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblCavity;
import com.foursoft.harness.kbl.v25.KblComponentBoxConnector;
import com.foursoft.harness.kbl.v25.KblSlot;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v2x.VecHousingComponent;
import com.foursoft.harness.vec.v2x.VecPinComponent;

import java.util.List;

public class HousingCompFromCompBoxConnTransformer
        implements Transformer<KblComponentBoxConnector, VecHousingComponent> {

    @Override
    public TransformationResult<VecHousingComponent> transform(final TransformationContext context,
                                                               final KblComponentBoxConnector source) {
        final VecHousingComponent destination = new VecHousingComponent();
        destination.setIdentification(source.getId());

        final List<KblCavity> cavities = source.getIntegratedSlots().stream()
                .filter(KblSlot.class::isInstance)
                .map(KblSlot.class::cast)
                .flatMap(kblSlot -> kblSlot.getCavities().stream())
                .toList();

        return TransformationResult.from(destination)
                .withDownstream(KblCavity.class, VecPinComponent.class, Query.fromLists(cavities),
                                VecHousingComponent::getPinComponents)
                .withLinker(Query.of(source), VecConnectorHousingSpecification.class,
                            VecHousingComponent::setHousingSpecification)
                .build();
    }
}
