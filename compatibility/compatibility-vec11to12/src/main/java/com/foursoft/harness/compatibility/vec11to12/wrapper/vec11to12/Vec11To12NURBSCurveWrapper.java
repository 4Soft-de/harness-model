/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.1.X To VEC 1.2.X
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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
package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.util.IdCreator;
import com.foursoft.harness.compatibility.core.util.ReflectionUtils;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.vec.v12x.VecCartesianPoint3D;
import com.foursoft.harness.vec.v12x.VecNURBSControlPoint;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecBSplineCurve}
 * to {@link com.foursoft.harness.vec.v12x.VecNURBSCurve}.
 */
public class Vec11To12NURBSCurveWrapper extends ReflectionBasedWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12NURBSCurveWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    private static VecNURBSControlPoint convertToNurbsControlPoint(final VecCartesianPoint3D point,
                                                                   final Object parent) {
        final VecNURBSControlPoint vecNURBSControlPoint = new VecNURBSControlPoint();
        vecNURBSControlPoint.setCartesianPoint3D(point);
        final String xmlId = IdCreator.generateXmlId(vecNURBSControlPoint);
        vecNURBSControlPoint.setXmlId(xmlId);
        vecNURBSControlPoint.setWeight(1.0);
        ReflectionUtils.setParentRelationship(vecNURBSControlPoint, "parentNURBSCurve", parent);
        return vecNURBSControlPoint;
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getControlPoints".equals(method.getName())) {
            return getControlPoints(obj);
        }

        return super.wrapObject(obj, method, allArguments);
    }

    private List<VecNURBSControlPoint> getControlPoints(final Object obj) {
        final List<VecCartesianPoint3D> wrappedVec11xControlPoints =
                wrapList("getControlPoint", VecCartesianPoint3D.class);
        return wrappedVec11xControlPoints.stream()
                .map(c -> convertToNurbsControlPoint(c, obj))
                .collect(Collectors.toList());
    }

}
