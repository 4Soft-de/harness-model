package com.foursoft.harness.vec.rdf.changes.diff.keys;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Statement;

public record PropertyValueKey(Property predicate, RdfNodeKey<?> object) {

    public static PropertyValueKey from(Statement statement) {
        RdfNodeKey<?> objectKey = (RdfNodeKey<?>) statement.getObject()
                .visitWith(new KeyFactory());
        return new PropertyValueKey(statement.getPredicate(), objectKey);
    }
}
