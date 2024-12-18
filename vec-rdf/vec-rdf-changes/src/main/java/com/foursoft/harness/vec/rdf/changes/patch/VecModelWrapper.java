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

import com.foursoft.harness.navext.runtime.io.read.XMLReader;
import com.foursoft.harness.navext.runtime.io.utils.ValidationEventLogger;
import com.foursoft.harness.navext.runtime.io.write.XMLWriter;
import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.rdf.common.NamingStrategy;
import com.foursoft.harness.vec.rdf.common.VecVersion;
import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;
import com.foursoft.harness.vec.rdf.common.util.VecXmlApiUtils;
import com.foursoft.harness.vec.rdf.common.util.XmlIdGenerator;
import jakarta.xml.bind.Marshaller;
import org.apache.jena.rdf.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.foursoft.harness.vec.rdf.common.util.VecXmlApiUtils.genericVecModelTraversal;

public class VecModelWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(VecModelWrapper.class);

    private final NamingStrategy namingStrategy;
    private final String namespace;
    private final Map<String, Identifiable> uriToIdentifiable = new HashMap<>();
    private final Map<Identifiable, VecObjectWrapper> wrapperObjects = new HashMap<>();
    private MergeContext mergeContext;
    private VecVersion version;

    public VecModelWrapper(final NamingStrategy namingStrategy, final String targetNamespace) {
        this.namingStrategy = namingStrategy;
        this.namespace = targetNamespace;
    }

    public VecVersion getVersion() {
        return version;
    }

    public void load(final InputStream vecFile) {
        LOGGER.info("Loading VEC model for patching.");
        final Document vecDocument = VecXmlApiUtils.loadDocument(vecFile);
        version = VecXmlApiUtils.guessVersion(vecDocument);

        final XMLReader<? extends Identifiable, Identifiable> vecReader = VecXmlApiUtils.resolveReader(version);

        final Identifiable root = vecReader.read(vecDocument);

        genericVecModelTraversal(root, this::addVecObject);

        mergeContext = new MergeContext(root, uriToIdentifiable);

        LOGGER.info("Found {} objects in existing VEC Model.", uriToIdentifiable.size());
    }

    private void addVecObject(final Identifiable identifiable) {
        final String uri = namingStrategy.uriFor(namespace, identifiable);
        if (uriToIdentifiable.containsKey(uri)) {
            throw new VecRdfException(
                    String.format("Duplicate URI '%1s' for VEC Object: %2s, existing value: %3s", uri, identifiable,
                                  uriToIdentifiable.get(uri)));
        }
        uriToIdentifiable.put(uri, identifiable);
    }

    public void removeValue(final Statement statement) {
        if (!statement.getSubject()
                .isURIResource()) {
            throw new VecRdfException("Subject of triple is not a URI resource, mapping of subjects only support ");
        }

        final String subjectUri = statement.getSubject()
                .getURI();
        final Identifiable subjectObject = uriToIdentifiable.get(subjectUri);

        if (subjectObject == null) {
            throw new MergeConflictException(
                    String.format("Subject '%1$s' for value removal does not exist in destination model. ", subjectUri),
                    statement);
        }

        final VecObjectWrapper subjectWrapper = wrapperObjects.computeIfAbsent(subjectObject,
                                                                               obj -> new VecObjectWrapper(mergeContext,
                                                                                                           obj));

        LOGGER.info("Modifying Object '{}', removing property: {}, value: {} ", subjectObject, statement.getPredicate()
                .getLocalName(), statement.getObject());

        subjectWrapper.removeValue(statement.getPredicate(), statement.getObject());

    }

    public void write(final OutputStream s) {
        final XmlIdGenerator idGenerator = new XmlIdGenerator("id_new_");

        genericVecModelTraversal(mergeContext.getRoot(), idGenerator::createIdForXmlBean);

        final XMLWriter<Identifiable> xmlWriter = createWriter();

        xmlWriter.write(mergeContext.getRoot(), s);
    }

    private XMLWriter<Identifiable> createWriter() {
        return new XMLWriter<>((Class<Identifiable>) mergeContext.getRoot()
                .getClass(), new ValidationEventLogger()) {

            @Override
            protected void configureMarshaller(final Marshaller marshaller) throws Exception {
                super.configureMarshaller(marshaller);
                marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "");
            }
        };
    }

    public void addValue(final Statement statement) {
        if (!statement.getSubject()
                .isURIResource()) {
            throw new VecRdfException("Subject of triple is not a URI resource, mapping of subjects only support ");
        }

        final String subjectUri = statement.getSubject()
                .getURI();
        final Identifiable subjectObject = uriToIdentifiable.get(subjectUri);

        if (subjectObject == null) {
            throw new MergeConflictException(
                    String.format("Subject '%1$s' for adding values does not exist in destination model. ", subjectUri),
                    statement);
        }

        final VecObjectWrapper subjectWrapper = wrapperObjects.computeIfAbsent(subjectObject,
                                                                               obj -> new VecObjectWrapper(mergeContext,
                                                                                                           obj));

        LOGGER.info("Modifying Object '{}', adding property: {}, value: {} ", subjectObject, statement.getPredicate()
                .getLocalName(), statement.getObject());

        subjectWrapper.loadValue(statement.getPredicate(), statement.getObject());
    }

}
