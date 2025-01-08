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
    void diff() throws IOException {
        final Model m1 = ModelFactory.createDefaultModel();
        final Model m2 = ModelFactory.createDefaultModel();

        RDFDataMgr.read(m1, loadResourceFromClasspath(VEC_BEFORE), Lang.TTL);
        RDFDataMgr.read(m2, loadResourceFromClasspath(VEC_AFTER), Lang.TTL);

        final DiffProcessor processor = new DiffProcessor(m1, m2);
        final Model diff = processor.diff("https://www.acme.inc/changes#",
                                          "CR-1243");

        diff.write(new FileOutputStream("target/diff-result.ttl"), "TURTLE");

    }
}
