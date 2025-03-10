package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCompositionSpecification;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecGeneralTechnicalPartSpecification;
import com.foursoft.harness.vec.v2x.VecTopologySpecification;

import static com.foursoft.harness.kbl2vec.transform.Fragments.commonDocumentAttributes;

public class HarnessDocumentVersionTransformer implements Transformer<KblHarness, VecDocumentVersion> {

    @Override
    public TransformationResult<VecDocumentVersion> transform(final TransformationContext context,
                                                              final KblHarness source) {
        final VecDocumentVersion documentVersion = new VecDocumentVersion();
        //TODO: Enums/Consts for OpenEnums.
        documentVersion.setDocumentType("HarnessDescription");

        return TransformationResult.from(documentVersion)
                .withFragment(commonDocumentAttributes(source, context))
                .downstreamTransformation(KblPart.class, VecGeneralTechnicalPartSpecification.class, Query.of(source),
                                          documentVersion::getSpecifications)
                .downstreamTransformation(KblHarness.class, VecTopologySpecification.class, Query.of(source),
                                          documentVersion::getSpecifications)
                .downstreamTransformation(KblHarness.class, VecCompositionSpecification.class, Query.of(source),
                                          documentVersion::getSpecifications)
                .build();
    }
}
