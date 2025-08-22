package com.foursoft.harness.kbl2vec.transform.components;

import com.foursoft.harness.kbl.v25.KblCavity;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCavity;

public class CavityTransformer implements Transformer<KblCavity, VecCavity> {
    @Override
    public TransformationResult<VecCavity> transform(final TransformationContext context, final KblCavity source) {
        final VecCavity dest = new VecCavity();
        dest.setCavityNumber(source.getCavityNumber());

        return TransformationResult.of(dest);
    }
}
