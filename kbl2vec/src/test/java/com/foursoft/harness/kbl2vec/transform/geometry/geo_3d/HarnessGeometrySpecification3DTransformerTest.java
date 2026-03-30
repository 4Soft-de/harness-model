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

import com.foursoft.harness.kbl.v25.KBLContainer;
import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecBuildingBlockPositioning3D;
import com.foursoft.harness.vec.v2x.VecHarnessGeometrySpecification3D;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HarnessGeometrySpecification3DTransformerTest {

    @Test
    void should_transformHarnessGeometrySpecification3D() {
        // Given
        final HarnessGeometrySpecification3DTransformer transformer = new HarnessGeometrySpecification3DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblHarness source = new KblHarness();

        final KblCartesianPoint cartesianPoint = new KblCartesianPoint();
        cartesianPoint.getCoordinates().add(1.0);
        cartesianPoint.getCoordinates().add(2.0);
        cartesianPoint.getCoordinates().add(3.0);

        final KBLContainer kblContainer = new KBLContainer();
        source.setParentKBLContainer(kblContainer);
        source.getParentKBLContainer().getCartesianPoints().add(cartesianPoint);

        final VecBuildingBlockPositioning3D vecBuildingBlockPositioning3D = new VecBuildingBlockPositioning3D();
        orchestrator.addMockMapping(source, vecBuildingBlockPositioning3D);

        // When
        final VecHarnessGeometrySpecification3D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("Dmu", VecHarnessGeometrySpecification3D::getType)
                .satisfies(v -> assertThat(v.getBuildingBlockPositionings()).contains(vecBuildingBlockPositioning3D));
    }
}
