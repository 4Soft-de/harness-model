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

    public VecObjectFactory(final Class<? extends Identifiable> rootClass) {
        this.packageName = rootClass.getPackageName();
    }

    public Identifiable create(final Resource node) {
        Objects.requireNonNull(node, "Parameter 'node' must not be null.");

        final Statement requiredProperty = node.getProperty(RDF.type);
        if (requiredProperty == null) {
            throw new VecRdfException("Cannot create a VEC class for node " + node + "without rdf:type property");
        }

        final Resource typeResource = toUriResource(requiredProperty.getObject());

        final Class<? extends Identifiable> vecClass = lookupVecClass(typeResource);

        try {
            return vecClass.getConstructor()
                    .newInstance();
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException |
                       NoSuchMethodException e) {
            throw new VecRdfException("Unable to instantiate VEC object for: " + node, e);
        }

    }

    private Class<? extends Identifiable> lookupVecClass(final Resource typeResource) {
        if (!VEC.NS.equals(typeResource.getNameSpace())) {
            throw new VecRdfException("Cannot find VEC class for: " + typeResource);
        }
        final String localName = typeResource.getLocalName();
        final String className = packageName + ".Vec" + localName;

        try {
            return (Class<? extends Identifiable>) Class.forName(className);
        } catch (final ClassNotFoundException e) {
            throw new VecRdfException("Could not load class for type: " + typeResource, e);
        }
    }

    private Resource toUriResource(final RDFNode node) {
        if (node.isURIResource()) {
            return node.asResource();
        }
        throw new VecRdfException(node + " is not a URI resource");
    }
}
