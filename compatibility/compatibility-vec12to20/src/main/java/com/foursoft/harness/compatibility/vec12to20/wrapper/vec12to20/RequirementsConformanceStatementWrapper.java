/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.2.X To VEC 2.0.X
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
package com.foursoft.harness.compatibility.vec12to20.wrapper.vec12to20;

import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.compatibility.core.wrapper.Wraps;
import com.foursoft.harness.vec.v12x.VecRequirementsConformanceStatement;

import java.lang.reflect.Method;

/**
 * Wraps {@link VecRequirementsConformanceStatement} to the VEC 2.x counterpart.
 *
 * <p>Explicitly handles {@code isSatisfies}/{@code setSatisfies} using the 2-argument
 * {@code registerValueProperty} overload. This is required because the standard 1-argument
 * convenience generates a {@code get*} getter name, whereas JAXB uses {@code is*} for booleans,
 * and because the property returns a primitive {@code boolean} in VEC 2.x — returning {@code null}
 * from an unset value property would cause a NullPointerException on auto-unboxing.
 */
@Wraps(VecRequirementsConformanceStatement.class)
public class RequirementsConformanceStatementWrapper extends DefaultWrapper {

    public RequirementsConformanceStatementWrapper(final Context context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments)
            throws Throwable {
        if ("isSatisfies".equals(method.getName())) {
            return Boolean.FALSE;
        }
        if ("setSatisfies".equals(method.getName())) {
            return null;
        }
        return super.wrapObject(obj, method, allArguments);
    }
}
