package de.foursoft.harness.xml;

import java.util.Map;
import java.util.Optional;

/**
 * Provides the possibility to lookup elements by their technical (xml) id. The
 * {@link IdLookupProvider} is normally created during the unmarshalling process
 * by an {@link ExtendedUnmarshaller}.
 * 
 * @author becker
 *
 * @param <I>
 *            Type of the superclass if elements that are identifiable (have an
 *            id).
 */
public class IdLookupProvider<I> {

    private final Map<String, I> idLookup;

    public IdLookupProvider(final Map<String, I> idLookup) {
        this.idLookup = idLookup;
    }

    public <T extends I> Optional<T> findById(final Class<T> clazz, final String id) {
        final I i = idLookup.get(id);
        return clazz.isInstance(i) ? (Optional<T>)Optional.of(i) : Optional.empty();
    }

    /**
     * Merges this {@link IdLookupProvider} with <tt>toMerge</tt> and returns
     * <tt>this</tt>. Duplicate id mappings are removed, the mappings in
     * <tt>toMerge</tt> are kept.
     * 
     * @param toJoin
     * @return
     */
    public IdLookupProvider<I> merge(final IdLookupProvider<I> toMerge) {
        idLookup.putAll(toMerge.idLookup);
        return this;
    }

}
