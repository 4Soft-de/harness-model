package com.foursoft.harness.vec.aas;

import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.rdf.common.meta.VecClass;
import com.foursoft.harness.vec.rdf.common.meta.VecField;
import org.eclipse.digitaltwin.aas4j.v3.model.Key;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultReference;

import java.util.*;

import static com.foursoft.harness.vec.rdf.common.meta.VecClass.analyzeClass;

public class ReferenceFactory {

    private final Map<Identifiable, List<String>> references = new HashMap<>();
    private final AasNamingStrategy namingStrategy;
    private final String targetNamespace;

    public ReferenceFactory(final AasNamingStrategy namingStrategy, final String targetNamespace,
                            final Identifiable root) {
        this.namingStrategy = namingStrategy;
        this.targetNamespace = targetNamespace;

        traverseModel(root, new ArrayList<>());

    }

    public Reference localReferenceFor(final Identifiable vecElement) {
        if (!references.containsKey(vecElement)) {
            throw new AasConversionException("Cannot find precalculated reference stack for " + vecElement);
        }
        final List<Key> keys = new ArrayList<>();
        keys.add(new DefaultKey.Builder()
                         .type(KeyTypes.GLOBAL_REFERENCE)
                         .value(targetNamespace)
                         .build());

        references.get(vecElement)
                .stream()
                .map(key -> new DefaultKey.Builder()
                        .type(KeyTypes.FRAGMENT_REFERENCE)
                        .value(key)
                        .build())
                .forEach(keys::add);

        return new DefaultReference.Builder()
                .type(ReferenceTypes.MODEL_REFERENCE)
                .keys(keys)
                .build();
    }

    private void traverseModel(final Identifiable root, final List<String> idShortsContext) {
        final List<String> idShorts = new ArrayList<>(idShortsContext);

        idShorts.add(namingStrategy.idShort(root));
        if (references.containsKey(root)) {
            throw new AasConversionException("Tried to generate a second reference stack for: " + root.getXmlId() +
                                                     ". There is probably an implementation error in the traversing.");
        }
        references.put(root, idShorts);
        final VecClass metaData = analyzeClass(root.getClass());

        for (final VecField field : metaData.getFields()) {
            final List<Identifiable> values = extractIdentifiableContainments(field, root);
            if (!values.isEmpty()) {
                final List<String> idShortsFieldContext = new ArrayList<>(idShorts);
                idShortsFieldContext.add(namingStrategy.idShort(field));
                values.forEach(value -> traverseModel(value, idShortsFieldContext));
            }
        }
    }

    private List<Identifiable> extractIdentifiableContainments(final VecField field, final Identifiable root) {
        if (field.isReference()) {
            return Collections.emptyList();
        }
        final Object value = field.readValue(root);
        if (value instanceof final Identifiable identifiable) {
            return List.of(identifiable);
        }
        if (value instanceof final List<?> list) {
            return list.stream()
                    .filter(Objects::nonNull)
                    .filter(Identifiable.class::isInstance)
                    .map(Identifiable.class::cast)
                    .toList();
        }
        return Collections.emptyList();
    }

}
