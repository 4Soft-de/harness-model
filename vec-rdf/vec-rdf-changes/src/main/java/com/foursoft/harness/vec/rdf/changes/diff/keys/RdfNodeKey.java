package com.foursoft.harness.vec.rdf.changes.diff.keys;

import org.apache.jena.rdf.model.RDFNode;

public interface RdfNodeKey<T extends RDFNode> {

    T getValue();
}
