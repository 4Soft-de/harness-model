package com.foursoft.harness.vec.rdf.changes.diff;

import com.foursoft.harness.vec.rdf.changes.diff.keys.StatementKey;
import org.apache.commons.lang3.stream.Streams;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.sparql.util.Closure;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BNodeDiffer {

    private final Set<StatementKey> beforeBNodeStatement;
    private final Set<StatementKey> afterBNodeStatement;

    public BNodeDiffer(Model before, Model after) {
        beforeBNodeStatement = bNodeRoots(before);
        afterBNodeStatement = bNodeRoots(after);

    }

    public DiffResult diff() {
        DiffResult diff = new DiffResult();

        Set<StatementKey> allStatements = new HashSet<>();
        allStatements.addAll(beforeBNodeStatement);
        allStatements.addAll(afterBNodeStatement);

        for (StatementKey stmt : allStatements) {
            if (!beforeBNodeStatement.contains(stmt)) {
                diff.getAdded()
                        .add(stmt.subject(), stmt.predicate(), stmt.object()
                                .getValue());
                diff.getAdded()
                        .add(Closure.closure(stmt.object()
                                                     .getValue()
                                                     .asResource(), true));
            }
            if (!afterBNodeStatement.contains(stmt)) {
                diff.getRemoved()
                        .add(stmt.subject(), stmt.predicate(), stmt.object()
                                .getValue());
                diff.getRemoved()
                        .add(Closure.closure(stmt.object()
                                                     .getValue()
                                                     .asResource(), true));
            }
        }

        return diff;
    }

    private Set<StatementKey> bNodeRoots(Model model) {
        return Streams.of(model.listStatements())
                .filter(this::isIdentifiableWithBNodeObject)
                .map(StatementKey::from)
                .collect(Collectors.toSet());
    }

    private boolean isIdentifiableWithBNodeObject(Statement statement) {
        return !statement.getSubject()
                .isAnon() && !statement.getPredicate()
                .isAnon() && statement.getObject()
                .isAnon();
    }
}
