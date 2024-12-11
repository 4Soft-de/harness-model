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
    void applyPatch() throws IOException {
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

    void handleChangeSet(Resource changeSet, VecModelWrapper wrapper) {
        //TODO: This should not be in a Test Case
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
