package com.foursoft.harness.kbl2vec.transform.components.wires.multi_cores;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecWireElement;
import com.foursoft.harness.vec.v2x.VecWireElementReference;
import com.foursoft.harness.vec.v2x.VecWireEnd;
import com.foursoft.harness.vec.v2x.VecWireLength;

import java.util.List;

public class WireElementReferenceTransformer implements Transformer<KblCoreOccurrence, VecWireElementReference> {

    @Override
    public TransformationResult<VecWireElementReference> transform(final TransformationContext context,
                                                                   final KblCoreOccurrence source) {
        final VecWireElementReference destination = new VecWireElementReference();
        destination.setIdentification(source.getWireNumber());

        return TransformationResult.from(destination)
                .withDownstream(KblWireLength.class, VecWireLength.class, source::getLengthInformations,
                                VecWireElementReference::getWireLengths)
                .withDownstream(KblExtremity.class, VecWireEnd.class, () -> getExtremities(source),
                                VecWireElementReference::getWireEnds)
                .withLinker(Query.of(source::getPart), VecWireElement.class,
                            VecWireElementReference::setReferencedWireElement)
                .build();
    }

    private List<KblExtremity> getExtremities(final KblCoreOccurrence source) {
        return source.getRefConnection().stream()
                .flatMap(v -> v.getExtremities().stream())
                .toList();
    }
}
