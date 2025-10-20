package com.foursoft.harness.kbl2vec.transform.geometry.d2;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecBuildingBlockPositioning2D;
import com.foursoft.harness.vec.v2x.VecHarnessDrawingSpecification2D;

public class HarnessDrawingSpecification2DTransformer
        implements Transformer<KblHarness, VecHarnessDrawingSpecification2D> {

    @Override
    public TransformationResult<VecHarnessDrawingSpecification2D> transform(final TransformationContext context,
                                                                            final KblHarness source) {
        final VecHarnessDrawingSpecification2D destination = new VecHarnessDrawingSpecification2D();

        return TransformationResult.from(destination)
                .withDownstream(KblHarness.class, VecBuildingBlockPositioning2D.class, Query.of(source),
                                VecHarnessDrawingSpecification2D::getBuildingBlockPositionings)
                .build();
    }
}
