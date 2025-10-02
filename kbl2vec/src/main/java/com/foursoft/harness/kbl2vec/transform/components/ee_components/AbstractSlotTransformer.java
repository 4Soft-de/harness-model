package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblAbstractSlot;
import com.foursoft.harness.kbl.v25.KblCavity;
import com.foursoft.harness.kbl.v25.KblSlot;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCavity;
import com.foursoft.harness.vec.v2x.VecSlot;

public class AbstractSlotTransformer implements Transformer<KblAbstractSlot, VecSlot> {

    @Override
    public TransformationResult<VecSlot> transform(final TransformationContext context,
                                                   final KblAbstractSlot kblAbstractSlot) {

        if (kblAbstractSlot instanceof final KblSlot source) {
            final VecSlot destination = new VecSlot();

            return TransformationResult.from(destination)
                    .withDownstream(KblCavity.class, VecCavity.class, Query.fromLists(source.getCavities()),
                                    VecSlot::getCavities)
                    .build();
        }
        return TransformationResult.noResult();
    }
}
