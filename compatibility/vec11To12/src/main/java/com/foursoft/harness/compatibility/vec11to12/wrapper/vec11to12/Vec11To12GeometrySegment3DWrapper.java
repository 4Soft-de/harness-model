package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.vec.v12x.VecCurve3D;

import java.lang.reflect.Method;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecGeometrySegment3D}
 * to {@link com.foursoft.harness.vec.v12x.VecGeometrySegment3D}.
 */
public class Vec11To12GeometrySegment3DWrapper extends ReflectionBasedWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12GeometrySegment3DWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getCurves".equals(method.getName())) {
            // getBSplineCurves returns VecBSplineCurves which will be converted to a List of VecCurve3D.
            return wrapList("getBSplineCurves", VecCurve3D.class);
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
