package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblCavityOccurrence;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecTerminalRole;

public class TerminalRoleFromCavityTransformer implements Transformer<KblCavityOccurrence, VecTerminalRole> {

    @Override
    public TransformationResult<VecTerminalRole> transform(final TransformationContext context,
                                                           final KblCavityOccurrence source) {
        final VecTerminalRole destination = new VecTerminalRole();
        return TransformationResult.of(destination);
    }
}
