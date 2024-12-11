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
