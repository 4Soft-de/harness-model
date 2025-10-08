package com.foursoft.harness.kbl2vec.transform.topology.geometry;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecBuildingBlockSpecification2D;
import com.foursoft.harness.vec.v2x.VecCartesianPoint2D;
import com.foursoft.harness.vec.v2x.VecGeometryNode2D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment2D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BuildingBlockSpecification2DTransformerTest {

    @Test
    void should_transformBuildingBlockSpecification2D() {
        // Given
        final BuildingBlockSpecification2DTransformer transformer = new BuildingBlockSpecification2DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblHarness source = new KblHarness();

        final KBLContainer kblContainer = new KBLContainer();
        source.setParentKBLContainer(kblContainer);

        final KblCartesianPoint cartesianPoint = new KblCartesianPoint();
        cartesianPoint.getCoordinates().add(1.0);
        cartesianPoint.getCoordinates().add(2.0);
        kblContainer.getCartesianPoints().add(cartesianPoint);

        final VecCartesianPoint2D vecCartesianPoint2D = new VecCartesianPoint2D();
        orchestrator.addMockMapping(cartesianPoint, vecCartesianPoint2D);

        final KblNode node = new KblNode();
        kblContainer.getNodes().add(node);

        final VecGeometryNode2D vecGeometryNode2D = new VecGeometryNode2D();
        orchestrator.addMockMapping(node, vecGeometryNode2D);

        final KblSegment segment = new KblSegment();
        kblContainer.getSegments().add(segment);

        final VecGeometrySegment2D vecGeometrySegment2D = new VecGeometrySegment2D();
        orchestrator.addMockMapping(segment, vecGeometrySegment2D);

        // When
        final VecBuildingBlockSpecification2D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getGeometryNodes()).containsExactly(vecGeometryNode2D))
                .satisfies(v -> assertThat(v.getGeometrySegments()).containsExactly(vecGeometrySegment2D))
                .satisfies(v -> assertThat(v.getCartesianPoints()).containsExactly(vecCartesianPoint2D));
    }
}
