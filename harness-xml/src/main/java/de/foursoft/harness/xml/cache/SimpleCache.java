package de.foursoft.harness.xml.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple Cache, loading the value for a key via a {@link CacheLoader}. Caching
 * the results in a {@link HashMap}. For each key, the {@link CacheLoader} is
 * only called once.
 * 
 * @author becker
 *
 * @param <K>
 * @param <V>
 */
public class SimpleCache<K, V> {

    private final Map<K, V> cachedResults = new HashMap<>();
    private final CacheLoader<K, V> cacheLoader;

    public SimpleCache(final CacheLoader<K, V> cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    public V get(final K t) {
        if (cachedResults.containsKey(t)) {
            return cachedResults.get(t);
        }
        final V result = cacheLoader.load(t);
        cachedResults.put(t, result);
        return result;
    }

    /**
     * All loaded values that are currently initialized in this cache.
     * 
     * @return
     */
    public Collection<V> getAllLoadedValues() {
        return cachedResults.values();
    }

}
