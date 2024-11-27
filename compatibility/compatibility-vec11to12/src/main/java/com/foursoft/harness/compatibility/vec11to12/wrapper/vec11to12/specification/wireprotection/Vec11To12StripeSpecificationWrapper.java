/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.1.X To VEC 1.2.X
 * %%
 * Copyright (C) 2020 - 2024 4Soft GmbH
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
package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification.wireprotection;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.vec.v12x.VecCustomProperty;
import com.foursoft.harness.vec.v12x.VecNumericalValue;
import com.foursoft.harness.vec.v12x.VecNumericalValueProperty;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecStripeSpecification}
 * to {@link com.foursoft.harness.vec.v12x.VecStripeSpecification}.
 */
public class Vec11To12StripeSpecificationWrapper extends ReflectionBasedWrapper {

    private static final String GET_CUSTOM_PROPERTIES_METHOD_NAME = "getCustomProperties";
    private static final String THICKNESS_CUSTOM_PROPERTY = "Thickness";

    private VecNumericalValue thickness;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12StripeSpecificationWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getThickness".equals(method.getName())) {
            return getThickness();
        }
        if ("setThickness".equals(method.getName())) {
            return setThickness((VecNumericalValue) allArguments[0]);
        }
        if (GET_CUSTOM_PROPERTIES_METHOD_NAME.equals(method.getName())) {
            return getCustomPropertiesWithoutThickness();
        }

        return super.wrapObject(obj, method, allArguments);
    }

    // The VEC 1.2.0 is not supposed to provide this CustomProperty anymore since it's an own field now.
    private List<VecCustomProperty> getCustomPropertiesWithoutThickness() {
        return wrapList(GET_CUSTOM_PROPERTIES_METHOD_NAME, VecCustomProperty.class).stream()
                .filter(c -> !c.getPropertyType().equalsIgnoreCase(THICKNESS_CUSTOM_PROPERTY))
                .toList();
    }

    private Object setThickness(final VecNumericalValue arg) {
        thickness = arg;
        return null;
    }

    private VecNumericalValue getThickness() {
        if (thickness == null) {
            thickness = wrapList(GET_CUSTOM_PROPERTIES_METHOD_NAME, VecCustomProperty.class).stream()
                    .filter(c -> c.getPropertyType().equalsIgnoreCase(THICKNESS_CUSTOM_PROPERTY))
                    .filter(VecNumericalValueProperty.class::isInstance)
                    .map(VecNumericalValueProperty.class::cast)
                    .map(VecNumericalValueProperty::getValue)
                    .findFirst().orElse(null);
        }

        return thickness;
    }

}
