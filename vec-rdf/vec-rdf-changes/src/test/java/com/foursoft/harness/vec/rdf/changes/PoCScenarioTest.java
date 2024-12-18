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

import com.foursoft.harness.vec.rdf.changes.diff.DiffProcessor;
import com.foursoft.harness.vec.rdf.changes.patch.PatchProcessor;
import com.foursoft.harness.vec.rdf.convert.Vec2RdfConverter;
import org.apache.jena.rdf.model.Model;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.foursoft.harness.vec.rdf.changes.test.TestUtils.loadResourceFromClasspath;

public class PoCScenarioTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PoCScenarioTest.class);
    private static final String DATA_NAMESPACE = "https://www.acme.inc/harness/mini-change#";
    private static final String CHANGE_NAMESPACE = "https://www.acme.inc/changes#";
    private static final String OUTPUT_DIR = "target/mini-change/";
    public static final String CHANGE_SET_FILE = OUTPUT_DIR + "changeset.ttl";
    private static final String VEC_STATE_BEFORE = "/fixtures/mini-change/VEC-Change-Example I.xml";
    private static final String VEC_STATE_AFTER = "/fixtures/mini-change/VEC-Change-Example II.xml";

    @Test
    void run_poc_scenario() throws Exception {
        LOGGER.info("Running poc scenario");
        LOGGER.info("---------- Step 1 converting VEC XML to RDF --------------");

        Files.createDirectories(Paths.get(OUTPUT_DIR));
        final Vec2RdfConverter vec2RdfConverter = new Vec2RdfConverter();
        final Model beforeRdfModel = vec2RdfConverter.convert(loadResourceFromClasspath(VEC_STATE_BEFORE),
                                                              DATA_NAMESPACE);
        beforeRdfModel.setNsPrefix("", DATA_NAMESPACE);
        beforeRdfModel.write(new FileOutputStream(OUTPUT_DIR + "before.ttl"), "TURTLE");

        final Model afterRdfModel = vec2RdfConverter.convert(loadResourceFromClasspath(VEC_STATE_AFTER),
                                                             DATA_NAMESPACE);
        afterRdfModel.setNsPrefix("", DATA_NAMESPACE);
        afterRdfModel.write(new FileOutputStream(OUTPUT_DIR + "after.ttl"), "TURTLE");

        LOGGER.info("---------- Step 2 Creating Diff --------------");

        final DiffProcessor processor = new DiffProcessor(beforeRdfModel, afterRdfModel);
        final Model diff = processor.diff(CHANGE_NAMESPACE, "CR-12345");

        diff.setNsPrefix("", DATA_NAMESPACE);
        diff.write(new FileOutputStream(CHANGE_SET_FILE), "TURTLE");

        LOGGER.info("---------- Step 3 Patching Target Model --------------");

        final PatchProcessor patchProcessor = new PatchProcessor(loadResourceFromClasspath(VEC_STATE_BEFORE),
                                                                 DATA_NAMESPACE);

        patchProcessor.applyPatch(new FileInputStream(CHANGE_SET_FILE));

        patchProcessor.write(new FileOutputStream(OUTPUT_DIR + "patched-vec.xml"));

        LOGGER.info("Written complete POC scenario to: {}", OUTPUT_DIR);
    }

}
