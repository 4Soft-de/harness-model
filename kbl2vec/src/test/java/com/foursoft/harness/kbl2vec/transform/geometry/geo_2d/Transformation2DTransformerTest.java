package com.foursoft.harness.kbl2vec.transform.geometry.geo_2d;

import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl.v25.KblTransformation;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCartesianPoint2D;
import com.foursoft.harness.vec.v2x.VecTransformation2D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Transformation2DTransformerTest {

    @Test
    void should_transformTransformation2D() {
        final Transformation2DTransformer transformer = new Transformation2DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblTransformation source = new KblTransformation();

        final KblCartesianPoint cartesianPoint = new KblCartesianPoint();
        source.setCartesianPoint(cartesianPoint);

        source.getUS().add(1.0);
        source.getUS().add(2.0);
        source.getVS().add(3.0);
        source.getVS().add(4.0);

        final VecCartesianPoint2D vecCartesianPoint2D = new VecCartesianPoint2D();
        orchestrator.addMockMapping(cartesianPoint, vecCartesianPoint2D);

        final VecTransformation2D result = orchestrator.transform(transformer, source);

        assertThat(result).isNotNull()
                .returns(vecCartesianPoint2D, VecTransformation2D::getOrigin)
                .returns(1.0, VecTransformation2D::getA11)
                .returns(2.0, VecTransformation2D::getA12)
                .returns(3.0, VecTransformation2D::getA21)
                .returns(4.0, VecTransformation2D::getA22);
    }
}
