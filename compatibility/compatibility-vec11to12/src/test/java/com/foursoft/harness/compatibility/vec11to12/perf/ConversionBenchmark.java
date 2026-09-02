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
package com.foursoft.harness.compatibility.vec11to12.perf;

import com.foursoft.harness.compatibility.vec11to12.TestFiles;
import com.foursoft.harness.compatibility.vec11to12.util.InitializeFields;
import com.foursoft.harness.compatibility.vec11to12.util.Vec1XXReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Manual benchmark of the VEC 1.2.X -&gt; VEC 1.1.3 conversion, i.e. the direction a container export uses.
 * <p>
 * This is not an assertion-based test but a measuring aid for work on the compatibility layer. It is skipped
 * unless it is asked for explicitly:
 * <pre>./mvnw -pl compatibility/compatibility-vec11to12 test -Dtest=ConversionBenchmark -Dbenchmark=true</pre>
 * Read the numbers from stdout. {@code allocated} is the most reliable of them, the wall clock timings
 * fluctuate heavily; the first run additionally contains the class loading and JIT warm-up.
 */
@EnabledIfSystemProperty(named = "benchmark", matches = "true")
class ConversionBenchmark {

    private static final int ITERATIONS = 10;

    @Test
    void benchmarkDowngrade() throws Exception {
        final com.foursoft.harness.vec.v12x.VecContent source = readPlainVec12x();

        for (int i = 0; i < ITERATIONS; i++) {
            runIteration(source, i);
        }
    }

    private void runIteration(final com.foursoft.harness.vec.v12x.VecContent source, final int iteration) {
        final long allocatedBefore = allocatedBytes();
        final long startConvert = System.nanoTime();
        final com.foursoft.harness.vec.v113.VecContent converted = Vec1XXReader.createVec11x(source, "benchmark");
        final long startInit = System.nanoTime();
        InitializeFields.initializeFields(converted);
        final long startWrite = System.nanoTime();
        final String xml = new com.foursoft.harness.vec.v113.VecWriter().writeToString(converted);
        final long end = System.nanoTime();

        final long allocated = allocatedBytes() - allocatedBefore;
        final long heap = usedHeap();
        System.out.printf("run %d: convert %6d ms | initFields %6d ms | write %6d ms | total %6d ms "
                                  + "| heap %5d MB | allocated %6d MB | %d chars%n",
                          iteration,
                          millis(startConvert, startInit), millis(startInit, startWrite),
                          millis(startWrite, end), millis(startConvert, end),
                          heap / (1024 * 1024), allocated / (1024 * 1024), xml.length());
    }

    private long allocatedBytes() {
        try {
            final Object threadBean = java.lang.management.ManagementFactory.getThreadMXBean();
            final java.lang.reflect.Method method =
                    Class.forName("com.sun.management.ThreadMXBean")
                            .getMethod("getCurrentThreadAllocatedBytes");
            return (long) method.invoke(threadBean);
        } catch (final ReflectiveOperationException e) {
            return 0L;
        }
    }

    private long millis(final long from, final long to) {
        return (to - from) / 1_000_000;
    }

    private long usedHeap() {
        final Runtime runtime = Runtime.getRuntime();
        for (int i = 0; i < 3; i++) {
            System.gc();
        }
        return runtime.totalMemory() - runtime.freeMemory();
    }

    /**
     * Reads the sample file, upgrades it to 1.2.X and serialises it, so that re-reading yields a plain
     * VEC 1.2.X model without any proxies - the starting point a container export has.
     */
    private com.foursoft.harness.vec.v12x.VecContent readPlainVec12x() throws Exception {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            final com.foursoft.harness.vec.v12x.VecContent proxied =
                    Vec1XXReader.createVec12x(inputStream, "setup");
            InitializeFields.initializeFields(proxied);
            final String xml = new com.foursoft.harness.vec.v12x.VecWriter().writeToString(proxied);
            try (final InputStream plain = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
                return new com.foursoft.harness.vec.v12x.VecReader().read(plain);
            }
        }
    }

}
