package com.foursoft.harness.kbl2vec.transform.topology.geometry.d2;

import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblNode;
import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecBuildingBlockSpecification2D;
import com.foursoft.harness.vec.v2x.VecCartesianPoint2D;
import com.foursoft.harness.vec.v2x.VecGeometryNode2D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment2D;

public class BuildingBlockSpecification2DTransformer
        implements Transformer<KblHarness, VecBuildingBlockSpecification2D> {

    @Override
    public TransformationResult<VecBuildingBlockSpecification2D> transform(final TransformationContext context,
                                                                           final KblHarness source) {
        final int dimensions = 2;
        final boolean isNot2D = source.getParentKBLContainer().getCartesianPoints().stream()
                .anyMatch(p -> p.getCoordinates().size() != dimensions);

        if (source.getParentKBLContainer().getCartesianPoints().isEmpty() || isNot2D) {
            return TransformationResult.noResult();
        }

        final VecBuildingBlockSpecification2D destination = new VecBuildingBlockSpecification2D();

        return TransformationResult.from(destination)
                .withDownstream(KblNode.class, VecGeometryNode2D.class,
                                Query.fromLists(source.getParentKBLContainer().getNodes()),
                                VecBuildingBlockSpecification2D::getGeometryNodes)
                .withDownstream(KblCartesianPoint.class, VecCartesianPoint2D.class,
                                Query.fromLists(source.getParentKBLContainer().getCartesianPoints()),
                                VecBuildingBlockSpecification2D::getCartesianPoints)
                .withDownstream(KblSegment.class, VecGeometrySegment2D.class,
                                Query.fromLists(source.getParentKBLContainer().getSegments()),
                                VecBuildingBlockSpecification2D::getGeometrySegments)
                .build();
    }
}
