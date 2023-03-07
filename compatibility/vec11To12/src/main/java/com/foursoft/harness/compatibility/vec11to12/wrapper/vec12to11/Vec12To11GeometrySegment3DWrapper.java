package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.vec.v113.VecBSplineCurve;

import java.lang.reflect.Method;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecGeometrySegment3D}
 * to {@link com.foursoft.harness.vec.v113.VecGeometrySegment3D}.
 */
public class Vec12To11GeometrySegment3DWrapper extends ReflectionBasedWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11GeometrySegment3DWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getBSplineCurves".equals(method.getName())) {
            // getCurves returns a List of VecCurve3D / VecNURBSCurve which will be converted to VecBSplineCurves.
            return wrapList("getCurves", VecBSplineCurve.class, allArguments);
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
