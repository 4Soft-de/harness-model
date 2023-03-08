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
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.vec.v113.VecOccurrenceOrUsage;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecOccurrenceOrUsageViewItem3D}
 * to {@link com.foursoft.harness.vec.v12x.VecOccurrenceOrUsageViewItem3D}.
 */
public class Vec11To12OccurrenceOrUsageViewItem3DWrapper extends ReflectionBasedWrapper {

    // Object so no cast is needed.
    // VEC 1.2.X has a list instead of a single object.
    private final List<Object> occurrenceOrUsages = new ArrayList<>(1);

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12OccurrenceOrUsageViewItem3DWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getOccurrenceOrUsage".equals(method.getName())) {
            if (occurrenceOrUsages.isEmpty()) {
                getResultObject("getOccurrenceOrUsage", VecOccurrenceOrUsage.class)
                        .map(getContext().getWrapperProxyFactory()::createProxy)
                        .ifPresent(occurrenceOrUsages::add);
            }

            return occurrenceOrUsages;
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
