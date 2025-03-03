package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecUnit;

public class NumericalValueTransformer implements Transformer<KblNumericalValue, VecNumericalValue> {

    @Override
    public TransformationResult<VecNumericalValue> transform(final TransformationContext context,
                                                             final KblNumericalValue source) {
        context.getLogger().debug("Transforming {} to VecNumericalValue.", source);
        final VecNumericalValue result = new VecNumericalValue();
        result.setValueComponent(source.getValueComponent());

        return TransformationResult.from(result)
                .withLinker(Query.of(source.getUnitComponent()), VecUnit.class, result::setUnitComponent)
                .build();
    }
}
