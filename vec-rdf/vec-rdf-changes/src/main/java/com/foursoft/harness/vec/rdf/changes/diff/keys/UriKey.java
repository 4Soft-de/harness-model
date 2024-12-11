package com.foursoft.harness.vec.rdf.changes.diff.keys;

import org.apache.jena.rdf.model.Resource;

public record UriKey(Resource resource) implements RdfNodeKey<Resource> {
    @Override
    public Resource getValue() {
        return resource;
    }
}
