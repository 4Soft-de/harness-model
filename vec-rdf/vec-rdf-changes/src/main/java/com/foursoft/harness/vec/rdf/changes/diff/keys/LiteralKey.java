package com.foursoft.harness.vec.rdf.changes.diff.keys;

import org.apache.jena.rdf.model.Literal;

public record LiteralKey(Literal literal) implements RdfNodeKey<Literal> {

    @Override
    public Literal getValue() {
        return literal;
    }
}
