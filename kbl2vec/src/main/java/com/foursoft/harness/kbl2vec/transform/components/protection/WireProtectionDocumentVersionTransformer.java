package com.foursoft.harness.kbl2vec.transform.components.protection;

import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl.v25.KblWireProtection;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecWireProtectionSpecification;

import static com.foursoft.harness.kbl2vec.transform.components.common.Fragments.commonComponentInformation;

public class WireProtectionDocumentVersionTransformer implements Transformer<KblPart, VecDocumentVersion> {

    @Override
    public TransformationResult<VecDocumentVersion> transform(final TransformationContext context,
                                                              final KblPart kblPart) {
        if (kblPart instanceof final KblWireProtection source) {
            final VecDocumentVersion destination = new VecDocumentVersion();

            return TransformationResult
                    .from(destination)
                    .withFragment(commonComponentInformation(source, context))
                    .withDownstream(KblWireProtection.class, VecWireProtectionSpecification.class, Query.of(source),
                                    VecDocumentVersion::getSpecifications)
                    .build();
        }
        return TransformationResult.noResult();
    }
}
