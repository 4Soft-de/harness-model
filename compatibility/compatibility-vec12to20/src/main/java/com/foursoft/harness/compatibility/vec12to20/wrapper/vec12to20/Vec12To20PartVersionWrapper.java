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
package com.foursoft.harness.compatibility.vec12to20.wrapper.vec12to20;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.vec.v2x.VecLanguageCode;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecPartVersion;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecPartVersion}
 * to {@link VecPartVersion}.
 */
public class Vec12To20PartVersionWrapper extends ReflectionBasedWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To20PartVersionWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    protected VecLocalizedString preferredUseCase;

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        final String methodName = method.getName();
        if ("getPreferredUseCase".equals(methodName)) {
            if (preferredUseCase == null) {
                String xmlId = getResultObject("getXmlId", String.class)
                        .orElse(UUID.randomUUID().toString().substring(0, 10));
                preferredUseCase = wrapToGerman(getResultObject("getPreferredUseCase", String.class).orElse(""), xmlId);
            }
            return preferredUseCase;
        } else if ("setPreferredUseCase".equals(methodName)) {
            if (allArguments[0] instanceof VecLocalizedString valueAsString) {
                preferredUseCase = valueAsString;
            }
        }

        return super.wrapObject(obj, method, allArguments);
    }

    public static VecLocalizedString wrapToGerman(String text, String xmlId) {
        VecLocalizedString result = new VecLocalizedString();
        result.setLanguageCode(VecLanguageCode.DE);
        result.setValue(text);
        result.setXmlId(xmlId);
        return result;
    }

}  
