package com.foursoft.harness.vec.rdf.changes;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public final class VECCS {

    public static final String URI = "http://www.prostep.org/ontologies/ecad/2024/03/vec-changeset#";

    private VECCS() {
        throw new AssertionError("Can't instantiate utility class");
    }

    public static final Resource ChangeSet = resource("ChangeSet");

    public static final Property added = property("changeSetAdded");
    public static final Property removed = property("changeSetRemoved");
    public static final Property identification = property("changeSetIdentification");

    static Resource resource(String local) {
        return ResourceFactory.createResource(URI + local);
    }

    static Property property(String local) {
        return ResourceFactory.createProperty(URI, local);
    }

}
