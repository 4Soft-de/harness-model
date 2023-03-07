package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.field;

import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.compatibility.core.util.IdCreator;
import com.foursoft.harness.compatibility.vec11to12.Constants;
import com.foursoft.harness.compatibility.vec11to12.util.WrapperUtils;
import com.foursoft.harness.vec.v12x.VecNumericalValue;
import com.foursoft.harness.vec.v12x.VecSize;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecSize cavityDimension}
 * to {@link com.foursoft.harness.vec.v113.VecNumericalValue cavityDiameter}.
 */
public class Vec11To12CavityDimensionWrapper {

    private final Context context;
    private final Object target;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12CavityDimensionWrapper(final Context context, final Object target) {
        this.context = context;
        this.target = target;
    }

    /**
     * Converts the {@link Constants#GET_CAVITY_DIAMETER} from VEC 1.1
     * to {@link Constants#GET_CAVITY_DIMENSION} of VEC 1.2.
     *
     * @return The converted {@link Constants#GET_CAVITY_DIAMETER}.
     * @throws NoSuchMethodException     If {@code GET_CAVITY_DIAMETER} cannot be called on the target object.
     * @throws InvocationTargetException If {@code GET_CAVITY_DIAMETER} cannot be invoked on the target object.
     * @throws IllegalAccessException    See {@link Method#invoke(Object, Object...)}.
     */
    public VecSize getCavityDimension()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method declaredMethod = target.getClass().getMethod(Constants.GET_CAVITY_DIAMETER);
        final Object invoke = declaredMethod.invoke(target);

        return Optional.ofNullable(invoke)
                .map(context.getWrapperProxyFactory()::createProxy)
                .filter(VecNumericalValue.class::isInstance)
                .map(VecNumericalValue.class::cast)
                .map(Vec11To12CavityDimensionWrapper::createSize)
                .orElse(null);
    }

    /**
     * Sets the cavityDiameter by converting the given {@link VecSize} from VEC 1.2
     * to a {@link com.foursoft.harness.vec.v113.VecNumericalValue} of VEC 1.1.
     *
     * @param dimension VecSize to convert.
     * @throws NoSuchMethodException     If {@code SET_CAVITY_DIAMETER} cannot be called on the target object.
     * @throws InvocationTargetException If {@code SET_CAVITY_DIAMETER} cannot be invoked on the target object.
     * @throws IllegalAccessException    See {@link Method#invoke(Object, Object...)}.
     */
    public void setCavityDimension(final VecSize dimension)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final VecNumericalValue diameter = createDiameter(dimension);

        final com.foursoft.harness.vec.v113.VecNumericalValue value =
                context.getWrapperProxyFactory().createProxy(diameter);

        final Method declaredMethod = target.getClass()
                .getDeclaredMethod(Constants.SET_CAVITY_DIAMETER,
                                   com.foursoft.harness.vec.v113.VecNumericalValue.class);
        declaredMethod.invoke(target, value);
    }

    private static VecNumericalValue createDiameter(final VecSize vecSize) {
        VecNumericalValue diameter = vecSize.getHeight();
        if (diameter == null) {
            diameter = vecSize.getWidth();
        }

        return diameter;
    }

    private static VecSize createSize(final VecNumericalValue vecNumericalValue) {
        final String xmlId = IdCreator.generateXmlId(VecNumericalValue.class);
        vecNumericalValue.setXmlId(xmlId);

        final VecSize tempVecSize = new VecSize();
        tempVecSize.setHeight(vecNumericalValue);
        tempVecSize.setWidth(WrapperUtils.copyVec12xNumericalValue(vecNumericalValue));
        final String generateXmlId = IdCreator.generateXmlId(tempVecSize);
        tempVecSize.setXmlId(generateXmlId);
        return tempVecSize;
    }

}
