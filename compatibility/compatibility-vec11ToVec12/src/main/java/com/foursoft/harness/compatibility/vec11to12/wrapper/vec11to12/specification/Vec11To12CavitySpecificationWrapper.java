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
package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.field.Vec11To12CavityDimensionWrapper;
import com.foursoft.harness.vec.v12x.VecSize;

import java.lang.reflect.Method;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecCavitySpecification}
 * to {@link com.foursoft.harness.vec.v12x.VecCavitySpecification}.
 */
public class Vec11To12CavitySpecificationWrapper extends ReflectionBasedWrapper {

    private final Vec11To12CavityDimensionWrapper cavityDimensionWrapper;

    private VecSize vecSize;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12CavitySpecificationWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
        cavityDimensionWrapper = new Vec11To12CavityDimensionWrapper(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        final String methodName = method.getName();

        if ("getCavityDimension".equals(methodName)) {
            if (vecSize == null) {
                vecSize = cavityDimensionWrapper.getCavityDimension();
            }
            return vecSize;
        }

        if ("setCavityDimension".equals(methodName) && allArguments.length == 1) {
            vecSize = (VecSize) allArguments[0];
            cavityDimensionWrapper.setCavityDimension(vecSize);
            return null;
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
