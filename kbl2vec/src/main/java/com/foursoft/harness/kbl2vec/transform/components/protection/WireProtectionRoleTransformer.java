package com.foursoft.harness.kbl2vec.transform.components.protection;

import com.foursoft.harness.kbl.v25.KblWireProtectionOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecWireProtectionRole;
import com.foursoft.harness.vec.v2x.VecWireProtectionSpecification;

public class WireProtectionRoleTransformer implements Transformer<KblWireProtectionOccurrence, VecWireProtectionRole> {

    @Override
    public TransformationResult<VecWireProtectionRole> transform(final TransformationContext context,
                                                                 final KblWireProtectionOccurrence source) {

        final VecWireProtectionRole destination = new VecWireProtectionRole();
        destination.setIdentification(source.getId());

        return TransformationResult.from(destination)
                .withLinker(Query.of(source::getPart), VecWireProtectionSpecification.class,
                            VecWireProtectionRole::setWireProtectionSpecification)
                .build();
    }
}
