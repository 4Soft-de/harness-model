package com.foursoft.harness.kbl2vec.transform.components.copack;

import com.foursoft.harness.kbl.v25.KblCoPackPart;
import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;

import static com.foursoft.harness.kbl2vec.transform.components.common.Fragments.commonComponentInformation;

public class CoPackDocumentVersionTransformer implements Transformer<KblPart, VecDocumentVersion> {

    @Override
    public TransformationResult<VecDocumentVersion> transform(final TransformationContext context, final KblPart part) {
        if (part instanceof final KblCoPackPart source) {
            final VecDocumentVersion destination = new VecDocumentVersion();

            return TransformationResult.from(destination)
                    .withFragment(commonComponentInformation(source, context))
                    .build();
        }
        return TransformationResult.noResult();
    }
}
