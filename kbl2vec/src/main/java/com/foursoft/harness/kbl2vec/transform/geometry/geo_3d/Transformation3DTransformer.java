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

import com.foursoft.harness.kbl.v25.KblTransformation;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.kbl2vec.transform.geometry.GeometryDimensionDetector;
import com.foursoft.harness.vec.v2x.VecCartesianPoint3D;
import com.foursoft.harness.vec.v2x.VecTransformation3D;

import java.util.Arrays;
import java.util.List;

import static com.foursoft.harness.kbl2vec.utils.ListUtils.getElementOrDefault;

public class Transformation3DTransformer implements Transformer<KblTransformation, VecTransformation3D> {

    @Override
    public TransformationResult<VecTransformation3D> transform(final TransformationContext context,
                                                               final KblTransformation source) {
        final VecTransformation3D destination = new VecTransformation3D();

        final List<Double> u = source.getUS();
        final List<Double> v = source.getVS();

        if (!GeometryDimensionDetector.hasDimensions(u, GeometryDimensionDetector.GEO_3D)) {
            context.getLogger().warn(
                    "Unexpected format for U vector of KblTransformation (xmlId: {}). Expected 3 coordinates but " +
                            "found {}: {}.", source.getXmlId(), u.size(), u);
        }

        if (!GeometryDimensionDetector.hasDimensions(v, GeometryDimensionDetector.GEO_3D)) {
            context.getLogger().warn(
                    "Unexpected format for V vector of KblTransformation (xmlId: {}). Expected 3 coordinates but " +
                            "found {}: {}.", source.getXmlId(), v.size(), v);
        }

        final List<Double> w = crossVectors(u, v);

        destination.setA11(getElementOrDefault(u, 0, 0.0));
        destination.setA12(getElementOrDefault(v, 0, 0.0));
        destination.setA13(getElementOrDefault(w, 0, 0.0));
        destination.setA21(getElementOrDefault(u, 1, 0.0));
        destination.setA22(getElementOrDefault(v, 1, 0.0));
        destination.setA23(getElementOrDefault(w, 1, 0.0));
        destination.setA31(getElementOrDefault(u, 2, 0.0));
        destination.setA32(getElementOrDefault(v, 2, 0.0));
        destination.setA33(getElementOrDefault(w, 2, 0.0));

        return TransformationResult.from(destination)
                .withLinker(Query.of(source.getCartesianPoint()), VecCartesianPoint3D.class,
                            VecTransformation3D::setOrigin)
                .build();
    }

    private List<Double> crossVectors(final List<Double> us, final List<Double> vs) {
        final double ux = us.get(0);
        final double uy = us.get(1);
        final double uz = us.get(2);
        final double vx = vs.get(0);
        final double vy = vs.get(1);
        final double vz = vs.get(2);

        final double x = uy * vz - uz * vy;
        final double y = uz * vx - ux * vz;
        final double z = ux * vy - uy * vx;

        return Arrays.asList(x, y, z);
    }
}
