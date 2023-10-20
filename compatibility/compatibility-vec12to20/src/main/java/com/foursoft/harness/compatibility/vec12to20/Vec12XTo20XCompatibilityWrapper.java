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
package com.foursoft.harness.compatibility.vec12to20;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.CompatibilityContext.CompatibilityContextBuilder;
import com.foursoft.harness.compatibility.core.WrapperRegistry;
import com.foursoft.harness.compatibility.core.mapping.ClassMapper;
import com.foursoft.harness.compatibility.core.wrapper.CompatibilityWrapper;
import com.foursoft.harness.compatibility.vec12to20.wrapper.vec12to20.*;
import com.foursoft.harness.compatibility.vec12to20.wrapper.vec20to12.*;
import com.foursoft.harness.vec.v2x.*;

/**
 * Compatibility Wrapper for VEC 1.2.X to VEC 2.X.X and vice versa.
 */
public final class Vec12XTo20XCompatibilityWrapper implements CompatibilityWrapper {

    private final CompatibilityContext compatibilityContext;

    /**
     * Creates a new VEC 1.2 to VEC 2.0 wrapper.
     */
    public Vec12XTo20XCompatibilityWrapper() {
        final ClassMapper classMapper = new Vec12XTo20XClassMapper();
        final CompatibilityContext context =
                new CompatibilityContextBuilder()
                        .withClassMapper(classMapper)
                        .withUnsupportedMethodCheck(classMapper.checkUnsupportedMethods())
                        .build();

        initializeRegistry(context);

        compatibilityContext = context;
    }

    @Override
    public CompatibilityContext getContext() {
        return compatibilityContext;
    }

    private static void initializeRegistry(final CompatibilityContext context) {
        final WrapperRegistry registry = context.getWrapperRegistry();

        // VEC 1.2.X -> VEC 2.X.X
        registry.register(com.foursoft.harness.vec.v12x.VecConnectorHousingRole.class,
                          c -> new Vec12To20ConnectorHousingRoleWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecDocumentVersion.class,
                          c -> new Vec12To20DocumentVersionWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecPartVersion.class,
                          c -> new Vec12To20PartVersionWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecContent.class,
                          c -> new Vec12To20ContentWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecPartOccurrence.class,
                          c -> new Vec12To20PartOccurrenceWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecPartUsage.class,
                          c -> new Vec12To20PartUsageWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecCableLeadThrough.class,
                          c -> new Vec12To20CableLeadThroughWrapper(context, c));

        // VEC 2.X.X -> VEC 1.2.X
        registry.register(VecConnectorHousingRole.class,
                          c -> new Vec20To12ConnectorHousingRoleWrapper(context, c))
                .register(VecDocumentVersion.class,
                          c -> new Vec20To12DocumentVersionWrapper(context, c))
                .register(VecPartVersion.class,
                          c -> new Vec20To12PartVersionWrapper(context, c))
                .register(VecContent.class,
                          c -> new Vec20To12ContentWrapper(context, c))
                .register(VecCableLeadThrough.class,
                          c -> new Vec20To12CableLeadThroughWrapper(context, c))
                .register(VecPartOccurrence.class,
                          c -> new Vec20To12PartOccurrenceWrapper(context, c))
                .register(VecPartUsage.class,
                          c -> new Vec20To12PartUsageWrapper(context, c));
    }

}
