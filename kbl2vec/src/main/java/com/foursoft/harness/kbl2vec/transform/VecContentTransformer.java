package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KBLContainer;
import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl.v25.KblUnit;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecContent;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecUnit;

import static com.foursoft.harness.kbl2vec.transform.Queries.allParts;

public class VecContentTransformer implements Transformer<KBLContainer, VecContent> {
    @Override
    public TransformationResult<VecContent> transform(final TransformationContext context, final KBLContainer source) {
        final VecContent resultElement = new VecContent();

        resultElement.setVecVersion("2.1.0");
        resultElement.setGeneratingSystemName("4Soft KBL2VEC Converter");
        resultElement.setGeneratingSystemVersion("0.0.1");

        return TransformationResult.from(resultElement)
//                .downstreamTransformation(KblPart.class, VecCopyrightInformation.class, allParts(source),
//                                          resultElement::getCopyrightInformations)
                .downstreamTransformation(KblUnit.class, VecUnit.class, source::getUnits, resultElement::getUnits)
                .downstreamTransformation(KblPart.class, VecPartVersion.class, allParts(source),
                                          resultElement::getPartVersions)
                .downstreamTransformation(KblPart.class, VecDocumentVersion.class, source::getParts,
                                          resultElement::getDocumentVersions)
                .build();
    }

}
