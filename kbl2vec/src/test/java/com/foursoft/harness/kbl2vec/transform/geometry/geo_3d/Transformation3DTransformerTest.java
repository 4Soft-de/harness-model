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

import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl.v25.KblTransformation;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCartesianPoint3D;
import com.foursoft.harness.vec.v2x.VecTransformation3D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Transformation3DTransformerTest {

    @Test
    void should_transformTransformation3D() {
        final Transformation3DTransformer transformer = new Transformation3DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblTransformation source = new KblTransformation();

        final KblCartesianPoint cartesianPoint = new KblCartesianPoint();
        source.setCartesianPoint(cartesianPoint);

        source.getUS().add(1.0);
        source.getUS().add(1.0);
        source.getUS().add(1.0);
        source.getVS().add(1.0);
        source.getVS().add(1.0);
        source.getVS().add(1.0);

        final VecCartesianPoint3D vecCartesianPoint3D = new VecCartesianPoint3D();
        orchestrator.addMockMapping(cartesianPoint, vecCartesianPoint3D);

        final VecTransformation3D result = orchestrator.transform(transformer, source);

        assertThat(result).isNotNull()
                .returns(vecCartesianPoint3D, VecTransformation3D::getOrigin)
                .returns(1.0, VecTransformation3D::getA11)
                .returns(1.0, VecTransformation3D::getA12)
                .returns(0.0, VecTransformation3D::getA13)
                .returns(1.0, VecTransformation3D::getA21)
                .returns(1.0, VecTransformation3D::getA22)
                .returns(0.0, VecTransformation3D::getA23)
                .returns(1.0, VecTransformation3D::getA31)
                .returns(1.0, VecTransformation3D::getA32)
                .returns(0.0, VecTransformation3D::getA33);
    }
}
