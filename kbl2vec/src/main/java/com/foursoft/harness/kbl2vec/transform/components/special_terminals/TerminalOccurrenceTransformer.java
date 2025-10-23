package com.foursoft.harness.kbl2vec.transform.components.special_terminals;

import com.foursoft.harness.kbl.v25.ConnectionOrOccurrence;
import com.foursoft.harness.kbl.v25.KblAliasIdentification;
import com.foursoft.harness.kbl.v25.KblSpecialTerminalOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecAliasIdentification;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecTerminalRole;

import static com.foursoft.harness.kbl2vec.transform.components.common.Fragments.commonOccurrenceInformation;

public class TerminalOccurrenceTransformer implements Transformer<ConnectionOrOccurrence, VecPartOccurrence> {

    @Override
    public TransformationResult<VecPartOccurrence> transform(final TransformationContext context,
                                                             final ConnectionOrOccurrence occurrence) {
        if (occurrence instanceof final KblSpecialTerminalOccurrence source) {
            final VecPartOccurrence destination = new VecPartOccurrence();

            return TransformationResult.from(destination)
                    .withFragment(commonOccurrenceInformation(source, context))
                    .withDownstream(KblAliasIdentification.class, VecAliasIdentification.class, source::getAliasIds,
                                    VecPartOccurrence::getAliasIds)
                    .withDownstream(KblSpecialTerminalOccurrence.class, VecTerminalRole.class, Query.of(source),
                                    VecPartOccurrence::getRoles)
                    .build();
        }
        return TransformationResult.noResult();
    }
}
