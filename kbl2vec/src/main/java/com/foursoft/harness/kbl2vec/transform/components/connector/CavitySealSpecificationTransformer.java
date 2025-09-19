package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.KblCavitySeal;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCavitySealSpecification;

import static com.foursoft.harness.kbl2vec.transform.Fragments.commonSpecificationAttributes;

public class CavitySealSpecificationTransformer implements Transformer<KblCavitySeal, VecCavitySealSpecification> {

    @Override
    public TransformationResult<VecCavitySealSpecification> transform(
            final TransformationContext context,
            final KblCavitySeal source
    ) {
        final VecCavitySealSpecification destination = new VecCavitySealSpecification();
        destination.setSpecialPartType(source.getSealType());

        return TransformationResult
                .from(destination)
                .withFragment(commonSpecificationAttributes(source))
                .build();
    }
}
