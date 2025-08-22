package com.foursoft.harness.kbl2vec.transform.components;

import com.foursoft.harness.kbl.v25.KblCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblSlotOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCavityReference;
import com.foursoft.harness.vec.v2x.VecSlot;
import com.foursoft.harness.vec.v2x.VecSlotReference;

public class SlotReferenceTransformer implements Transformer<KblSlotOccurrence, VecSlotReference> {
    @Override
    public TransformationResult<VecSlotReference> transform(final TransformationContext context,
                                                            final KblSlotOccurrence source) {
        final VecSlotReference dest = new VecSlotReference();
        dest.setIdentification(source.getPart().getId());

        return TransformationResult
                .from(dest)
                .withDownstream(KblCavityOccurrence.class, VecCavityReference.class, source::getCavities,
                                VecSlotReference::getCavityReferences)
                .withLinker(Query.of(source::getPart), VecSlot.class, VecSlotReference::setReferencedSlot)
                .build();
    }
}
