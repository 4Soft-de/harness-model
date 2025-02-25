package com.foursoft.harness.kbl2vec.core;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import java.util.List;
import java.util.Objects;

public class EntityMapping {

    Multimap<Object, Object> mappedElements = MultimapBuilder.hashKeys()
            .arrayListValues()
            .build();

    public void put(final Object sourceEntity, final Object destinationEntity) {
        Objects.requireNonNull(sourceEntity, "sourceEntity must not be null");
        Objects.requireNonNull(destinationEntity, "destinationEntity must not be null");
        mappedElements.put(sourceEntity, destinationEntity);

    }

    /**
     * Returns an element from the cache, matching the {@code destinationClass} criteria.
     * <p>
     * If nothing is found, or if more than one element is found, the method throws an exception.
     */
    public <D> D getIfUniqueOrElseThrow(final Object sourceEntity, final Class<D> destinationClass) {

        final List<D> result = mappedElements.get(sourceEntity)
                .stream()
                .filter(destinationClass::isInstance)
                .map(destinationClass::cast)
                .toList();

        if (result.isEmpty()) {
            throw new ConversionException(
                    String.format("No transformation result found for source: '%1s' and destination type: '%2s'",
                                  sourceEntity, destinationClass.getSimpleName()));
        }
        if (result.size() > 1) {
            throw new ConversionException(String.format(
                    "Expected exactly one transformation result for source: '%1s' and destination type: '%2s', but " +
                            "found: %3s",
                    sourceEntity, destinationClass.getSimpleName(), result.size()));
        }

        return result.get(0);
    }

}
