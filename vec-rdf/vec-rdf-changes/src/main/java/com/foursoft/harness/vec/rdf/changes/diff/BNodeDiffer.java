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

import com.foursoft.harness.vec.rdf.changes.equivalences.rdf.StatementEquivalence;
import com.google.common.base.Equivalence;
import org.apache.commons.lang3.stream.Streams;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BNodeDiffer {

    private final Set<Equivalence.Wrapper<Statement>> beforeBNodeStatement;
    private final Set<Equivalence.Wrapper<Statement>> afterBNodeStatement;

    public BNodeDiffer(final Model before, final Model after) {
        beforeBNodeStatement = bNodeRoots(before);
        afterBNodeStatement = bNodeRoots(after);

    }

    public void diff(final ChangeSet changeSet) {

        final Set<Equivalence.Wrapper<Statement>> allStatements = new HashSet<>();
        allStatements.addAll(beforeBNodeStatement);
        allStatements.addAll(afterBNodeStatement);

        for (final Equivalence.Wrapper<Statement> wrappedStmt : allStatements) {
            if (!beforeBNodeStatement.contains(wrappedStmt)) {
                final Statement stmt = wrappedStmt.get();
                changeSet.addAdded(stmt);

            }
            if (!afterBNodeStatement.contains(wrappedStmt)) {
                final Statement stmt = wrappedStmt.get();
                changeSet.addRemoved(stmt);
            }
        }

    }

    private Set<Equivalence.Wrapper<Statement>> bNodeRoots(final Model model) {
        return Streams.of(model.listStatements())
                .filter(this::isIdentifiableWithBNodeObject)
                .map(StatementEquivalence.statementEquals()::wrap)
                .collect(Collectors.toSet());
    }

    private boolean isIdentifiableWithBNodeObject(final Statement statement) {
        return !statement.getSubject()
                .isAnon() && !statement.getPredicate()
                .isAnon() && statement.getObject()
                .isAnon();
    }
}
