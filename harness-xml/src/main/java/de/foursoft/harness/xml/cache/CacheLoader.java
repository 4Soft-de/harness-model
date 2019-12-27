package de.foursoft.harness.xml.cache;

@FunctionalInterface
public interface CacheLoader<K, V> {

    V load(K key);

}
