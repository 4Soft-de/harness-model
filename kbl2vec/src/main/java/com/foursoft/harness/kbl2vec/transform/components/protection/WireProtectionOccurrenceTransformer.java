package com.foursoft.harness.kbl2vec.transform.components.protection;

import com.foursoft.harness.kbl.v25.ConnectionOrOccurrence;
import com.foursoft.harness.kbl.v25.KblWireProtectionOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsage;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecWireProtectionRole;

import static com.foursoft.harness.kbl2vec.transform.components.common.Fragments.commonOccurrenceInformation;

public class WireProtectionOccurrenceTransformer implements Transformer<ConnectionOrOccurrence, VecPartOccurrence> {

    @Override
    public TransformationResult<VecPartOccurrence> transform(final TransformationContext context,
                                                             final ConnectionOrOccurrence occurrence) {
        if (occurrence instanceof final KblWireProtectionOccurrence source) {
            final VecPartOccurrence destination = new VecPartOccurrence();

            return TransformationResult.from(destination)
                    .withFragment(commonOccurrenceInformation(source, context))
                    .withDownstream(
                            KblWireProtectionOccurrence.class, VecWireProtectionRole.class, Query.of(source),
                            VecOccurrenceOrUsage::getRoles
                    )
                    .build();
        }
        return TransformationResult.noResult();
    }
}
