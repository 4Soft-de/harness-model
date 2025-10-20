package com.foursoft.harness.kbl2vec.transform.geometry.geo_2d;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecBuildingBlockPositioning2D;
import com.foursoft.harness.vec.v2x.VecBuildingBlockSpecification2D;

public class BuildingBlockPositioning2DTransformer implements Transformer<KblHarness, VecBuildingBlockPositioning2D> {

    @Override
    public TransformationResult<VecBuildingBlockPositioning2D> transform(final TransformationContext context,
                                                                         final KblHarness source) {
        final VecBuildingBlockPositioning2D destination = new VecBuildingBlockPositioning2D();

        return TransformationResult.from(destination)
                .withLinker(Query.of(source), VecBuildingBlockSpecification2D.class,
                            VecBuildingBlockPositioning2D::setReferenced2DBuildingBlock)
                .build();
    }
}
