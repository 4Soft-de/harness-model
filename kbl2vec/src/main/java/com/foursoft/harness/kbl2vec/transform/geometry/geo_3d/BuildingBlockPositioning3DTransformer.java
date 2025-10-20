package com.foursoft.harness.kbl2vec.transform.geometry.geo_3d;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecBuildingBlockPositioning3D;
import com.foursoft.harness.vec.v2x.VecBuildingBlockSpecification3D;

public class BuildingBlockPositioning3DTransformer implements Transformer<KblHarness, VecBuildingBlockPositioning3D> {

    @Override
    public TransformationResult<VecBuildingBlockPositioning3D> transform(final TransformationContext context,
                                                                         final KblHarness source) {
        final VecBuildingBlockPositioning3D destination = new VecBuildingBlockPositioning3D();

        return TransformationResult.from(destination)
                .withLinker(Query.of(source), VecBuildingBlockSpecification3D.class,
                            VecBuildingBlockPositioning3D::setReferenced3DBuildingBlock)
                .build();
    }
}
