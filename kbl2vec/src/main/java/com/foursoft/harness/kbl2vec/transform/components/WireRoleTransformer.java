package com.foursoft.harness.kbl2vec.transform.components;

import com.foursoft.harness.kbl.v25.ConnectionOrOccurrence;
import com.foursoft.harness.kbl.v25.KblGeneralWireOccurrence;
import com.foursoft.harness.kbl.v25.KblSpecialWireOccurrence;
import com.foursoft.harness.kbl.v25.KblWireOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecWireElementReference;
import com.foursoft.harness.vec.v2x.VecWireRole;
import com.foursoft.harness.vec.v2x.VecWireSpecification;

public class WireRoleTransformer implements Transformer<ConnectionOrOccurrence, VecWireRole> {
    @Override
    public TransformationResult<VecWireRole> transform(final TransformationContext context,
                                                       final ConnectionOrOccurrence source) {
        if (source instanceof final KblGeneralWireOccurrence sourceWireOccurrence) {
            final VecWireRole dest = new VecWireRole();

            if (sourceWireOccurrence instanceof final KblWireOccurrence wireOccurrence) {
                dest.setIdentification(wireOccurrence.getWireNumber());
            } else if (sourceWireOccurrence instanceof final KblSpecialWireOccurrence specialWireOccurrence) {
                dest.setIdentification(specialWireOccurrence.getSpecialWireId());
            } else {
                context.getLogger().warn("'{}' has a unsupported wire class type", sourceWireOccurrence);
            }

            return TransformationResult
                    .from(dest)
                    .withDownstream(KblGeneralWireOccurrence.class, VecWireElementReference.class,
                                    Query.of(sourceWireOccurrence), VecWireRole::getWireElementReferences)
                    .withLinker(Query.of(sourceWireOccurrence::getPart), VecWireSpecification.class,
                                VecWireRole::setWireSpecification)
                    .build();

        }
        return TransformationResult.noResult();
    }
}
