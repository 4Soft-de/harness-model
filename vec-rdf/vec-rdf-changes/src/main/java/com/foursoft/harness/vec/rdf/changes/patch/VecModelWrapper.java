package com.foursoft.harness.vec.rdf.changes.patch;

import com.foursoft.harness.navext.runtime.io.read.XMLReader;
import com.foursoft.harness.navext.runtime.io.utils.ValidationEventLogger;
import com.foursoft.harness.navext.runtime.io.write.XMLWriter;
import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.rdf.common.NamingStrategy;
import com.foursoft.harness.vec.rdf.common.VecVersion;
import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;
import com.foursoft.harness.vec.rdf.convert.VecXmlApiUtils;
import jakarta.xml.bind.Marshaller;
import org.apache.jena.rdf.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class VecModelWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(VecModelWrapper.class);

    private final NamingStrategy namingStrategy;
    private final String namespace;
    //TODO: Move to merge context.
    private final Map<String, Identifiable> uriToIdentifiable = new HashMap<String, Identifiable>();
    private final Map<Identifiable, VecObjectWrapper> wrapperObjects = new HashMap<>();
    private MergeContext mergeContext;
    private VecVersion version;

    public VecModelWrapper(NamingStrategy namingStrategy, String targetNamespace) {
        this.namingStrategy = namingStrategy;
        this.namespace = targetNamespace;

    }

    public void load(InputStream vecFile) {
        LOGGER.info("Loading VEC model for patching.");
        Document vecDocument = VecXmlApiUtils.loadDocument(vecFile);
        version = VecXmlApiUtils.guessVersion(vecDocument);

        XMLReader<? extends Identifiable, Identifiable> vecReader = VecXmlApiUtils.resolveReader(version);

        Identifiable root = vecReader.read(vecDocument);

        VecXmlApiUtils.genericVecModelTraversal(root, this::addVecObject);

        mergeContext = new MergeContext(root, uriToIdentifiable);

        LOGGER.info("Found {} objects in existing VEC Model.", uriToIdentifiable.size());
    }

    private void addVecObject(Identifiable identifiable) {
        String uri = namingStrategy.uriFor(namespace, identifiable);
        if (uriToIdentifiable.containsKey(uri)) {
            throw new VecRdfException(
                    String.format("Duplicate URI '%1s' for VEC Object: %2s, existing value: %3s", uri, identifiable,
                                  uriToIdentifiable.get(uri)));
        }
        uriToIdentifiable.put(uri, identifiable);
    }

    public void removeValue(Statement statement) {
        if (!statement.getSubject()
                .isURIResource()) {
            throw new VecRdfException("Subject of triple is not a URI resource, mapping of subjects only support ");
        }

        String subjectUri = statement.getSubject()
                .getURI();
        Identifiable subjectObject = uriToIdentifiable.get(subjectUri);

        if (subjectObject == null) {
            throw new MergeConflictException(
                    String.format("Subject '%1$s' for value removal does not exist in destination model. ", subjectUri),
                    statement);
        }

        VecObjectWrapper subjectWrapper = wrapperObjects.computeIfAbsent(subjectObject,
                                                                         obj -> new VecObjectWrapper(mergeContext,
                                                                                                     obj));

        LOGGER.info("Modifying Object '{}', removing property: {}, value: {} ", subjectObject, statement.getPredicate()
                .getLocalName(), statement.getObject());

        subjectWrapper.removeValue(statement.getPredicate(), statement.getObject());

    }

    public void write(String s) throws FileNotFoundException {
        XMLWriter<Identifiable> xmlWriter = createWriter();
        xmlWriter.write(mergeContext.getRoot(), new FileOutputStream(s));
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

    public void addValue(Statement statement) {
        if (!statement.getSubject()
                .isURIResource()) {
            throw new VecRdfException("Subject of triple is not a URI resource, mapping of subjects only support ");
        }

        String subjectUri = statement.getSubject()
                .getURI();
        Identifiable subjectObject = uriToIdentifiable.get(subjectUri);

        if (subjectObject == null) {
            throw new MergeConflictException(
                    String.format("Subject '%1$s' for adding values does not exist in destination model. ", subjectUri),
                    statement);
        }

        VecObjectWrapper subjectWrapper = wrapperObjects.computeIfAbsent(subjectObject,
                                                                         obj -> new VecObjectWrapper(mergeContext,
                                                                                                     obj));

        LOGGER.info("Modifying Object '{}', adding property: {}, value: {} ", subjectObject, statement.getPredicate()
                .getLocalName(), statement.getObject());

        subjectWrapper.loadValue(statement.getPredicate(), statement.getObject());
    }

}
