package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KblAliasIdentification;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecAliasIdentification;

public class AliasIdentificationTransformer implements Transformer<KblAliasIdentification, VecAliasIdentification> {
    @Override
    public TransformationResult<VecAliasIdentification> transform(final TransformationContext context,
                                                                  final KblAliasIdentification source) {
        final VecAliasIdentification aliasIdentification = new VecAliasIdentification();
        aliasIdentification.setIdentificationValue(source.getAliasId());
        aliasIdentification.setScope(source.getScope());
        aliasIdentification.setType(source.getDescription());
        //TODO: LocalizedDescription missing
        return TransformationResult.of(aliasIdentification);
    }
}
