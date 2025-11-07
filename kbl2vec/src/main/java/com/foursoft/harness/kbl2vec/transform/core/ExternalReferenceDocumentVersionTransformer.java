package com.foursoft.harness.kbl2vec.transform.core;

import com.foursoft.harness.kbl.v25.KblExternalReference;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;

public class ExternalReferenceDocumentVersionTransformer
        implements Transformer<KblExternalReference, VecDocumentVersion> {

    @Override
    public TransformationResult<VecDocumentVersion> transform(final TransformationContext context,
                                                              final KblExternalReference source) {
        final VecDocumentVersion destination = new VecDocumentVersion();
        destination.setDocumentType(source.getDocumentType());
        destination.setDocumentNumber(source.getDocumentNumber());
        destination.setFileName(source.getFileName());
        destination.setDocumentVersion(source.getChangeLevel());
        destination.setCreatingSystem(source.getCreatingSystem());
        destination.setLocation(source.getLocation());
        destination.setDataFormat(source.getDataFormat());
        return TransformationResult.of(destination);
    }
}
