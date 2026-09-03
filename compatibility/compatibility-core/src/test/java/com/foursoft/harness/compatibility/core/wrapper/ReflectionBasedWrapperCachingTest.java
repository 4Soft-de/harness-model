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
import com.foursoft.harness.compatibility.core.PropertyAdditionProvider;
import com.foursoft.harness.compatibility.core.PurePropertyAdditions;
import com.foursoft.harness.compatibility.core.mapping.ClassMapper;
import com.foursoft.harness.compatibility.core.wrapper.fixture.additions.AdditionsBean;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static com.foursoft.harness.compatibility.core.PropertyAddition.list;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * A {@link ReflectionBasedWrapper} exists once per wrapped object, so it builds the maps it needs only
 * when they are actually used. These tests cover that the state is still per wrapper and still stable
 * across calls.
 */
class ReflectionBasedWrapperCachingTest {

    private static final String FIXTURE_PACKAGE =
            "com.foursoft.harness.compatibility.core.wrapper.fixture.additions";

    @Test
    void aCollectionGetterAnswersWithTheSameInstanceOnEveryCall() throws Exception {
        final Object proxy = proxyFor(new AdditionsBean(), new PurePropertyAdditions());

        final Object first = invoke(proxy, "getBarList");
        final Object second = invoke(proxy, "getBarList");

        assertThat(first).isNotNull().isSameAs(second);
    }

    @Test
    void differentCollectionGettersAnswerWithDifferentInstances() throws Exception {
        final Object proxy = proxyFor(new AdditionsBean(), new PurePropertyAdditions());

        assertThat(invoke(proxy, "getBarList")).isNotSameAs(invoke(proxy, "getBazSet"));
    }

    @Test
    void twoWrappersOfTheSameClassDoNotShareTheirAddedProperties() throws Exception {
        final PurePropertyAdditions additions = new PurePropertyAdditions()
                .register(AdditionsBean.class, list("barList"));
        final Object first = proxyFor(new AdditionsBean(), additions);
        final Object second = proxyFor(new AdditionsBean(), additions);

        ((List<Object>) invoke(first, "getBarList")).add("only for the first wrapper");

        assertThat((List<?>) invoke(second, "getBarList")).isEmpty();
    }

    private Object invoke(final Object proxy, final String methodName) throws Exception {
        final Method method = proxy.getClass().getMethod(methodName);
        return method.invoke(proxy);
    }

    private Object proxyFor(final Object source, final PurePropertyAdditions additions) {
        final CompatibilityContext context = new CompatibilityContextBuilder()
                .withClassMapper(new AdditionsClassMapper(additions))
                .build();
        return context.getWrapperProxyFactory().createProxy(source);
    }

    private record AdditionsClassMapper(PurePropertyAdditions additions)
            implements ClassMapper, PropertyAdditionProvider {

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

        @Override
        public PurePropertyAdditions getPropertyAdditions() {
            return additions;
        }

    }

}
