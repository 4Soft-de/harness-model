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

import com.foursoft.harness.vec.rdf.changes.VECCS;
import com.foursoft.harness.vec.rdf.changes.VecPrefixMapping;
import org.apache.commons.lang3.stream.Streams;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import java.io.OutputStream;

public class DiffResult {

    //TODO: Refactor Namespace Handling
    public static final String CHANGESET_URI = "https://www.acme.inc/conversion/test/diff-test#";

    private final Model added = ModelFactory.createDefaultModel();
    private final Model removed = ModelFactory.createDefaultModel();

    public DiffResult() {
        added.setNsPrefixes(VecPrefixMapping.VecStandard);
        removed.setNsPrefixes(VecPrefixMapping.VecStandard);
    }

    public void add(DiffResult other) {
        added.add(other.added);
        removed.add(other.removed);
    }

    public Model getAdded() {
        return added;
    }

    public Model getRemoved() {
        return removed;
    }

    public void write(OutputStream out) {
        Model changeSet = ModelFactory.createDefaultModel();
        changeSet.setNsPrefixes(VecPrefixMapping.VecStandard);

        changeSet.add(added);
        changeSet.add(removed);

        Resource cs = changeSet.createResource(CHANGESET_URI + "MyChangeSet");

        changeSet.add(cs, RDF.type, VECCS.ChangeSet);
        changeSet.add(cs, VECCS.identification, "ISSUE-1278");

        Streams.of(added.listStatements())
                .filter(s -> !s.getSubject()
                        .isAnon())
                .forEach(s -> {
                    Resource statementResource = changeSet.createResource(s);

                    changeSet.add(cs, VECCS.added, statementResource);
                });
        Streams.of(removed.listStatements())
                .filter(s -> !s.getSubject()
                        .isAnon())
                .forEach(s -> {
                    Resource statementResource = changeSet.createResource(s);

                    changeSet.add(cs, VECCS.removed, statementResource);
                });

        changeSet.write(out, "TURTLE");
    }
}
