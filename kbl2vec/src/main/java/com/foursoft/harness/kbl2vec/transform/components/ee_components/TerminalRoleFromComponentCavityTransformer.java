package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponentCavityOccurrence;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecTerminalRole;

public class TerminalRoleFromComponentCavityTransformer implements Transformer<KblComponentCavityOccurrence, VecTerminalRole> {

    @Override
    public TransformationResult<VecTerminalRole> transform(final TransformationContext context,
                                                           final KblComponentCavityOccurrence source) {
        final VecTerminalRole destination = new VecTerminalRole();
        return TransformationResult.of(destination);
    }
}
