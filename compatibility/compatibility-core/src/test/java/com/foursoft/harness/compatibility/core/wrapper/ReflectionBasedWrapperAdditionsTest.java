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
import com.foursoft.harness.compatibility.core.wrapper.fixture.additions.ParentAdditionsBean;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static com.foursoft.harness.compatibility.core.PropertyAddition.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that {@link ReflectionBasedWrapper} automatically applies
 * {@link com.foursoft.harness.compatibility.core.PropertyAddition}s declared by the class mapper
 * (when the mapper implements {@link PropertyAdditionProvider}).
 */
class ReflectionBasedWrapperAdditionsTest {

    private static final String FIXTURE_PACKAGE =
            "com.foursoft.harness.compatibility.core.wrapper.fixture.additions";

    // ── value additions ──────────────────────────────────────────────────────

    @Test
    void valueAddition_getterReturnsNullInitially() throws Exception {
        final Object proxy = proxyFor(new AdditionsBean(),
                                      new PurePropertyAdditions()
                                              .register(AdditionsBean.class, value("foo")));

        final Object result = getFoo(proxy);

        assertThat(result).isNull();
    }

    @Test
    void valueAddition_setterStoresValue_getterReturnsIt() throws Exception {
        final Object proxy = proxyFor(new AdditionsBean(),
                                      new PurePropertyAdditions()
                                              .register(AdditionsBean.class, value("foo")));

        setFoo(proxy, "stored");

        assertThat(getFoo(proxy)).isEqualTo("stored");
    }

    @Test
    void valueAddition_doesNotDelegateToUnderlyingBean() throws Exception {
        // Without the addition, getFoo() would delegate to AdditionsBean and return "fooOriginal".
        // With the addition, the wrapper intercepts and returns null / stored value.
        final Object proxy = proxyFor(new AdditionsBean(),
                                      new PurePropertyAdditions()
                                              .register(AdditionsBean.class, value("foo")));

        assertThat(getFoo(proxy)).isNotEqualTo("fooOriginal");
    }

    // ── list additions ───────────────────────────────────────────────────────

    @Test
    void listAddition_returnsEmptyListNotNull() throws Exception {
        final Object proxy = proxyFor(new AdditionsBean(),
                                      new PurePropertyAdditions()
                                              .register(AdditionsBean.class, list("barList")));

        final Object result = getBarList(proxy);

        assertThat(result).isNotNull().isInstanceOf(List.class);
        assertThat((List<?>) result).isEmpty();
    }

    @Test
    void listAddition_returnsSameInstanceOnRepeatedCalls() throws Exception {
        final Object proxy = proxyFor(new AdditionsBean(),
                                      new PurePropertyAdditions()
                                              .register(AdditionsBean.class, list("barList")));

        final Object first = getBarList(proxy);
        final Object second = getBarList(proxy);

        assertThat(first).isSameAs(second);
    }

    // ── backRef additions ────────────────────────────────────────────────────

    @Test
    void backRefAddition_returnsEmptySet() throws Exception {
        final Object proxy = proxyFor(new AdditionsBean(),
                                      new PurePropertyAdditions()
                                              .register(AdditionsBean.class, backRef("bazSet")));

        final Object result = getBazSet(proxy);

        assertThat(result).isNotNull().isInstanceOf(Set.class);
        assertThat((Set<?>) result).isEmpty();
    }

    @Test
    void backRefAddition_returnsSameInstanceOnRepeatedCalls() throws Exception {
        final Object proxy = proxyFor(new AdditionsBean(),
                                      new PurePropertyAdditions()
                                              .register(AdditionsBean.class, backRef("bazSet")));

        final Object first = getBazSet(proxy);
        final Object second = getBazSet(proxy);

        assertThat(first).isSameAs(second);
    }

    // ── inheritance ──────────────────────────────────────────────────────────

    @Test
    void additionOnParentClass_isAppliedWhenWrappingSubclass() throws Exception {
        // parentProp is declared on ParentAdditionsBean; AdditionsBean extends it.
        final Object proxy = proxyFor(new AdditionsBean(),
                                      new PurePropertyAdditions()
                                              .register(ParentAdditionsBean.class, value("parentProp")));

        setParentProp(proxy, "fromParentAddition");

        assertThat(getParentProp(proxy)).isEqualTo("fromParentAddition");
    }

    @Test
    void withoutAddition_delegationToUnderlyingBeanStillWorks() throws Exception {
        // No addition registered for getParentProp → wrapper falls through and delegates.
        final Object proxy = proxyFor(new AdditionsBean(), new PurePropertyAdditions());

        assertThat(getParentProp(proxy)).isEqualTo("parentOriginal");
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Object proxyFor(final Object source, final PurePropertyAdditions additions) {
        final CompatibilityContext ctx = new CompatibilityContextBuilder()
                .withClassMapper(new AdditionsClassMapper(additions))
                .build();
        return ctx.getWrapperProxyFactory().createProxy(source);
    }

    private static Object getFoo(final Object proxy) throws Exception {
        return method(proxy, "getFoo").invoke(proxy);
    }

    private static void setFoo(final Object proxy, final String value) throws Exception {
        method(proxy, "setFoo", String.class).invoke(proxy, value);
    }

    private static Object getBarList(final Object proxy) throws Exception {
        return method(proxy, "getBarList").invoke(proxy);
    }

    private static Object getBazSet(final Object proxy) throws Exception {
        return method(proxy, "getBazSet").invoke(proxy);
    }

    private static Object getParentProp(final Object proxy) throws Exception {
        return method(proxy, "getParentProp").invoke(proxy);
    }

    private static void setParentProp(final Object proxy, final String value) throws Exception {
        method(proxy, "setParentProp", String.class).invoke(proxy, value);
    }

    private static Method method(final Object proxy, final String name, final Class<?>... params)
            throws NoSuchMethodException {
        return proxy.getClass().getMethod(name, params);
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
