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

import com.foursoft.harness.compatibility.core.wrapper.fixture.mapping.sourcepackage.MappedBean;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class MethodCacheTest {

    @Test
    void returnsTheMethodOfAnInitialisedClass() {
        MethodCache.initClassCache(MappedBean.class);

        assertThat(MethodCache.get(MappedBean.class, "getName"))
                .isPresent()
                .get()
                .extracting(Method::getName)
                .isEqualTo("getName");
    }

    @Test
    void returnsEmptyForAnUnknownMethod() {
        MethodCache.initClassCache(MappedBean.class);

        assertThat(MethodCache.get(MappedBean.class, "getSomethingElse")).isEmpty();
    }

    @Test
    void returnsEmptyForAnUninitialisedClass() {
        assertThat(MethodCache.get(UninitialisedBean.class, "getName")).isEmpty();
    }

    @Test
    void isSafeToInitialiseAndReadFromSeveralThreads() throws Exception {
        final ExecutorService executor = Executors.newFixedThreadPool(8);
        try {
            final List<Callable<Optional<Method>>> tasks = IntStream.range(0, 64)
                    .mapToObj(i -> (Callable<Optional<Method>>) () -> {
                        MethodCache.initClassCache(ConcurrentBean.class);
                        return MethodCache.get(ConcurrentBean.class, "getName");
                    })
                    .toList();

            final List<Future<Optional<Method>>> results = executor.invokeAll(tasks);
            for (final Future<Optional<Method>> result : results) {
                assertThat(result.get()).isPresent();
            }
        } finally {
            executor.shutdownNow();
        }
    }

    private static class UninitialisedBean {

        public String getName() {
            return "uninitialised";
        }

    }

    private static class ConcurrentBean {

        public String getName() {
            return "concurrent";
        }

    }

}
