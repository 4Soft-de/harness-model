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
package com.foursoft.harness.vec.rdf.changes.patch;

import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;

import static com.foursoft.harness.vec.rdf.changes.test.TestUtils.loadResourceFromClasspath;

public class PatchTest {

    public static final String VEC_XML_BEFORE = "/fixtures/mini-change/VEC-Change-Example I.xml";
    public static final String CHANGESET_TTL = "/fixtures/mini-change/changeset.ttl";

    @Test
    void applyPatch() throws IOException {
        final PatchProcessor patchProcessor = new PatchProcessor(loadResourceFromClasspath(VEC_XML_BEFORE),
                                                                 "https://www.acme.inc/conversion/test/diff-test#");

        patchProcessor.applyPatch(loadResourceFromClasspath(CHANGESET_TTL));

        patchProcessor.write(new FileOutputStream("target/output.xml"));

    }

}
