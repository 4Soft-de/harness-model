/*-
 * ========================LICENSE_START=================================
 * vec-v2x
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
package com.foursoft.harness.vec.v2x.visitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Variant of {@link DefaultXmlIdGenerator} that keeps a separate counter per prefix instead of a single
 * counter shared across all bean types. Inserting a bean of one type therefore no longer shifts the
 * generated ids of unrelated types, keeping ids stable across edits that only add or remove elements.
 */
public class PrefixScopedXmlIdGenerator extends DefaultXmlIdGenerator {

    private final Map<String, AtomicInteger> countersByPrefix = new ConcurrentHashMap<>();

    @Override
    protected String generateNewXmlId(final String prefix) {
        final int value = countersByPrefix.computeIfAbsent(prefix, unused -> new AtomicInteger(0))
                .getAndIncrement();
        return String.format("%s%05d", prefix, value);
    }
}
