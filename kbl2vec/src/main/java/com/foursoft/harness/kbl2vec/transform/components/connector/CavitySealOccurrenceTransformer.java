package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.ConnectionOrOccurrence;
import com.foursoft.harness.kbl.v25.KblCavitySealOccurrence;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;

import static com.foursoft.harness.kbl2vec.transform.components.common.Fragments.commonOccurrenceInformation;

public class CavitySealOccurrenceTransformer implements Transformer<ConnectionOrOccurrence, VecPartOccurrence> {

    @Override
    public TransformationResult<VecPartOccurrence> transform(
            final TransformationContext context, final ConnectionOrOccurrence occurrence
    ) {
        if (occurrence instanceof final KblCavitySealOccurrence source) {
            final VecPartOccurrence destination = new VecPartOccurrence();

            return TransformationResult
                    .from(destination)
                    .withFragment(commonOccurrenceInformation(source, context))
                    .build();
        }
        return TransformationResult.noResult();
    }
}
