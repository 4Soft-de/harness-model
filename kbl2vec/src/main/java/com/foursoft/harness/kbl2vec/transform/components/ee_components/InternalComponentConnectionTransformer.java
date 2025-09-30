package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponentBoxConnection;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecInternalComponentConnection;
import com.foursoft.harness.vec.v2x.VecPinComponent;

public class InternalComponentConnectionTransformer
        implements Transformer<KblComponentBoxConnection, VecInternalComponentConnection> {

    @Override
    public TransformationResult<VecInternalComponentConnection> transform(final TransformationContext context,
                                                                          final KblComponentBoxConnection source) {
        final VecInternalComponentConnection destination = new VecInternalComponentConnection();

        return TransformationResult.from(destination)
                .withLinker(Query.fromLists(source.getCavities()), VecPinComponent.class,
                            VecInternalComponentConnection::getPins)
                .withLinker(Query.fromLists(source.getComponentCavities()), VecPinComponent.class,
                            VecInternalComponentConnection::getPins)
                .build();
    }
}
