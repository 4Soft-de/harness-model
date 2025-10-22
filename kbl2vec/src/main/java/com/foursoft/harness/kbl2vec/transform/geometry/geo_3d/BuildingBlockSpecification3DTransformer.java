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
import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblNode;
import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.kbl2vec.transform.geometry.GeometryDimensionDetector;
import com.foursoft.harness.vec.v2x.VecBuildingBlockSpecification3D;
import com.foursoft.harness.vec.v2x.VecCartesianPoint3D;
import com.foursoft.harness.vec.v2x.VecGeometryNode3D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment3D;

public class BuildingBlockSpecification3DTransformer
        implements Transformer<KblHarness, VecBuildingBlockSpecification3D> {

    @Override
    public TransformationResult<VecBuildingBlockSpecification3D> transform(final TransformationContext context,
                                                                           final KblHarness source) {
        final VecBuildingBlockSpecification3D destination = new VecBuildingBlockSpecification3D();

        if (!GeometryDimensionDetector.hasDimensions(source.getParentKBLContainer().getCartesianPoints(),
                                                     GeometryDimensionDetector.GEO_3D)) {
            return TransformationResult.noResult();
        }
        context.getLogger().info("Detected 3D data. Creating 3D building block specification.");

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
