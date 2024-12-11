package com.foursoft.harness.vec.rdf.changes.patch;

import com.foursoft.harness.navext.runtime.model.Identifiable;
import org.apache.jena.rdf.model.Resource;

import java.util.Map;
import java.util.Objects;

public class MergeContext {
    private final Map<String, Identifiable> uriToIdentifiable;
    private final VecObjectFactory objectFactory;
    private final Identifiable root;

    public MergeContext(final Identifiable root, Map<String, Identifiable> uriToIdentifiable) {
        Objects.requireNonNull(root, "root must not be null");
        Objects.requireNonNull(uriToIdentifiable, "uriToIdentifiable must not be null");
        this.root = root;
        this.uriToIdentifiable = uriToIdentifiable;
        this.objectFactory = new VecObjectFactory(root.getClass());
    }

    public Identifiable getRoot() {
        return root;
    }

    public Identifiable getVecObjectForUri(final String uri) {
        Identifiable identifiable = uriToIdentifiable.get(uri);
        if (identifiable == null) {
            throw new MergeConflictException("The object represented by: " + uri + " does not exist in the context.");
        }
        return identifiable;
    }

    public Identifiable create(Resource valueNode) {
        Identifiable result = objectFactory.create(valueNode);
        if (valueNode.isURIResource()) {
            uriToIdentifiable.put(valueNode.getURI(), result);
        }
        return result;
    }
}
