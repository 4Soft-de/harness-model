package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponentBox;
import com.foursoft.harness.kbl.v25.KblComponentBoxConnection;
import com.foursoft.harness.kbl.v25.KblComponentBoxConnector;
import com.foursoft.harness.kbl.v25.KblComponentSlot;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecEEComponentSpecification;
import com.foursoft.harness.vec.v2x.VecHousingComponent;
import com.foursoft.harness.vec.v2x.VecInternalComponentConnection;

import static com.foursoft.harness.kbl2vec.transform.Fragments.commonSpecificationAttributes;

public class EEComponentSpecificationTransformer implements Transformer<KblComponentBox, VecEEComponentSpecification> {

    @Override
    public TransformationResult<VecEEComponentSpecification> transform(final TransformationContext context,
                                                                       final KblComponentBox source) {
        final VecEEComponentSpecification destination = new VecEEComponentSpecification();

        return TransformationResult.from(destination)
                .withFragment(commonSpecificationAttributes(source))
                .withDownstream(KblComponentSlot.class, VecHousingComponent.class,
                                Query.fromLists(source.getComponentSlots()),
                                VecEEComponentSpecification::getHousingComponents)
                .withDownstream(KblComponentBoxConnector.class, VecHousingComponent.class,
                                Query.fromLists(source.getComponentBoxConnectors()),
                                VecEEComponentSpecification::getHousingComponents)
                .withDownstream(KblComponentBoxConnection.class, VecInternalComponentConnection.class,
                                Query.fromLists(source.getConnections()), VecEEComponentSpecification::getConnections)
                .build();
    }
}
