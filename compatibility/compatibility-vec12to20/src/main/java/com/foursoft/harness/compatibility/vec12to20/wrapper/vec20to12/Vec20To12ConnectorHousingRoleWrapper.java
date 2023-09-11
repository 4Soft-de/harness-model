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
import com.foursoft.harness.vec.v2x.VecComponentConnector;
import com.foursoft.harness.vec.v2x.VecComponentNode;
import com.foursoft.harness.vec.v2x.VecConnectorHousingRole;

import java.lang.reflect.Method;
import java.util.Comparator;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecConnectorHousingRole}
 * to {@link VecConnectorHousingRole}.
 */
public class Vec20To12ConnectorHousingRoleWrapper extends ReflectionBasedWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec20To12ConnectorHousingRoleWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    private com.foursoft.harness.vec.v12x.VecComponentNode node;

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        final String methodName = method.getName();
        if ("getComponentNode".equals(methodName)) {
            if (node == null) {
                node = getResultList("getComponentConnector", VecComponentConnector.class, allArguments[0]).stream()
                        .map(VecComponentConnector::getParentComponentNode)
                        .distinct()
                        .min(Comparator.comparing(VecComponentNode::getIdentification))
                        .map(getContext().getWrapperProxyFactory()::createProxy)
                        .map(com.foursoft.harness.vec.v12x.VecComponentNode.class::cast)
                        .orElse(null);
            }
            return node;
        } else if ("setComponentNode".equals(methodName)) {
            node = (com.foursoft.harness.vec.v12x.VecComponentNode) allArguments[0];
        }

        return super.wrapObject(obj, method, allArguments);
    }

}  
