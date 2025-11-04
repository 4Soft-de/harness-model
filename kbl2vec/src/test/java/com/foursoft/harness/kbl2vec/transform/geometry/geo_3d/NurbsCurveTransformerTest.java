/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
package com.foursoft.harness.kbl2vec.transform.geometry.geo_3d;

import com.foursoft.harness.kbl.v25.KblBSplineCurve;
import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecNURBSControlPoint;
import com.foursoft.harness.vec.v2x.VecNURBSCurve;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NurbsCurveTransformerTest {

    @Test
    void should_transformNurbsCurve() {
        // Given
        final NurbsCurveTransformer transformer = new NurbsCurveTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblBSplineCurve source = new KblBSplineCurve();
        final BigInteger degree = BigInteger.valueOf(1);
        source.setDegree(degree);

        final KblCartesianPoint controlPoint1 = new KblCartesianPoint();
        final KblCartesianPoint controlPoint2 = new KblCartesianPoint();
        source.getControlPoints().add(controlPoint1);
        source.getControlPoints().add(controlPoint2);

        final VecNURBSControlPoint vecControlPoint1 = new VecNURBSControlPoint();
        final VecNURBSControlPoint vecControlPoint2 = new VecNURBSControlPoint();
        orchestrator.addMockMapping(controlPoint1, vecControlPoint1);
        orchestrator.addMockMapping(controlPoint2, vecControlPoint2);

        final List<Double> expectedKnots = List.of(0.0, 0.0, 1.0, 1.0);

        // When
        final VecNURBSCurve result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(degree, VecNURBSCurve::getDegree)
                .returns(expectedKnots, VecNURBSCurve::getKnots)
                .satisfies(v -> assertThat(v.getControlPoints()).containsExactly(vecControlPoint1, vecControlPoint2));
    }
}
