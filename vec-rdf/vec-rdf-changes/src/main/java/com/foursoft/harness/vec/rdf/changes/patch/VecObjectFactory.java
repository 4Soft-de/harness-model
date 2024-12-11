package com.foursoft.harness.vec.rdf.changes.patch;

import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.rdf.common.VEC;
import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class VecObjectFactory {

    private final String packageName;

    public VecObjectFactory(Class<? extends Identifiable> rootClass) {
        this.packageName = rootClass.getPackageName();
    }

    public Identifiable create(Resource node) {
        Objects.requireNonNull(node, "Parameter 'node' must not be null.");

        Statement requiredProperty = node.getProperty(RDF.type);
        if (requiredProperty == null) {
            throw new VecRdfException("Cannot create a VEC class for node " + node + "without rdf:type property");
        }

        Resource typeResource = toUriResource(requiredProperty.getObject());

        Class<? extends Identifiable> vecClass = lookupVecClass(typeResource);

        try {
            return vecClass.getConstructor()
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new VecRdfException("Unable to instantiate VEC object for: " + node, e);
        }

    }

    private Class<? extends Identifiable> lookupVecClass(Resource typeResource) {
        if (!VEC.URI.equals(typeResource.getNameSpace())) {
            throw new VecRdfException("Cannot find VEC class for: " + typeResource);
        }
        String localName = typeResource.getLocalName();
        String className = packageName + ".Vec" + localName;

        try {
            return (Class<? extends Identifiable>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new VecRdfException("Could not load class for type: " + typeResource, e);
        }
    }

    private Resource toUriResource(RDFNode node) {
        if (node.isURIResource()) {
            return node.asResource();
        }
        throw new VecRdfException(node + " is not a URI resource");
    }
}
