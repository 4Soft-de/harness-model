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

import com.foursoft.harness.vec.rdf.changes.diff.keys.KeyFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;

public class DiffProcessor {

    private final Model before;
    private final Model after;

    public DiffProcessor(Model before, Model after) {
        this.before = before;
        this.after = after;
    }

    public DiffResult diff() {
        DiffResult diff = new DiffResult();

        createDiffForIdentifiableTriples(diff);

        DiffResult bNodeDiff = new BNodeDiffer(before, after).diff();

        diff.add(bNodeDiff);

        before.listStatements()
                .forEach(s -> s.getObject()
                        .visitWith(new KeyFactory()));

        //TODO: If Diff contains Custom Open Enums, those must be added as well.

        //        beforeBNodes.forEach((key, value) -> {
        //            if (!afterBNodes.containsKey(key)) {
        //                value.forEach(bnode -> {
        //                    diff.getRemoved()
        //                            .add(key.subject(), key.property(), bnode);
        //                    diff.getRemoved()
        //                            .add(Closure.closure(bnode.asResource(), true));
        //                });
        //                System.out.println("BNode Key not found in dest:" + key);
        //            }
        //        });

        return diff;
    }

    private void createDiffForIdentifiableTriples(DiffResult diff) {
        Model namedBefore = identifiableTriples(before);
        Model namedAfter = identifiableTriples(after);

        diff.getAdded()
                .add(namedAfter.difference(namedBefore));
        diff.getRemoved()
                .add(namedBefore.difference(namedAfter));
    }

    private Model identifiableTriples(Model model) {
        Model result = ModelFactory.createDefaultModel();
        model.listStatements()
                .filterKeep(this::hasNoAnonymousNode)
                .forEach(result::add);
        return result;
    }

    private boolean hasNoAnonymousNode(Statement statement) {
        return !(statement.getSubject()
                .isAnon() || statement.getPredicate()
                .isAnon() || statement.getObject()
                .isAnon());
    }
}
