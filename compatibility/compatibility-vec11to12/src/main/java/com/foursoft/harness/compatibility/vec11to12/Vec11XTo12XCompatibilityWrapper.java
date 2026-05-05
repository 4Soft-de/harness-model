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
package com.foursoft.harness.compatibility.vec11to12;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.CompatibilityContext.CompatibilityContextBuilder;
import com.foursoft.harness.compatibility.core.mapping.ClassMapper;
import com.foursoft.harness.compatibility.core.wrapper.CompatibilityWrapper;
import com.foursoft.harness.compatibility.core.wrapper.WrapperAutoRegistrar;

/**
 * Compatibility Wrapper for VEC 1.1.X to VEC 1.2.X and vice versa.
 */
public final class Vec11XTo12XCompatibilityWrapper implements CompatibilityWrapper {

    private static final String WRAPPER_PACKAGE = "com.foursoft.harness.compatibility.vec11to12.wrapper";

    private final CompatibilityContext compatibilityContext;

    /**
     * Creates a new VEC 1.1 to VEC 1.2 wrapper.
     */
    public Vec11XTo12XCompatibilityWrapper() {
        final ClassMapper classMapper = new Vec11XTo12XClassMapper();
        final CompatibilityContext context =
                new CompatibilityContextBuilder()
                        .withClassMapper(classMapper)
                        .withUnsupportedMethodCheck(classMapper.checkUnsupportedMethods())
                        .build();

        WrapperAutoRegistrar.registerAll(context, WRAPPER_PACKAGE);

        compatibilityContext = context;
    }

    @Override
    public CompatibilityContext getContext() {
        return compatibilityContext;
    }

}
