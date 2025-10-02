package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponentCavity;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCavity;
import com.foursoft.harness.vec.v2x.VecPinComponent;
import com.foursoft.harness.vec.v2x.VecTerminalSpecification;

public class PinCompFromCompCavityTransformer implements Transformer<KblComponentCavity, VecPinComponent> {

    @Override
    public TransformationResult<VecPinComponent> transform(final TransformationContext context,
                                                           final KblComponentCavity source) {
        final VecPinComponent destination = new VecPinComponent();
        return TransformationResult.from(destination)
                .withLinker(Query.of(source), VecCavity.class, VecPinComponent::setReferencedCavity)
                .withLinker(Query.of(source), VecTerminalSpecification.class, VecPinComponent::setPinSpecification)
                .build();
    }
}
