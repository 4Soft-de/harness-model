package com.foursoft.harness.kbl2vec.transform.components;

import com.foursoft.harness.kbl.v25.ConnectionOrOccurrence;
import com.foursoft.harness.kbl.v25.KblConnectorOccurrence;
import com.foursoft.harness.kbl.v25.KblSlotOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecConnectorHousingRole;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v2x.VecSlotReference;

public class ConnectorHousingRoleTransformer implements Transformer<ConnectionOrOccurrence, VecConnectorHousingRole> {
    @Override public TransformationResult<VecConnectorHousingRole> transform(final TransformationContext context,
                                                                             final ConnectionOrOccurrence source) {
        if (source instanceof final KblConnectorOccurrence sourceConnector) {
            final VecConnectorHousingRole dest = new VecConnectorHousingRole();
            dest.setIdentification(sourceConnector.getId());

            return TransformationResult.from(dest)
                    .withDownstream(KblSlotOccurrence.class, VecSlotReference.class, sourceConnector::getSlots,
                                    VecConnectorHousingRole::getSlotReferences)
                    .withLinker(Query.of(sourceConnector::getPart), VecConnectorHousingSpecification.class,
                                VecConnectorHousingRole::setConnectorHousingSpecification)
                    .build();
        }
        return TransformationResult.noResult();
    }
}
