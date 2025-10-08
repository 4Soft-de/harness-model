package com.foursoft.harness.kbl2vec.transform.topology.geometry;

import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblNode;
import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecBuildingBlockSpecification3D;
import com.foursoft.harness.vec.v2x.VecCartesianPoint3D;
import com.foursoft.harness.vec.v2x.VecGeometryNode3D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment3D;

public class BuildingBlockSpecification3DTransformer
        implements Transformer<KblHarness, VecBuildingBlockSpecification3D> {

    @Override
    public TransformationResult<VecBuildingBlockSpecification3D> transform(final TransformationContext context,
                                                                           final KblHarness source) {
        final int dimensions = 3;
        final boolean isNot3D = source.getParentKBLContainer().getCartesianPoints().stream()
                .anyMatch(p -> p.getCoordinates().size() != dimensions);

        if (source.getParentKBLContainer().getCartesianPoints().isEmpty() || isNot3D) {
            return TransformationResult.noResult();
        }

        final VecBuildingBlockSpecification3D destination = new VecBuildingBlockSpecification3D();

        return TransformationResult.from(destination)
                .withDownstream(KblNode.class, VecGeometryNode3D.class,
                                Query.fromLists(source.getParentKBLContainer().getNodes()),
                                VecBuildingBlockSpecification3D::getGeometryNodes)
                .withDownstream(KblCartesianPoint.class, VecCartesianPoint3D.class,
                                Query.fromLists(source.getParentKBLContainer().getCartesianPoints()),
                                VecBuildingBlockSpecification3D::getCartesianPoints)
                .withDownstream(KblSegment.class, VecGeometrySegment3D.class,
                                Query.fromLists(source.getParentKBLContainer().getSegments()),
                                VecBuildingBlockSpecification3D::getGeometrySegments)
                .build();
    }
}
