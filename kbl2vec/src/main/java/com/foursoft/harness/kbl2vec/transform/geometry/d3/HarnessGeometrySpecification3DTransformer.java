package com.foursoft.harness.kbl2vec.transform.geometry.d3;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecBuildingBlockPositioning3D;
import com.foursoft.harness.vec.v2x.VecHarnessGeometrySpecification3D;

public class HarnessGeometrySpecification3DTransformer
        implements Transformer<KblHarness, VecHarnessGeometrySpecification3D> {

    @Override
    public TransformationResult<VecHarnessGeometrySpecification3D> transform(final TransformationContext context,
                                                                             final KblHarness source) {
        final VecHarnessGeometrySpecification3D destination = new VecHarnessGeometrySpecification3D();
        destination.setType("Dmu");

        return TransformationResult.from(destination)
                .withDownstream(KblHarness.class, VecBuildingBlockPositioning3D.class, Query.of(source),
                                VecHarnessGeometrySpecification3D::getBuildingBlockPositionings)
                .build();
    }
}
