/*-
 * ========================LICENSE_START=================================
 * compatibility-vec11tovec12
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
package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.field;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.util.IdCreator;
import com.foursoft.harness.compatibility.vec11to12.Constants;
import com.foursoft.harness.compatibility.vec11to12.util.WrapperUtils;
import com.foursoft.harness.vec.v113.VecNumericalValue;
import com.foursoft.harness.vec.v113.VecSize;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static com.foursoft.harness.compatibility.vec11to12.Constants.GET_CAVITY_DIMENSION;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecNumericalValue cavityDiameter}
 * to {@link com.foursoft.harness.vec.v113.VecSize cavityDimension}.
 */
public class Vec12To11CavityDiameterWrapper {

    private final CompatibilityContext context;
    private final Object target;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11CavityDiameterWrapper(final CompatibilityContext context, final Object target) {
        this.context = context;
        this.target = target;
    }

    /**
     * Converts the {@link Constants#GET_CAVITY_DIMENSION} from VEC 1.2
     * to {@link Constants#GET_CAVITY_DIAMETER} of VEC 1.1.
     *
     * @return The converted {@link Constants#GET_CAVITY_DIMENSION}.
     * @throws NoSuchMethodException     If {@code GET_CAVITY_DIMENSION} cannot be called on the target object.
     * @throws InvocationTargetException If {@code GET_CAVITY_DIMENSION} cannot be invoked on the target object.
     * @throws IllegalAccessException    See {@link Method#invoke(Object, Object...)}.
     */
    public VecNumericalValue getCavityDiameter()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method declaredMethod = target.getClass().getMethod(GET_CAVITY_DIMENSION);
        final Object invoke = declaredMethod.invoke(target);

        return Optional.ofNullable(invoke)
                .map(context.getWrapperProxyFactory()::createProxy)
                .filter(VecSize.class::isInstance)
                .map(VecSize.class::cast)
                .map(Vec12To11CavityDiameterWrapper::createDiameter)
                .orElse(null);
    }

    /**
     * Sets the cavityDiameter by converting the given {@link VecNumericalValue} from VEC 1.1
     * to a {@link com.foursoft.harness.vec.v12x.VecSize} of VEC 1.2.
     *
     * @param diameter VecNumericalValue to convert.
     * @throws NoSuchMethodException     If {@code SET_CAVITY_DIMENSION} cannot be called on the target object.
     * @throws InvocationTargetException If {@code SET_CAVITY_DIMENSION} cannot be invoked on the target object.
     * @throws IllegalAccessException    See {@link Method#invoke(Object, Object...)}.
     */
    public void setCavityDiameter(final VecNumericalValue diameter)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final VecSize vecSize = createSize(diameter);

        final com.foursoft.harness.vec.v12x.VecSize value =
                context.getWrapperProxyFactory().createProxy(vecSize);

        final Method declaredMethod = target.getClass()
                .getDeclaredMethod(Constants.SET_CAVITY_DIMENSION, com.foursoft.harness.vec.v12x.VecSize.class);
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
        tempVecSize.setWidth(WrapperUtils.copyVec11xNumericalValue(vecNumericalValue));
        final String generateXmlId = IdCreator.generateXmlId(tempVecSize);
        tempVecSize.setXmlId(generateXmlId);
        return tempVecSize;
    }

}
