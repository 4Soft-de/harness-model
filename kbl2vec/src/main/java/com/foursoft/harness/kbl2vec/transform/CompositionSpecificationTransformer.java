package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.ConnectionOrOccurrence;
import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCompositionSpecification;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;

public class CompositionSpecificationTransformer implements Transformer<KblHarness, VecCompositionSpecification> {
    @Override public TransformationResult<VecCompositionSpecification> transform(final TransformationContext context,
                                                                                 final KblHarness source) {
        final VecCompositionSpecification compositionSpecification = new VecCompositionSpecification();
        compositionSpecification.setIdentification("COMPONENTS");

        return TransformationResult.from(compositionSpecification)
                .downstreamTransformation(ConnectionOrOccurrence.class, VecPartOccurrence.class,
                                          source::getConnectionOrOccurrences, compositionSpecification::getComponents)
                .build();
    }
}
