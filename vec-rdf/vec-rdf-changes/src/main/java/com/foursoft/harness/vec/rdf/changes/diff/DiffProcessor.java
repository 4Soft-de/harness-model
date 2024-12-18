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

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;

public class DiffProcessor {

    private final Model before;
    private final Model after;

    public DiffProcessor(final Model before, final Model after) {
        this.before = before;
        this.after = after;
    }

    public Model diff(final String changeSetNameSpace, final String changeSetLocalName) {
        final ChangeSet diff = new ChangeSet(changeSetNameSpace, changeSetLocalName);

        createDiffForIdentifiableTriples(diff);

        new BNodeDiffer(before, after).diff(diff);

        return diff.getChangeSetModel();
    }

    private void createDiffForIdentifiableTriples(final ChangeSet diff) {
        final Model namedBefore = identifiableTriples(before);
        final Model namedAfter = identifiableTriples(after);

        diff.addAdded(namedAfter.difference(namedBefore));
        diff.addRemoved(namedBefore.difference(namedAfter));
    }

    private Model identifiableTriples(final Model model) {
        final Model result = ModelFactory.createDefaultModel();
        model.listStatements()
                .filterKeep(this::hasNoAnonymousNode)
                .forEach(result::add);
        return result;
    }

    private boolean hasNoAnonymousNode(final Statement statement) {
        return !(statement.getSubject()
                .isAnon() || statement.getPredicate()
                .isAnon() || statement.getObject()
                .isAnon());
    }
}
