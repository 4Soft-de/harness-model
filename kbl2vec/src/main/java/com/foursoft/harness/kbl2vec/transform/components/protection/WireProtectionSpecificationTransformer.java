package com.foursoft.harness.kbl2vec.transform.components.protection;

import com.foursoft.harness.kbl.v25.KblWireProtection;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecWireProtectionSpecification;

import static com.foursoft.harness.kbl2vec.transform.Fragments.commonSpecificationAttributes;

public class WireProtectionSpecificationTransformer
        implements Transformer<KblWireProtection, VecWireProtectionSpecification> {

    @Override
    public TransformationResult<VecWireProtectionSpecification> transform(final TransformationContext context,
                                                                          final KblWireProtection source) {
        final VecWireProtectionSpecification destination = new VecWireProtectionSpecification();
        destination.setSpecialPartType(source.getProtectionType());

        return TransformationResult.from(destination)
                .withFragment(commonSpecificationAttributes(source))
                .build();
    }
}
