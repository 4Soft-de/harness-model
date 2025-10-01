package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponentCavityOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecPinComponent;
import com.foursoft.harness.vec.v2x.VecPinComponentReference;
import com.foursoft.harness.vec.v2x.VecTerminalRole;

public class PinComponentFromComponentCavityReferenceTransformer
        implements Transformer<KblComponentCavityOccurrence, VecPinComponentReference> {

    @Override
    public TransformationResult<VecPinComponentReference> transform(final TransformationContext context,
                                                                    final KblComponentCavityOccurrence source) {
        final VecPinComponentReference destination = new VecPinComponentReference();

        return TransformationResult.from(destination)
                .withDownstream(KblComponentCavityOccurrence.class, VecTerminalRole.class, Query.of(source),
                                VecPinComponentReference::setTerminalRole)
                .withLinker(Query.of(source::getPart), VecPinComponent.class, VecPinComponentReference::setPinComponent)
                .build();
    }
}
