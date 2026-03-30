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

import com.foursoft.harness.kbl.v25.KblAliasIdentification;
import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl.v25.KblNode;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecAliasIdentification;
import com.foursoft.harness.vec.v2x.VecCartesianPoint3D;
import com.foursoft.harness.vec.v2x.VecGeometryNode3D;
import com.foursoft.harness.vec.v2x.VecTopologyNode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GeometryNode3DTransformerTest {

    @Test
    void should_transformGeometryNode3D() {
        // Given
        final GeometryNode3DTransformer transformer = new GeometryNode3DTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblNode source = new KblNode();
        source.setId("TestId");

        final KblCartesianPoint cartesianPoint = new KblCartesianPoint();
        source.setCartesianPoint(cartesianPoint);

        final VecCartesianPoint3D vecCartesianPoint3D = new VecCartesianPoint3D();
        orchestrator.addMockMapping(cartesianPoint, vecCartesianPoint3D);

        final VecTopologyNode vecTopologyNode = new VecTopologyNode();
        orchestrator.addMockMapping(source, vecTopologyNode);

        final KblAliasIdentification aliasIdentification = new KblAliasIdentification();
        source.getAliasIds().add(aliasIdentification);

        final VecAliasIdentification vecAliasIdentification = new VecAliasIdentification();
        orchestrator.addMockMapping(aliasIdentification, vecAliasIdentification);

        // When
        final VecGeometryNode3D result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(vecTopologyNode, VecGeometryNode3D::getReferenceNode)
                .returns("TestId", VecGeometryNode3D::getIdentification)
                .returns(vecCartesianPoint3D, VecGeometryNode3D::getCartesianPoint)
                .satisfies(v -> assertThat(v.getAliasIds()).containsExactly(vecAliasIdentification));
    }
}
