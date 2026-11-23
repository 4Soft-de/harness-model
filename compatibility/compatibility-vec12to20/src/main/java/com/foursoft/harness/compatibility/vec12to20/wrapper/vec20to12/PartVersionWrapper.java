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
package com.foursoft.harness.compatibility.vec12to20.wrapper.vec20to12;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.compatibility.core.wrapper.Wraps;
import com.foursoft.harness.vec.v2x.VecLanguageCode;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecPartVersion;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Wrapper to wrap {@link VecPartVersion}
 * to {@link com.foursoft.harness.vec.v12x.VecPartVersion}.
 */
@Wraps(VecPartVersion.class)
public class PartVersionWrapper extends ReflectionBasedWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public PartVersionWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    protected String preferredUseCase;

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        final String methodName = method.getName();
        if ("getPreferredUseCase".equals(methodName)) {
            if (preferredUseCase == null) {
                // VEC 2.x has a list of localized strings, VEC 1.2 a single plain string. The German one is
                // preferred, as the upgrade to VEC 2.x declares the VEC 1.2 string as German.
                final List<VecLocalizedString> useCases =
                        getResultList("getPreferredUseCases", VecLocalizedString.class);
                preferredUseCase = useCases.stream()
                        .filter(useCase -> useCase.getLanguageCode() == VecLanguageCode.DE)
                        .findFirst()
                        .or(() -> useCases.stream().findFirst())
                        .map(VecLocalizedString::getValue)
                        .orElse(null);
            }
            return preferredUseCase;
        } else if ("setPreferredUseCase".equals(methodName)) {
            // VEC 2.x has no single valued property, so the value is only kept by this wrapper.
            preferredUseCase = (String) allArguments[0];
            return null;
        }

        return super.wrapObject(obj, method, allArguments);
    }

}  
