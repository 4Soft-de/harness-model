/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.2.X To VEC 2.X.X
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
package com.foursoft.harness.compatibility.vec12to20.wrapper.vec12to20;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.vec.v2x.VecCableLeadThrough;
import com.foursoft.harness.vec.v2x.VecCableLeadThroughOutlet;
import com.foursoft.harness.vec.v2x.VecPlacementPoint;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecCableLeadThrough}
 * to {@link VecCableLeadThrough}.
 */
public class Vec12To20CableLeadThroughWrapper extends ReflectionBasedWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To20CableLeadThroughWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        final String methodName = method.getName();
        if ("getOutlets".equals(methodName)) {
            return getResultList("getPlacementPoint", com.foursoft.harness.vec.v12x.VecPlacementPoint.class,
                                 allArguments[0])
                    .stream()
                    .map(this::wrapPointWithOutlet)
                    .collect(Collectors.toList());
        }
        return super.wrapObject(obj, method, allArguments);
    }

    private VecCableLeadThroughOutlet wrapPointWithOutlet(
            com.foursoft.harness.vec.v12x.VecPlacementPoint placementPoint) {
        VecPlacementPoint wrappedPlacementPoint = getContext().getWrapperProxyFactory().createProxy(placementPoint);
        VecCableLeadThroughOutlet outlet = new VecCableLeadThroughOutlet();
        outlet.setXmlId(UUID.randomUUID().toString().substring(0, 10));
        outlet.setPlacementPoint(wrappedPlacementPoint);
        outlet.setIdentification("CableLeadThroughOutlet_" + placementPoint.getIdentification());
        return outlet;
    }

}  
