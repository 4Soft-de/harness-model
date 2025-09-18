package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.KblCavitySealOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCavitySealRole;
import com.foursoft.harness.vec.v2x.VecCavitySealSpecification;

public class CavitySealRoleTransformer implements Transformer<KblCavitySealOccurrence, VecCavitySealRole> {

    @Override
    public TransformationResult<VecCavitySealRole> transform(
            final TransformationContext context, final KblCavitySealOccurrence source
    ) {
        final VecCavitySealRole dest = new VecCavitySealRole();
        dest.setIdentification(source.getId());

        return TransformationResult
                .from(dest)
                .withLinker(
                        Query.of(source::getPart),
                        VecCavitySealSpecification.class,
                        VecCavitySealRole::setCavitySealSpecification
                )
                .build();
    }
}
