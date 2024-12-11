package com.foursoft.harness.vec.rdf.changes.diff.keys;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.stream.Streams;
import org.apache.jena.rdf.model.Resource;

import java.util.Set;
import java.util.stream.Collectors;

public record BNodeKey(Resource bNode, Set<PropertyValueKey> properties) implements RdfNodeKey<Resource> {

    @Override
    public Resource getValue() {
        return bNode;
    }

    public static BNodeKey from(Resource bNode) {
        return new BNodeKey(bNode, Streams.of(bNode.listProperties())
                .map(PropertyValueKey::from)
                .collect(Collectors.toSet()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof BNodeKey bNodeKey))
            return false;

        return new EqualsBuilder().append(properties, bNodeKey.properties)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(properties)
                .toHashCode();
    }
}
