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

