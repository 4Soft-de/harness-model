package com.foursoft.harness.kbl2vec.transform.topology.geometry.d3;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecBuildingBlockSpecification3D;
import com.foursoft.harness.vec.v2x.VecCartesianPoint3D;
import com.foursoft.harness.vec.v2x.VecGeometryNode3D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment3D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BuildingBlockSpecification3DTransformerTest {

    @Test
    void should_transformBuildingBlockSpecification3D() {
        // Given
        final BuildingBlockSpecification3DTransformer transformer = new BuildingBlockSpecification3DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblHarness source = new KblHarness();

        final KBLContainer kblContainer = new KBLContainer();
        source.setParentKBLContainer(kblContainer);

        final KblCartesianPoint cartesianPoint = new KblCartesianPoint();
        cartesianPoint.getCoordinates().add(1.0);
        cartesianPoint.getCoordinates().add(2.0);
        cartesianPoint.getCoordinates().add(3.0);
        kblContainer.getCartesianPoints().add(cartesianPoint);

        final VecCartesianPoint3D vecCartesianPoint3D = new VecCartesianPoint3D();
        orchestrator.addMockMapping(cartesianPoint, vecCartesianPoint3D);

        final KblNode node = new KblNode();
        kblContainer.getNodes().add(node);

        final VecGeometryNode3D vecGeometryNode3D = new VecGeometryNode3D();
        orchestrator.addMockMapping(node, vecGeometryNode3D);

        final KblSegment segment = new KblSegment();
        kblContainer.getSegments().add(segment);

        final VecGeometrySegment3D vecGeometrySegment3D = new VecGeometrySegment3D();
        orchestrator.addMockMapping(segment, vecGeometrySegment3D);

        // When
        final VecBuildingBlockSpecification3D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getGeometryNodes()).containsExactly(vecGeometryNode3D))
                .satisfies(v -> assertThat(v.getGeometrySegments()).containsExactly(vecGeometrySegment3D))
                .satisfies(v -> assertThat(v.getCartesianPoints()).containsExactly(vecCartesianPoint3D));
    }
}
