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
import com.foursoft.harness.vec.v113.VecPartRelationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecPartRelation}
 * to {@link com.foursoft.harness.vec.v113.VecPartRelation}.
 * <p>
 * VEC 1.2 added the relation type {@code Custom}, whose meaning is given by the custom relation expression.
 * VEC 1.1 knows neither of them, and its relation type is mandatory and a closed enumeration. A custom relation
 * therefore becomes {@code Optional}, the weakest statement VEC 1.1 can make about an accessory. As a part
 * relation of VEC 1.1 cannot carry custom properties, the expression is lost; a warning names it.
 */
@Wraps(com.foursoft.harness.vec.v12x.VecPartRelation.class)
public class PartRelationWrapper extends ReflectionBasedWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartRelationWrapper.class);

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public PartRelationWrapper(final Context context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getRelationType".equals(method.getName())) {
            return getResultObject("getRelationType", com.foursoft.harness.vec.v12x.VecPartRelationType.class)
                    .map(this::toVec11)
                    .orElse(null);
        }
        return super.wrapObject(obj, method, allArguments);
    }

    private VecPartRelationType toVec11(final com.foursoft.harness.vec.v12x.VecPartRelationType relationType) {
        if (relationType == com.foursoft.harness.vec.v12x.VecPartRelationType.CUSTOM) {
            final com.foursoft.harness.vec.v12x.VecPartRelation relation =
                    (com.foursoft.harness.vec.v12x.VecPartRelation) getTarget();
            LOGGER.warn("Part relation {} has the relation type Custom, which does not exist in VEC 1.1. It is "
                                + "converted to Optional, its custom relation expression '{}' is lost.",
                        relation.getXmlId(), relation.getCustomRelationExpression());
            return VecPartRelationType.OPTIONAL;
        }
        return VecPartRelationType.valueOf(relationType.name());
    }

}
