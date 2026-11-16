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
package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11;

import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.compatibility.core.wrapper.Wraps;
import com.foursoft.harness.vec.v113.VecPrimaryPartType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecPartVersion}
 * to {@link com.foursoft.harness.vec.v113.VecPartVersion}.
 * <p>
 * VEC 1.2 added primary part types which VEC 1.1 does not know. As the primary part type is mandatory in
 * VEC 1.1, such a part gets the closest, more general type VEC 1.1 offers instead of none.
 */
@Wraps(com.foursoft.harness.vec.v12x.VecPartVersion.class)
public class PartVersionWrapper extends ReflectionBasedWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartVersionWrapper.class);

    private static final Map<com.foursoft.harness.vec.v12x.VecPrimaryPartType, VecPrimaryPartType> REPLACEMENTS =
            Map.of(com.foursoft.harness.vec.v12x.VecPrimaryPartType.BOLT_TERMINAL, VecPrimaryPartType.TERMINAL,
                   com.foursoft.harness.vec.v12x.VecPrimaryPartType.OPEN_WIRE_END, VecPrimaryPartType.TERMINAL,
                   com.foursoft.harness.vec.v12x.VecPrimaryPartType.CAPACITOR, VecPrimaryPartType.EE_COMPONENT,
                   com.foursoft.harness.vec.v12x.VecPrimaryPartType.DIODE, VecPrimaryPartType.EE_COMPONENT,
                   com.foursoft.harness.vec.v12x.VecPrimaryPartType.FERRITE, VecPrimaryPartType.OTHER);

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public PartVersionWrapper(final Context context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getPrimaryPartType".equals(method.getName())) {
            return getResultObject("getPrimaryPartType", com.foursoft.harness.vec.v12x.VecPrimaryPartType.class)
                    .map(this::toVec11PrimaryPartType)
                    .orElse(null);
        }
        return super.wrapObject(obj, method, allArguments);
    }

    private VecPrimaryPartType toVec11PrimaryPartType(final com.foursoft.harness.vec.v12x.VecPrimaryPartType type) {
        final VecPrimaryPartType replacement = REPLACEMENTS.get(type);
        if (replacement != null) {
            LOGGER.debug("Primary part type {} does not exist in VEC 1.1, it is converted to {}.", type,
                         replacement);
            return replacement;
        }
        return VecPrimaryPartType.valueOf(type.name());
    }

}
