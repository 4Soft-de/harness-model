/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.2.X To VEC 2.0.X
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
package com.foursoft.harness.compatibility.vec12to20.perf;

import com.foursoft.harness.compatibility.vec12to20.util.InitializeFields;
import com.foursoft.harness.compatibility.vec12to20.util.Vec20XReader;
import com.foursoft.harness.vec.v12x.VecReader;
import com.foursoft.harness.vec.v2x.VecContent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Manual benchmark of {@link InitializeFields} on the VEC 1.2.X -&gt; VEC 2.X.X upgrade.
 * <p>
 * Complements {@code ConversionBenchmarkTest} in {@code compatibility-vec11to12}, which measures a whole
 * conversion. This one isolates the field initialization and additionally measures calling it a second time
 * on an already initialized tree, which is what client code does that initializes before every write.
 * <p>
 * Skipped unless it is asked for explicitly:
 * <pre>./mvnw -pl compatibility/compatibility-vec12to20 test -Dtest=InitializeFieldsBenchmarkTest -Dbenchmark=true</pre>
 * Read the numbers from stdout. The wall clock timings fluctuate, so compare the best run of several; the
 * first run additionally contains the class loading and JIT warm-up.
 */
@EnabledIfSystemProperty(named = "benchmark", matches = "true")
class InitializeFieldsBenchmarkTest {

    private static final int ITERATIONS = 10;
    private static final String SAMPLE_FILE = "/vec12x/oldbeetle_vec12x.vec";

    @Test
    void benchmarkInitializeFields() {
        // There is nothing to assert here - the point of the run are the numbers on stdout. Asserting that it
        // completes at all keeps it an intentional smoke test rather than a forgotten assertion.
        assertThatCode(() -> {
            final com.foursoft.harness.vec.v12x.VecContent source = readPlainVec12x();
            for (int i = 0; i < ITERATIONS; i++) {
                runIteration(source, i);
            }
        }).doesNotThrowAnyException();
    }

    private void runIteration(final com.foursoft.harness.vec.v12x.VecContent source, final int iteration) {
        final VecContent upgraded = Vec20XReader.createVec20x(source, "benchmark");

        final long allocatedBefore = allocatedBytes();
        final long startFresh = System.nanoTime();
        InitializeFields.initializeFields(upgraded);
        final long endFresh = System.nanoTime();
        final long allocatedFresh = allocatedBytes() - allocatedBefore;

        final long startRepeat = System.nanoTime();
        InitializeFields.initializeFields(upgraded);
        final long endRepeat = System.nanoTime();

        System.out.printf("run %d: initFields fresh %6d ms | initFields repeat %6d ms | allocated %6d MB%n",
                          iteration,
                          millis(startFresh, endFresh), millis(startRepeat, endRepeat),
                          allocatedFresh / (1024 * 1024));
    }

    private long allocatedBytes() {
        try {
            final Object threadBean = ManagementFactory.getThreadMXBean();
            final Method method = Class.forName("com.sun.management.ThreadMXBean")
                    .getMethod("getCurrentThreadAllocatedBytes");
            return (long) method.invoke(threadBean);
        } catch (final ReflectiveOperationException e) {
            return 0L;
        }
    }

    private long millis(final long from, final long to) {
        return (to - from) / 1_000_000;
    }

    /**
     * Reads the sample file as a plain VEC 1.2.X model, i.e. without any proxies - the starting point an
     * upgrade has.
     * <p>
     * Reads the resource itself rather than going through {@code TestFiles}: this class sits in a package
     * the module descriptor does not declare, {@code TestFiles} sits in one it does. A launcher that does
     * not patch the test classes into the module can load this class but not that one.
     */
    private com.foursoft.harness.vec.v12x.VecContent readPlainVec12x() throws Exception {
        try (final InputStream inputStream = getClass().getResourceAsStream(SAMPLE_FILE)) {
            Objects.requireNonNull(inputStream, "Couldn't get resource " + SAMPLE_FILE);
            return new VecReader().read(new BufferedInputStream(inputStream));
        }
    }

}
