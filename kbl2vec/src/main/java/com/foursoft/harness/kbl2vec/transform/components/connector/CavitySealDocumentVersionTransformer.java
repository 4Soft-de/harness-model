package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.KblCavitySeal;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;

import static com.foursoft.harness.kbl2vec.transform.components.common.Fragments.commonComponentInformation;

public class CavitySealDocumentVersionTransformer implements Transformer<KblCavitySeal, VecDocumentVersion> {

    @Override
    public TransformationResult<VecDocumentVersion> transform(
            final TransformationContext context, final KblCavitySeal source
    ) {
        final VecDocumentVersion documentVersion = new VecDocumentVersion();

        final TransformationResult.Builder<VecDocumentVersion> builder = TransformationResult
                .from(documentVersion)
                .withFragment(commonComponentInformation(source, context));

        return builder.build();
    }
}
