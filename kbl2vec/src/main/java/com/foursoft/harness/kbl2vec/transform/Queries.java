/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.Query;

import java.util.Arrays;
import java.util.List;

public final class Queries {

    private Queries() {
        throw new AssertionError("Should not be instantiated");
    }

    public static Query<KblPart> allParts(final KBLContainer container) {
        return () -> concat(container.getParts(), List.of(container.getHarness()), container.getHarness()
                .getModules());
    }

    private static <T> List<T> concat(final List<? extends T>... lists) {
        return Arrays.stream(lists)
                .flatMap(List::stream)
                .map(e -> (T) e)
                .toList();
    }

    public static Query<ConnectionOrOccurrence> partOccurrences(final KblModuleConfiguration source) {
        return () -> source.getControlledComponents()
                .stream()
                .filter(c -> !(c instanceof KblConnection))
                .toList();
    }

}
