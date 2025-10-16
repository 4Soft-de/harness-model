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
package com.foursoft.harness.kbl2vec.transform.geometry.d3;

import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl2vec.convert.DoublesToCartesianVector3DConverter;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.kbl2vec.transform.geometry.GeometryDimensionDetector;
import com.foursoft.harness.vec.v2x.VecGeometryNode3D;
import com.foursoft.harness.vec.v2x.VecGeometrySegment3D;

public class GeometrySegment3DTransformer implements Transformer<KblSegment, VecGeometrySegment3D> {

    @Override
    public TransformationResult<VecGeometrySegment3D> transform(final TransformationContext context,
                                                                final KblSegment source) {
        final VecGeometrySegment3D destination = new VecGeometrySegment3D();
        final int DIMENSIONS = 3;

        if (!GeometryDimensionDetector.hasDimensions(source.getStartVectors(), DIMENSIONS)) {
            context.getLogger().warn(
                    "Wrong number of coordinates provided for the transformation of start vectors. Expected {} but " +
                            "found {}.",
                    DIMENSIONS,
                    source.getStartVectors().size());
        }

        if (!GeometryDimensionDetector.hasDimensions(source.getEndVectors(), DIMENSIONS)) {
            context.getLogger().warn(
                    "Wrong number of coordinates provided for the transformation of end vectors. Expected {} but " +
                            "found {}.",
                    DIMENSIONS,
                    source.getEndVectors().size());
        }

        final DoublesToCartesianVector3DConverter converter =
                context.getConverterRegistry().getDoublesToCartesianVector3DConverter();
        converter.convert(source.getStartVectors()).ifPresent(destination::setStartVector);
        converter.convert(source.getEndVectors()).ifPresent(destination::setEndVector);

        return TransformationResult.from(destination)
                .withLinker(Query.of(source.getStartNode()), VecGeometryNode3D.class,
                            VecGeometrySegment3D::setStartNode)
                .withLinker(Query.of(source.getEndNode()), VecGeometryNode3D.class, VecGeometrySegment3D::setEndNode)
                .build();
    }
}
