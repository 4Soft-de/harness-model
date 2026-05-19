/*-
 * ========================LICENSE_START=================================
 * Compatibility Core
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
package com.foursoft.harness.compatibility.core.wrapper;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.CompatibilityContext.CompatibilityContextBuilder;
import com.foursoft.harness.compatibility.core.mapping.ClassMapper;
import com.foursoft.harness.compatibility.core.wrapper.fixture.additions.AdditionsBean;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the null-to-empty-list coercion in {@link ReflectionBasedWrapper#invoke}.
 * <p>
 * The condition {@code List.class.isAssignableFrom(method.getReturnType())} must only
 * fire for actual List return types, not for supertypes of List such as {@code Object}
 * or {@code Collection} (regression: the operands were previously reversed).
 */
class ReflectionBasedWrapperNullCoercionTest {

    private static final String FIXTURE_PACKAGE =
            "com.foursoft.harness.compatibility.core.wrapper.fixture.additions";

    @Test
    void nullReturn_onListMethod_isCoercedToEmptyList() throws Exception {
        final Object proxy = proxyFor(new AdditionsBean());

        final Object result = method(proxy, "getNullList").invoke(proxy);

        assertThat(result).isNotNull().isInstanceOf(List.class);
        assertThat((List<?>) result).isEmpty();
    }

    @Test
    void nullReturn_onObjectMethod_remainsNull() throws Exception {
        // Regression: with the reversed isAssignableFrom, Object-typed methods that returned
        // null were incorrectly coerced to new ArrayList<>() because Object.isAssignableFrom(List)
        // is true (List IS-A Object). The corrected check is List.isAssignableFrom(returnType).
        final Object proxy = proxyFor(new AdditionsBean());

        final Object result = method(proxy, "getNullObject").invoke(proxy);

        assertThat(result).isNull();
    }

    private Object proxyFor(final Object source) {
        final CompatibilityContext ctx = new CompatibilityContextBuilder()
                .withClassMapper(new SimpleClassMapper())
                .build();
        return ctx.getWrapperProxyFactory().createProxy(source);
    }

    private static Method method(final Object proxy, final String name, final Class<?>... params)
            throws NoSuchMethodException {
        return proxy.getClass().getMethod(name, params);
    }

    private static final class SimpleClassMapper implements ClassMapper {
        @Override
        public Class<?> map(final Class<?> clazz) {
            return clazz;
        }

        @Override
        public String getSourcePackageName() {
            return FIXTURE_PACKAGE;
        }

        @Override
        public String getTargetPackageName() {
            return FIXTURE_PACKAGE;
        }
    }
}
