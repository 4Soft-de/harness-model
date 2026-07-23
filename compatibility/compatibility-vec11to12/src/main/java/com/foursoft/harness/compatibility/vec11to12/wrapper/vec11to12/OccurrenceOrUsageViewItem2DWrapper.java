/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.1.X To VEC 1.2.X
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
import com.foursoft.harness.compatibility.core.wrapper.Wraps;
import com.foursoft.harness.vec.v113.VecOccurrenceOrUsage;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Wraps(com.foursoft.harness.vec.v113.VecOccurrenceOrUsageViewItem2D.class)
public class OccurrenceOrUsageViewItem2DWrapper extends DefaultWrapper {

    private final List<Object> occurrenceOrUsages = new ArrayList<>(1);

    public OccurrenceOrUsageViewItem2DWrapper(final CompatibilityContext context, final Object target) {
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
