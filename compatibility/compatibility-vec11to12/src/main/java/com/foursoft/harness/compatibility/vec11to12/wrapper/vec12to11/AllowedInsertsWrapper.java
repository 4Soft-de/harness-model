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
import com.foursoft.harness.vec.v113.VecPartVersion;
import com.foursoft.harness.vec.v12x.VecPartRelation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecExtensionSlot} and
 * {@link com.foursoft.harness.vec.v12x.VecModularSlot} to their VEC 1.1 counterparts.
 * <p>
 * VEC 1.1 lists the part versions allowed as inserts directly, VEC 1.2 wraps them into part relations. The
 * accessory parts of these relations are the allowed inserts.
 */
@Wraps({com.foursoft.harness.vec.v12x.VecExtensionSlot.class, com.foursoft.harness.vec.v12x.VecModularSlot.class})
public class AllowedInsertsWrapper extends ReflectionBasedWrapper {

    private List<VecPartVersion> allowedInserts;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public AllowedInsertsWrapper(final Context context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getAllowedInserts".equals(method.getName())) {
            if (allowedInserts == null) {
                allowedInserts = getResultList("getAllowedInserts", VecPartRelation.class).stream()
                        .flatMap(relation -> relation.getAccessoryPart().stream())
                        .filter(Objects::nonNull)
                        .distinct()
                        .map(getContext().getWrapperProxyFactory()::createProxy)
                        .filter(VecPartVersion.class::isInstance)
                        .map(VecPartVersion.class::cast)
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            return allowedInserts;
        }
        return super.wrapObject(obj, method, allArguments);
    }

}
