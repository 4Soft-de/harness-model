/*-
 * ========================LICENSE_START=================================
 * compatibility-vec11to12
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
package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.vec.v12x.VecWireElement;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecWireSpecification}
 * to {@link com.foursoft.harness.vec.v113.VecWireSpecification}.
 */
public class Vec12To11WireSpecificationWrapper extends ReflectionBasedWrapper {

    private final List<com.foursoft.harness.vec.v113.VecWireElement> wireElements = new ArrayList<>();

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11WireSpecificationWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getWireElements".equals(method.getName())) {
            if (!wireElements.isEmpty()) {
                return wireElements;
            }
            final com.foursoft.harness.vec.v12x.VecWireElement wireElement12 =
                    getResultObject("getWireElement",
                                    com.foursoft.harness.vec.v12x.VecWireElement.class,
                                    allArguments)
                            .orElse(null);
            if (null == wireElement12) {
                return null;
            }
            wireElements.add(getContext().getWrapperProxyFactory().createProxy(wireElement12));

            final List<VecWireElement> subWireElements = wireElement12.getSubWireElements();
            if (!subWireElements.isEmpty()) {
                wireElements.addAll(subWireElements.stream()
                                            .map(getContext().getWrapperProxyFactory()::createProxy)
                                            .map(com.foursoft.harness.vec.v113.VecWireElement.class::cast)
                                            .collect(Collectors.toList()));
            }

            return wireElements;
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
