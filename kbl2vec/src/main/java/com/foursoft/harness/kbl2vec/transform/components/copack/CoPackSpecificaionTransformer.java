package com.foursoft.harness.kbl2vec.transform.components.copack;

import com.foursoft.harness.kbl.v25.KblCoPackPart;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecPartOrUsageRelatedSpecification;

import static com.foursoft.harness.kbl2vec.transform.Fragments.commonSpecificationAttributes;

public class CoPackSpecificaionTransformer implements Transformer<KblCoPackPart, VecPartOrUsageRelatedSpecification> {

    @Override
    public TransformationResult<VecPartOrUsageRelatedSpecification> transform(final TransformationContext context,
                                                                                final KblCoPackPart source) {
        final VecPartOrUsageRelatedSpecification destination = new VecPartOrUsageRelatedSpecification();
        destination.setSpecialPartType(source.getPartType());

        return TransformationResult.from(destination)
                .withFragment(commonSpecificationAttributes(source))
                .build();
    }
}
