package com.foursoft.harness.vec.rdf.changes.diff.keys;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

public record StatementKey(Resource subject, Property predicate, RdfNodeKey<?> object) {

    public static StatementKey from(Statement statement) {
        RdfNodeKey<?> objectKey = (RdfNodeKey<?>) statement.getObject()
                .visitWith(new KeyFactory());
        return new StatementKey(statement.getSubject(), statement.getPredicate(), objectKey);
    }
}
