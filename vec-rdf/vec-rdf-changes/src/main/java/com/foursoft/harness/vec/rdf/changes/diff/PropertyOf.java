package com.foursoft.harness.vec.rdf.changes.diff;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

public record PropertyOf(Resource subject, Property property) {

    public static PropertyOf from(Statement statement) {
        return new PropertyOf(statement.getSubject(), statement.getPredicate());
    }

}
