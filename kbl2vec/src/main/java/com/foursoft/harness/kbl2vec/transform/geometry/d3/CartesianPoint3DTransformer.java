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

import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.kbl2vec.transform.geometry.GeometryDimensionDetector;
import com.foursoft.harness.vec.v2x.VecCartesianPoint3D;

import java.util.List;

import static com.foursoft.harness.kbl2vec.utils.ListUtils.getElementOrDefault;

public class CartesianPoint3DTransformer implements Transformer<KblCartesianPoint, VecCartesianPoint3D> {

    @Override
    public TransformationResult<VecCartesianPoint3D> transform(final TransformationContext context,
                                                               final KblCartesianPoint source) {
        final VecCartesianPoint3D destination = new VecCartesianPoint3D();
        final int DIMENSIONS = 3;

        final List<Double> coordinates = source.getCoordinates();
        if (source.getCoordinates().isEmpty()) {
            return TransformationResult.noResult();
        }

        if (!GeometryDimensionDetector.hasDimensions(source, DIMENSIONS)) {
            context.getLogger().warn(
                    "Wrong number of coordinates provided for the transformation. Expected {} but found {}.",
                    DIMENSIONS,
                    source.getCoordinates().size());
        }
        destination.setX(getElementOrDefault(coordinates, 0, 0.0));
        destination.setY(getElementOrDefault(coordinates, 1, 0.0));
        destination.setZ(getElementOrDefault(coordinates, 2, 0.0));

        return TransformationResult.of(destination);
    }
}
