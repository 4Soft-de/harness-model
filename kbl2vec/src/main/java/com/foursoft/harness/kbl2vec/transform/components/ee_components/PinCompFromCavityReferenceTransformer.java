package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblCavityOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecPinComponent;
import com.foursoft.harness.vec.v2x.VecPinComponentReference;
import com.foursoft.harness.vec.v2x.VecTerminalRole;

public class PinCompFromCavityReferenceTransformer
        implements Transformer<KblCavityOccurrence, VecPinComponentReference> {

    @Override
    public TransformationResult<VecPinComponentReference> transform(final TransformationContext context,
                                                                    final KblCavityOccurrence source) {
        final VecPinComponentReference destination = new VecPinComponentReference();

        return TransformationResult.from(destination)
                .withDownstream(KblCavityOccurrence.class, VecTerminalRole.class, Query.of(source),
                                VecPinComponentReference::setTerminalRole)
                .withLinker(Query.of(source::getPart), VecPinComponent.class, VecPinComponentReference::setPinComponent)
                .build();
    }
}
