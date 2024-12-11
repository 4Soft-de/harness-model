package com.foursoft.harness.vec.rdf.changes.diff.keys;

import org.apache.jena.rdf.model.*;

public class KeyFactory implements RDFVisitor {
    @Override
    public Object visitBlank(Resource r, AnonId id) {
        return BNodeKey.from(r);
    }

    @Override
    public Object visitURI(Resource r, String uri) {
        return new UriKey(r);
    }

    @Override
    public Object visitLiteral(Literal l) {
        return new LiteralKey(l);
    }

    @Override
    public Object visitStmt(Resource r, Statement statement) {
        throw new UnsupportedOperationException("Support for Statements not supported.");
    }
}
