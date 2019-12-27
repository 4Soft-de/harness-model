package de.foursoft.harness.xml.postprocessing;

import java.util.HashMap;
import java.util.function.Function;

import de.foursoft.harness.xml.IdLookupProvider;

/**
 * {@link ModelPostProcessor} to generate a {@link IdLookupProvider} during the
 * unmarshalling Process.
 * 
 * @author becker
 *
 * @param <I>
 *            Type of the identifiable elements.
 */
public class IdLookupGeneratorPostProcessor<I> implements ModelPostProcessor {

    private final Class<I> identifiableElements;
    private final Function<I, String> idMapper;

    private int objectCount = 0;
    private HashMap<String, I> idLookup;

    public IdLookupGeneratorPostProcessor(final Class<I> identifiableElements, final Function<I, String> idMapper) {
        this.identifiableElements = identifiableElements;
        this.idMapper = idMapper;
    }

    @Override
    public Class<?> getClassToHandle() {
        return identifiableElements;
    }

    @Override
    public void afterUnmarshalling(final Object target, final Object parent) {
        objectCount++;
    }

    @Override
    public void afterUnmarshallingCompleted(final Object target) {
        // No type check for performance reasons. The ModelPostProcessorRegistry
        // ensures that this handler is only called correctly.
        if (idLookup == null) {
            idLookup = new HashMap<>(objectCount);
        }
        final I element = identifiableElements.cast(target);
        idLookup.put(idMapper.apply(element), element);
    }

    @Override
    public void clearState() {
        objectCount = 0;
        idLookup = null;
    }

    /**
     * Creates a new {@link IdLookupProvider} based on the current state of the
     * post processor.
     * 
     * @return
     */
    public IdLookupProvider<I> createIdLookkup() {
        return new IdLookupProvider<>(idLookup);
    }

}
