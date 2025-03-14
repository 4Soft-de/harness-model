package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KblAssemblyPart;
import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPartStructureSpecification;

import static com.foursoft.harness.kbl2vec.transform.Fragments.commonSpecificationAttributes;
import static com.foursoft.harness.kbl2vec.transform.Queries.partOccurrences;

public class AssemblyPartStructureSpecification implements
        Transformer<KblPart, VecPartStructureSpecification> {
    @Override
    public TransformationResult<VecPartStructureSpecification> transform(final TransformationContext context,
                                                                         final KblPart source) {
        if (source instanceof final KblAssemblyPart assemblyPart) {
            final VecPartStructureSpecification element = new VecPartStructureSpecification();

            return TransformationResult.from(element)
                    .withLinker(partOccurrences(assemblyPart.getConnectionOrOccurrences()), VecPartOccurrence.class,
                                VecPartStructureSpecification::getInBillOfMaterial)
                    .withFragment(commonSpecificationAttributes(source))
                    .build();

        }
        return TransformationResult.noResult();
    }
}
