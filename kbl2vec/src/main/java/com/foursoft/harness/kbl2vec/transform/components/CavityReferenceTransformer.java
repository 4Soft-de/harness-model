package com.foursoft.harness.kbl2vec.transform.components;

import com.foursoft.harness.kbl.v25.KblCavityOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCavity;
import com.foursoft.harness.vec.v2x.VecCavityReference;

public class CavityReferenceTransformer implements Transformer<KblCavityOccurrence, VecCavityReference> {

    @Override
    public TransformationResult<VecCavityReference> transform(final TransformationContext context,
                                                              final KblCavityOccurrence source) {
        final VecCavityReference dest = new VecCavityReference();
        dest.setIdentification(source.getPart().getCavityNumber());

        return TransformationResult.from(dest)
                .withLinker(Query.of(source::getPart), VecCavity.class, VecCavityReference::setReferencedCavity)
                .build();
    }
}
