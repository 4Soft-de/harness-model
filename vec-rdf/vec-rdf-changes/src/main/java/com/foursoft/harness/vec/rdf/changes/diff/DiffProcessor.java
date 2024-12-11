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
