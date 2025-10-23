package com.foursoft.harness.kbl2vec.transform.components.special_terminals;

import com.foursoft.harness.kbl.v25.KblSpecialTerminalOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecTerminalRole;
import com.foursoft.harness.vec.v2x.VecTerminalSpecification;

public class TerminalRoleTransformer implements Transformer<KblSpecialTerminalOccurrence, VecTerminalRole> {

    @Override
    public TransformationResult<VecTerminalRole> transform(final TransformationContext context,
                                                           final KblSpecialTerminalOccurrence source) {
        final VecTerminalRole destination = new VecTerminalRole();
        destination.setIdentification(source.getId());

        return TransformationResult.from(destination)
                .withLinker(Query.of(source::getPart), VecTerminalSpecification.class,
                            VecTerminalRole::setTerminalSpecification)
                .build();
    }
}
