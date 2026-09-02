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
package com.foursoft.harness.compatibility.core;

import com.foursoft.harness.compatibility.core.mapping.NameBasedClassMapper;
import com.foursoft.harness.compatibility.core.wrapper.fixture.mapping.sourcepackage.MappedBean;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WrapperProxyFactoryTest {

    private static final String SOURCE_PACKAGE =
            "com.foursoft.harness.compatibility.core.wrapper.fixture.mapping.sourcepackage";
    private static final String TARGET_PACKAGE =
            "com.foursoft.harness.compatibility.core.wrapper.fixture.mapping.targetpackage";

    @Test
    void returnsTheSameProxyForTheSameObject() {
        final WrapperProxyFactory factory = contextFor(new ArrayList<>()).getWrapperProxyFactory();
        final MappedBean bean = new MappedBean("bean");

        assertThat((Object) factory.createProxy(bean)).isSameAs(factory.createProxy(bean));
    }

    @Test
    void returnsSeparateProxiesForEqualButDistinctObjects() {
        // Two distinct source objects are two distinct objects in the converted model as well, even
        // when they consider themselves equal - the wrapper cache is keyed by identity.
        final WrapperProxyFactory factory = contextFor(new ArrayList<>()).getWrapperProxyFactory();

        final Object first = factory.createProxy(new MappedBean("same"));
        final Object second = factory.createProxy(new MappedBean("same"));

        assertThat(first).isNotSameAs(second);
    }

    @Test
    void passesTheSameMethodObjectToTheHandlerOnEveryCall() throws Exception {
        // The generated proxy caches the Method it hands to the invocation handler. Creating a fresh
        // one per call would mean a reflective lookup on every single invocation.
        final List<Method> invoked = new ArrayList<>();
        final Object proxy = contextFor(invoked).getWrapperProxyFactory().createProxy(new MappedBean("bean"));

        final Method getName = proxy.getClass().getMethod("getName");
        getName.invoke(proxy);
        getName.invoke(proxy);

        assertThat(invoked).hasSize(2)
                .satisfies(methods -> assertThat(methods.get(0)).isSameAs(methods.get(1)));
    }

    private CompatibilityContext contextFor(final List<Method> invoked) {
        final InvocationHandler handler = (proxy, method, args) -> {
            invoked.add(method);
            return null;
        };
        return new CompatibilityContext.CompatibilityContextBuilder()
                .withClassMapper(new TestClassMapper())
                .withDefaultWrapperFactory((context, target) -> handler)
                .build();
    }

    private static class TestClassMapper extends NameBasedClassMapper {

        TestClassMapper() {
            super(SOURCE_PACKAGE, TARGET_PACKAGE);
        }

    }

}
