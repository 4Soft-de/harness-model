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
package com.foursoft.harness.kbl2vec.transform.geometry.d2;

import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblNode;
import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.kbl2vec.transform.geometry.GeometryDimensionDetector;
import com.foursoft.harness.vec.v2x.VecBuildingBlockSpecification2D;
import com.foursoft.harness.vec.v2x.VecCartesianPoint2D;
import com.foursoft.harness.vec.v2x.VecGeometryNode2D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment2D;

public class BuildingBlockSpecification2DTransformer
        implements Transformer<KblHarness, VecBuildingBlockSpecification2D> {

    @Override
    public TransformationResult<VecBuildingBlockSpecification2D> transform(final TransformationContext context,
                                                                           final KblHarness source) {
        final VecBuildingBlockSpecification2D destination = new VecBuildingBlockSpecification2D();
        final int DIMENSIONS = 2;

        if (!GeometryDimensionDetector.hasDimensions(source.getParentKBLContainer().getCartesianPoints(), DIMENSIONS)) {
            return TransformationResult.noResult();
        }
        context.getLogger().info("Detected 2D data. Creating VEC 2D specifications.");

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
