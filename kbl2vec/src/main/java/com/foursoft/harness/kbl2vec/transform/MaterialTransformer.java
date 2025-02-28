package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KblMaterial;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecMaterial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaterialTransformer implements Transformer<KblMaterial, VecMaterial> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaterialTransformer.class);

    @Override
    public TransformationResult<VecMaterial> transform(final TransformationContext context,
                                                       final KblMaterial source) {
        LOGGER.debug("Transforming {} to KblMaterial.", source);
        final VecMaterial result = new VecMaterial();
        result.setReferenceSystem(source.getMaterialReferenceSystem());
        result.setKey(source.getMaterialKey());

        return TransformationResult.of(result);
    }
}
