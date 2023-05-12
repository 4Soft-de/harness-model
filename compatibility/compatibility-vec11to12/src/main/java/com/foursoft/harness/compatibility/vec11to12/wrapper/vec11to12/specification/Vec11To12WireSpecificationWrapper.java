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
package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.vec.v113.VecExtendableElement;
import com.foursoft.harness.vec.v12x.VecWireElement;
import com.foursoft.harness.vec.v12x.VecWireElementSpecification;
import com.foursoft.harness.vec.v12x.VecWireSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecWireSpecification}
 * to {@link com.foursoft.harness.vec.v12x.VecWireSpecification}.
 */
public class Vec11To12WireSpecificationWrapper extends ReflectionBasedWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Vec11To12WireSpecificationWrapper.class);

    private VecWireElement vecWireElement;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12WireSpecificationWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    private static boolean sameId(final VecExtendableElement element1,
                                  final com.foursoft.harness.vec.v12x.VecExtendableElement element2) {
        return element1.getXmlId().equals(element2.getXmlId());
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getWireElement".equals(method.getName())) {
            if (vecWireElement != null) {
                return vecWireElement;
            }

            final VecWireElementSpecification wireElementSpecification =
                    ((VecWireSpecification) obj).getWireElementSpecification();

            final List<com.foursoft.harness.vec.v113.VecWireElement> wireElements =
                    getResultList("getWireElements",
                                  com.foursoft.harness.vec.v113.VecWireElement.class);

            vecWireElement = wireElements.stream()
                    .filter(c -> sameId(c.getWireElementSpecification(), wireElementSpecification))
                    .map(getContext().getWrapperProxyFactory()::createProxy)
                    .map(VecWireElement.class::cast)
                    .findFirst().orElse(null);
            if (vecWireElement == null) {
                LOGGER.error("The WireElementSpecification {} has no referenced WireElement.",
                             wireElementSpecification);
                return null;
            }

            final List<VecWireElement> subWireElements = wireElements.stream()
                    .filter(c -> !sameId(c.getWireElementSpecification(), wireElementSpecification))
                    .map(getContext().getWrapperProxyFactory()::createProxy)
                    .map(VecWireElement.class::cast)
                    .toList();

            vecWireElement.getSubWireElements().addAll(subWireElements);

            return vecWireElement;
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
