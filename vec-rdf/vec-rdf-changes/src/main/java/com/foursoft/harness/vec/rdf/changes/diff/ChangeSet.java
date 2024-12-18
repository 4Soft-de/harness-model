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

import com.foursoft.harness.vec.rdf.changes.VECCS;
import com.foursoft.harness.vec.rdf.changes.VecPrefixMapping;
import com.foursoft.harness.vec.rdf.common.VEC;
import com.foursoft.harness.vec.rdf.common.VecVersion;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;

class ChangeSet {

    private final Model changeSetModel = ModelFactory.createDefaultModel();
    private final Model vecOntology;

    private final String changeSetNameSpace;
    private final String changeSetLocalName;
    private Resource changeSetResource;

    public ChangeSet(final String changeSetNameSpace, final String changeSetLocalName) {
        this.changeSetNameSpace = changeSetNameSpace;
        this.changeSetLocalName = changeSetLocalName;
        initializeChangeSet();

        this.vecOntology = ModelFactory.createDefaultModel();
        // If we would be completely correct (and not a PoC) we would the ontology version matching the VEC version
        // used for CS creation.
        RDFDataMgr.read(vecOntology, VecVersion.LATEST.getOntologyInputStream(), Lang.TURTLE);
    }

    private void initializeChangeSet() {
        changeSetModel.setNsPrefixes(VecPrefixMapping.VecStandard);
        changeSetModel.setNsPrefix("cr", changeSetNameSpace);

        changeSetResource = changeSetModel.createResource(changeSetNameSpace + changeSetLocalName);

        changeSetModel.add(changeSetResource, RDF.type, VECCS.ChangeSet);
        changeSetModel.add(changeSetResource, VECCS.identification, changeSetLocalName);
    }

    public void addAdded(final Model model) {
        model.listStatements().forEach(this::addAdded);
    }

    public void addRemoved(final Model model) {
        model.listStatements().forEach(this::addRemoved);
    }

    public void addAdded(final Statement statement) {
        addStatement(statement, VECCS.added);
    }

    public void addRemoved(final Statement statement) {
        addStatement(statement, VECCS.removed);
    }

    private void addStatement(final Statement statement, final Property changeSetProperty) {
        addClosureContent(statement);
        final Resource statementResource = changeSetModel.createResource(statement);
        changeSetModel.add(changeSetResource, changeSetProperty, statementResource);
    }

    private void addClosureContent(final Statement statement) {
        changeSetModel.add(statement);

        // Handling of BNodes. Must be embedded completely.
        if (statement.getObject().isAnon()) {
            statement.getResource().listProperties().forEach(this::addClosureContent);
        } else if (statement.getObject().isURIResource()) {
            final Resource object = statement.getResource();
            // Handling of OpenEnums that are not part of the VEC ontology. Must be serialized in CS as well.
            if (isCustomOpenEnumLiteral(object)) {
                object.listProperties().forEach(this::addClosureContent);
            }
        }
    }

    public Model getChangeSetModel() {
        return changeSetModel;
    }

    private boolean isCustomOpenEnumLiteral(final Resource resource) {
        final Statement typeStmt = resource.getProperty(RDF.type);
        if (typeStmt == null || !typeStmt.getObject().isURIResource()) {
            return false;
        }
        final Resource typeResource = vecOntology.getResource(typeStmt.getResource().getURI());
        if (typeResource == null) {
            return false;
        }
        if (!VEC.isSubclassOf(typeResource, VEC.OpenEnumeration)) {
            return false;
        }
        return !VEC.URI.equals(resource.getNameSpace());
    }

}
