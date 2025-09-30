package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponentBox;
import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecEEComponentSpecification;

import static com.foursoft.harness.kbl2vec.transform.components.common.Fragments.commonComponentInformation;

public class EEComponentDocumentVersionTransformer implements Transformer<KblPart, VecDocumentVersion> {

    @Override
    public TransformationResult<VecDocumentVersion> transform(final TransformationContext context,
                                                              final KblPart kblPart) {
        if (kblPart instanceof final KblComponentBox source) {
            final VecDocumentVersion destination = new VecDocumentVersion();

            return TransformationResult.from(destination)
                    .withFragment(commonComponentInformation(source, context))
                    .withDownstream(KblComponentBox.class, VecEEComponentSpecification.class, Query.of(source),
                                    VecDocumentVersion::getSpecifications)
                    .build();
        }
        return TransformationResult.noResult();
    }
}
