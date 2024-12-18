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

import com.foursoft.harness.vec.rdf.changes.VECCS;
import com.foursoft.harness.vec.rdf.common.NamingStrategy;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

public class PatchProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatchProcessor.class);
    private final VecModelWrapper modelWrapper;

    /**
     * Creates a {@link PatchProcessor} to apply ChangeSets to existing VEC data. The VEC data is
     * loaded from an XML source. To apply RDF based changes to VEC data, VEC elements must be
     * identified with URIs. As most XML based VEC data contains no URIs the namespace for generated
     * URIs must be defined.
     *
     * @param targetModel
     * @param dataNamespace
     */
    public PatchProcessor(final InputStream targetModel, final String dataNamespace) {
        modelWrapper = new VecModelWrapper(new NamingStrategy(), dataNamespace);
        modelWrapper.load(targetModel);
    }

    public void applyPatch(final InputStream changeSetIS) {
        final Model changeSet = ModelFactory.createDefaultModel();
        RDFDataMgr.read(changeSet, changeSetIS, Lang.TURTLE);
        RDFDataMgr.read(changeSet, modelWrapper.getVersion().getOntologyInputStream(), Lang.TURTLE);

        changeSet.listSubjectsWithProperty(RDF.type, VECCS.ChangeSet)
                .forEach(cs -> handleChangeSet(cs, this.modelWrapper));
    }

    /**
     * Write the patched VEC model into a {@link OutputStream}
     *
     * @param targetModel
     */
    public void write(final OutputStream targetModel) {
        modelWrapper.write(targetModel);
    }

    void handleChangeSet(final Resource changeSet, final VecModelWrapper wrapper) {
        changeSet.listProperties(VECCS.removed)
                .forEach(reificationHandler(wrapper::removeValue));
        changeSet.listProperties(VECCS.added)
                .forEach(reificationHandler(wrapper::addValue));
    }

    static Consumer<Statement> reificationHandler(final Consumer<Statement> reifiedStatementConsumer) {
        return s -> {
            final RDFNode object = s.getObject();
            if (!object.isStmtResource()) {
                LOGGER.warn("Expected a reified statement resource but got a non-statement resource: {}", object);
            } else {
                reifiedStatementConsumer.accept(object.asResource().getStmtTerm());
            }
        };
    }

}
