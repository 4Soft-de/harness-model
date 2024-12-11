package com.foursoft.harness.vec.rdf.changes;

import com.foursoft.harness.vec.rdf.changes.patch.VecModelWrapper;
import com.foursoft.harness.vec.rdf.common.NamingStrategy;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.foursoft.harness.vec.rdf.changes.test.TestUtils.loadResourceFromClasspath;

public class PatchTest {

    public static final String VEC_XML_BEFORE = "/fixtures/mini-change/VEC-Change-Example I.xml";
    public static final String CHANGESET_TTL = "/fixtures/mini-change/changeset.ttl";

    @Test
    public void applyPatch() throws IOException {
        Model m1 = ModelFactory.createDefaultModel();

        RDFDataMgr.read(m1, loadResourceFromClasspath(CHANGESET_TTL), Lang.TTL);
        RDFDataMgr.read(m1, loadResourceFromClasspath("/vec/v2.1.0/vec-2.1.0-ontology.ttl"),
                        Lang.TTL);

        VecModelWrapper wrapper = new VecModelWrapper(new NamingStrategy(),
                                                      "https://www.acme.inc/conversion/test/diff-test#");
        wrapper.load(loadResourceFromClasspath(VEC_XML_BEFORE));

        m1.listSubjectsWithProperty(RDF.type, VECCS.ChangeSet)
                .forEach(changeSet -> handleChangeSet(changeSet, wrapper));

        wrapper.write("output.xml");
    }

    public void handleChangeSet(Resource changeSet, VecModelWrapper wrapper) {
        changeSet.listProperties(VECCS.removed)
                .forEach(s -> {
                    Statement removedStatement = s.getObject()
                            .asResource()
                            .getStmtTerm();
                    wrapper.removeValue(removedStatement);
                });
        changeSet.listProperties(VECCS.added)
                .forEach(s -> {
                    Statement addedStatement = s.getObject()
                            .asResource()
                            .getStmtTerm();
                    wrapper.addValue(addedStatement);
                });

    }

}
