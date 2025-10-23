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
