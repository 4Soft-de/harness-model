/*-
 * ========================LICENSE_START=================================
 * VEC RDF Changesets (Experimental)
 * %%
 * Copyright (C) 2024 4Soft GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
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
