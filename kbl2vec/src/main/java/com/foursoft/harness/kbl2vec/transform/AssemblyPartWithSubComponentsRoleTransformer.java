package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.ConnectionOrOccurrence;
import com.foursoft.harness.kbl.v25.KblAssemblyPartOccurrence;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecPartWithSubComponentsRole;

public class AssemblyPartWithSubComponentsRoleTransformer
        implements Transformer<ConnectionOrOccurrence, VecPartWithSubComponentsRole> {
    @Override public TransformationResult<VecPartWithSubComponentsRole> transform(final TransformationContext context,
                                                                                  final ConnectionOrOccurrence source) {
        if (source instanceof final KblAssemblyPartOccurrence assemblyPartOccurrence) {
            final VecPartWithSubComponentsRole role = new VecPartWithSubComponentsRole();
            role.setIdentification(assemblyPartOccurrence.getId());

            return TransformationResult.from(role)
                    .build();
        }
        return TransformationResult.noResult();
    }
}
