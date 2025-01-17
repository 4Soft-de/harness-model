/*-
 * ========================LICENSE_START=================================
 * NavExt Runtime
 * %%
 * Copyright (C) 2019 - 2023 4Soft GmbH
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
package com.foursoft.harness.navext.runtime.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple Cache, loading the value for a key via a {@link CacheLoader}. Caching
 * the results in a {@link HashMap}. For each key, the {@link CacheLoader} is
 * only called once.
 *
 * @param <K> The type of the key
 * @param <V> The type of the value
 * @author becker
 */
public class SimpleCache<K, V> {

    private final Map<K, V> cachedResults = new HashMap<>();
    private final CacheLoader<K, V> cacheLoader;

    public SimpleCache(final CacheLoader<K, V> cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    /**
     * get the value for a given key
     *
     * @param key the key
     * @return the value, maybe Null
     */
    public V get(final K key) {
        if (cachedResults.containsKey(key)) {
            return cachedResults.get(key);
        }
        final V result = cacheLoader.load(key);
        cachedResults.put(key, result);
        return result;
    }

    /**
     * All loaded values that are currently initialized in this cache.
     *
     * @return returning all values of the cache
     */
    public Collection<V> getAllLoadedValues() {
        return cachedResults.values();
    }

}
