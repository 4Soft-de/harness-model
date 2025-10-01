package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.ConnectionOrOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentBoxOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecEEComponentRole;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsage;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;

import static com.foursoft.harness.kbl2vec.transform.components.common.Fragments.commonOccurrenceInformation;

public class EEComponentOccurrenceTransformer implements Transformer<ConnectionOrOccurrence, VecPartOccurrence> {

    @Override
    public TransformationResult<VecPartOccurrence> transform(final TransformationContext context,
                                                             final ConnectionOrOccurrence occurrence) {
        if (occurrence instanceof final KblComponentBoxOccurrence source) {
            final VecPartOccurrence destination = new VecPartOccurrence();

            return TransformationResult.from(destination)
                    .withFragment(commonOccurrenceInformation(source, context))
                    .withDownstream(KblComponentBoxOccurrence.class, VecEEComponentRole.class, Query.of(source),
                                    VecOccurrenceOrUsage::getRoles)
                    .build();
        }
        return TransformationResult.noResult();
    }
}
