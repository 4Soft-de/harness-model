package com.foursoft.harness.vec.rdf.changes.diff;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;

import static com.foursoft.harness.vec.rdf.changes.test.TestUtils.loadResourceFromClasspath;

public class DiffTest {

    public static final String VEC_BEFORE = "/fixtures/mini-change/VEC-Change-Example I.ttl";
    public static final String VEC_AFTER = "/fixtures/mini-change/VEC-Change-Example II.ttl";

    /**
     * This is an experimental Test an has nothing to with VEC2RDF Conversion or Validation.
     * <p>
     * This is protoptyping for a potential Diff-Extension.
     *
     * @throws IOException
     */
    @Test
    public void diff() throws IOException {
        Model m1 = ModelFactory.createDefaultModel();
        Model m2 = ModelFactory.createDefaultModel();

        RDFDataMgr.read(m1, loadResourceFromClasspath(VEC_BEFORE), Lang.TTL);
        RDFDataMgr.read(m2, loadResourceFromClasspath(VEC_AFTER), Lang.TTL);

        if (m1.isIsomorphicWith(m2)) {
            System.out.println("SAME!");
        }

        DiffResult r = new DiffResult();

        DiffProcessor processor = new DiffProcessor(m1, m2);
        DiffResult diff = processor.diff();

        diff.write(new FileOutputStream("changeset.ttl"));
    }
}
