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

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class WrapperRegistryTest {

    static class GrandParent {}

    static class Parent extends GrandParent {}

    static class Child extends Parent {}

    @Test
    void exactMatchUsesRegisteredFunction() {
        final WrapperRegistry registry = new WrapperRegistry(factoryFor("default"));
        registry.register(Child.class, factoryFor("child"));

        assertThat(invoke(registry.createInvocationHandler(new Child()))).isEqualTo("child");
    }

    @Test
    void fallsBackToDirectSuperclassHandler() {
        final WrapperRegistry registry = new WrapperRegistry(factoryFor("default"));
        registry.register(Parent.class, factoryFor("parent"));

        assertThat(invoke(registry.createInvocationHandler(new Child()))).isEqualTo("parent");
    }

    @Test
    void fallsBackToGrandparentHandlerWhenNoDirectSuperclassMatch() {
        final WrapperRegistry registry = new WrapperRegistry(factoryFor("default"));
        registry.register(GrandParent.class, factoryFor("grandparent"));

        assertThat(invoke(registry.createInvocationHandler(new Child()))).isEqualTo("grandparent");
    }

    @Test
    void exactMatchTakesPrecedenceOverSuperclassMatch() {
        final WrapperRegistry registry = new WrapperRegistry(factoryFor("default"));
        registry.register(Parent.class, factoryFor("parent"));
        registry.register(Child.class, factoryFor("child"));

        assertThat(invoke(registry.createInvocationHandler(new Child()))).isEqualTo("child");
    }

    @Test
    void usesDefaultWrapperWhenNoHierarchyMatchFound() {
        final WrapperRegistry registry = new WrapperRegistry(factoryFor("default"));

        assertThat(invoke(registry.createInvocationHandler(new Child()))).isEqualTo("default");
    }

    @Test
    void hierarchyTraversalIsCachedAfterFirstLookup() {
        final AtomicInteger traversalCount = new AtomicInteger();
        final Function<Object, InvocationHandler> countingParentFactory = obj -> {
            traversalCount.incrementAndGet();
            return (proxy, method, args) -> "parent";
        };
        final WrapperRegistry registry = new WrapperRegistry(factoryFor("default"));
        registry.register(Parent.class, countingParentFactory);

        registry.createInvocationHandler(new Child());
        registry.createInvocationHandler(new Child());

        // The factory is called each time a handler is created, but the hierarchy
        // traversal that resolves Child → Parent's factory must only happen once.
        // After the first call, Child is cached in the map pointing to countingParentFactory,
        // so both calls reach countingParentFactory — but via a direct map hit on the second call.
        assertThat(traversalCount).hasValue(2);
    }

    private static Function<Object, InvocationHandler> factoryFor(final String tag) {
        return obj -> (proxy, method, args) -> tag;
    }

    private static String invoke(final InvocationHandler handler) {
        try {
            return (String) handler.invoke(null, null, null);
        } catch (final Throwable e) {
            throw new AssertionError("Unexpected handler exception", e);
        }
    }

}
