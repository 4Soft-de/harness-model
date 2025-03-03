package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KblMaterial;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecMaterial;

public class MaterialTransformer implements Transformer<KblMaterial, VecMaterial> {

    @Override
    public TransformationResult<VecMaterial> transform(final TransformationContext context,
                                                       final KblMaterial source) {
        context.getLogger().debug("Transforming {} to KblMaterial.", source);
        final VecMaterial result = new VecMaterial();
        result.setReferenceSystem(source.getMaterialReferenceSystem());
        result.setKey(source.getMaterialKey());

        return TransformationResult.of(result);
    }
}
