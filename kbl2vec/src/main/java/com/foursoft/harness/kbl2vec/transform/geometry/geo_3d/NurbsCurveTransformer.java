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
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.kbl2vec.transform.geometry.KnotVector;
import com.foursoft.harness.vec.v2x.VecNURBSControlPoint;
import com.foursoft.harness.vec.v2x.VecNURBSCurve;

import java.util.List;

public class NurbsCurveTransformer implements Transformer<KblBSplineCurve, VecNURBSCurve> {

    @Override
    public TransformationResult<VecNURBSCurve> transform(final TransformationContext context,
                                                         final KblBSplineCurve source) {
        final VecNURBSCurve destination = new VecNURBSCurve();
        destination.setDegree(source.getDegree());
        destination.getKnots().addAll(deriveKnots(source, context));

        return TransformationResult.from(destination)
                .withDownstream(KblCartesianPoint.class, VecNURBSControlPoint.class,
                                Query.fromLists(source.getControlPoints()), VecNURBSCurve::getControlPoints)
                .build();
    }

    private List<Double> deriveKnots(final KblBSplineCurve source, final TransformationContext context) {
        final int degree = source.getDegree().intValue();
        final int numberOfControlPoints = source.getControlPoints().size();
        final int order = degree + 1;
        final String clamping = context.getConversionProperties().getDefault3DCurveClamping();
        final KnotVector knotVector = new KnotVector(degree, order, numberOfControlPoints, clamping);
        return knotVector.getKnots();
    }
}
