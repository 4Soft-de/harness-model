package com.foursoft.harness.vec.rdf.changes.test;

import com.foursoft.harness.vec.rdf.common.VEC;
import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;
import org.apache.commons.lang3.stream.Streams;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.InputStream;
import java.io.StringReader;

public class TestUtils {
    private static final String PROLOGUE = """
            PREFIX : <https://www.foursoft.com/test#>
            PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
            PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#>
            PREFIX vec:     <http://www.prostep.org/ontologies/ecad/2024/03/vec#>
            PREFIX vec-dbg: <http://www.prostep.org/ontologies/ecad/2024/03/vec-debug#>
            PREFIX xsd:     <http://www.w3.org/2001/XMLSchema#>
            
            """;

    public static Model loadModel(String turtleString) {
        Model m1 = ModelFactory.createDefaultModel();
        RDFDataMgr.read(m1, new StringReader(PROLOGUE + turtleString), "https://www.foursoft.com/test", Lang.TTL);

        return m1;
    }

    public static Resource findNodeWithXmlId(Model model, String xmlId) {
        return Streams.of(model.listResourcesWithProperty(model.createProperty(VEC.DEBUG_NS, "id"), xmlId))
                .findFirst()
                .orElseThrow();
    }

    public static InputStream loadResourceFromClasspath(String filename) {
        final InputStream resourceAsStream = TestUtils.class.getResourceAsStream(filename);

        if (resourceAsStream == null) {
            throw new VecRdfException("Resource not found: " + filename);
        }
        return resourceAsStream;
    }
}

