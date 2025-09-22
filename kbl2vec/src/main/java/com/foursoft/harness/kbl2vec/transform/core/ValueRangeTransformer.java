package com.foursoft.harness.kbl2vec.transform.core;

import com.foursoft.harness.kbl.v25.KblValueRange;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecUnit;
import com.foursoft.harness.vec.v2x.VecValueRange;

public class ValueRangeTransformer implements Transformer<KblValueRange, VecValueRange> {

    @Override
    public TransformationResult<VecValueRange> transform(final TransformationContext context,
                                                         final KblValueRange kblValueRange) {
        final VecValueRange result = new VecValueRange();
        result.setMinimum(kblValueRange.getMinimum());
        result.setMaximum(kblValueRange.getMaximum());

        return TransformationResult
                .from(result)
                .withLinker(Query.of(kblValueRange.getUnitComponent()), VecUnit.class, VecValueRange::setUnitComponent)
                .build();
    }
}
