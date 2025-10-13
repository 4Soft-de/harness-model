package com.foursoft.harness.kbl2vec.transform.core;

import com.foursoft.harness.kbl.v25.KblInstructionClassification;
import com.foursoft.harness.kbl.v25.KblProcessingInstruction;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCustomProperty;
import com.foursoft.harness.vec.v2x.VecSimpleValueProperty;

public class ProcessingInformationCustomPropertyTransformer
        implements Transformer<KblProcessingInstruction, VecCustomProperty> {

    @Override
    public TransformationResult<VecCustomProperty> transform(final TransformationContext context,
                                                             final KblProcessingInstruction source) {
        if (source.getClassification() == null ||
                source.getClassification() == KblInstructionClassification.CUSTOM_PROPERTY) {
            final VecSimpleValueProperty destination = new VecSimpleValueProperty();
            destination.setPropertyType(source.getInstructionType());
            destination.setPropertyType(source.getInstructionValue());
            return TransformationResult.of(destination);
        }
        return TransformationResult.noResult();
    }
}
