package com.foursoft.harness.vec.rdf.changes.patch;

import org.apache.jena.rdf.model.Statement;

public class MergeConflictException extends RuntimeException {
    private final Statement statement;

    public MergeConflictException(String message, Statement statement) {
        super(message);
        this.statement = statement;
    }

    public MergeConflictException(String message) {
        super(message);
        this.statement = null;
    }

    public MergeConflictException(String message, Throwable cause, Statement statement) {
        super(message, cause);
        this.statement = statement;
    }

    public Statement getStatement() {
        return statement;
    }
}
