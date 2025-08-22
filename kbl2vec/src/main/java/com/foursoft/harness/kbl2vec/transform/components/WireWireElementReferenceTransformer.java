package com.foursoft.harness.kbl2vec.transform.components;

import com.foursoft.harness.kbl.v25.KblGeneralWireOccurrence;
import com.foursoft.harness.kbl.v25.KblSpecialWireOccurrence;
import com.foursoft.harness.kbl.v25.KblWireLength;
import com.foursoft.harness.kbl.v25.KblWireOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecWireElement;
import com.foursoft.harness.vec.v2x.VecWireElementReference;
import com.foursoft.harness.vec.v2x.VecWireLength;

public class WireWireElementReferenceTransformer
        implements Transformer<KblGeneralWireOccurrence, VecWireElementReference> {

    @Override public TransformationResult<VecWireElementReference> transform(final TransformationContext context,
                                                                             final KblGeneralWireOccurrence source) {
        final VecWireElementReference dest = new VecWireElementReference();
        if (source instanceof final KblWireOccurrence wireOccurrence) {
            dest.setIdentification(wireOccurrence.getWireNumber());
        } else if (source instanceof final KblSpecialWireOccurrence specialWireOccurrence) {
            dest.setIdentification(specialWireOccurrence.getSpecialWireId());
        } else {
            context.getLogger().warn("'{}' has a unsupported wire class type", source);
        }
        return TransformationResult.from(dest)
                .withDownstream(KblWireLength.class, VecWireLength.class, source::getLengthInformations,
                                VecWireElementReference::getWireLengths)
                .withLinker(Query.of(source::getPart), VecWireElement.class,
                            VecWireElementReference::setReferencedWireElement)
                .build();
    }
}
