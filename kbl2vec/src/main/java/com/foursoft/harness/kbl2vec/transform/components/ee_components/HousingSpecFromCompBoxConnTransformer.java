package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblAbstractSlot;
import com.foursoft.harness.kbl.v25.KblComponentBoxConnector;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v2x.VecSlot;

public class HousingSpecFromCompBoxConnTransformer
        implements Transformer<KblComponentBoxConnector, VecConnectorHousingSpecification> {

    @Override
    public TransformationResult<VecConnectorHousingSpecification> transform(final TransformationContext context,
                                                                            final KblComponentBoxConnector source) {
        final VecConnectorHousingSpecification destination = new VecConnectorHousingSpecification();
        destination.setIdentification(source.getId());

        return TransformationResult.from(destination)
                .withDownstream(KblAbstractSlot.class, VecSlot.class, Query.fromLists(source.getIntegratedSlots()),
                                VecConnectorHousingSpecification::getSlots)
                .build();
    }
}
