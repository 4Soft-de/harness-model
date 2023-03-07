package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.vec.v12x.VecCartesianPoint3D;
import com.foursoft.harness.vec.v12x.VecNURBSControlPoint;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecNURBSCurve}
 * to {@link com.foursoft.harness.vec.v113.VecBSplineCurve}.
 */
public class Vec12To11BSplineCurveWrapper extends ReflectionBasedWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11BSplineCurveWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    private static VecCartesianPoint3D convertToBSplineControlPoint(final VecNURBSControlPoint point) {
        return point.getCartesianPoint3D();
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getControlPoint".equals(method.getName())) {
            return getControlPoint(allArguments);
        }

        return super.wrapObject(obj, method, allArguments);
    }

    private Object getControlPoint(final Object[] args) {
        // VecNURBSControlPoint has no counterpart in VEC 1.1, thus custom handling is needed.
        // Invoke VecNURBSCurve#getControlPoints and convert that one by one.
        final List<VecNURBSControlPoint> vec12xControlPoints =
                getResultList("getControlPoints", VecNURBSControlPoint.class, args);
        // Convert each NURBS ControlPoint to a BSpline ControlPoint and create a proxy for that element.
        return vec12xControlPoints.stream()
                .filter(Objects::nonNull)
                .map(Vec12To11BSplineCurveWrapper::convertToBSplineControlPoint)
                .map(getContext().getWrapperProxyFactory()::createProxy)
                .collect(Collectors.toList());
    }

}
