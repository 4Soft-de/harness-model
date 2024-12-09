/*-
 * ========================LICENSE_START=================================
 * VEC RDF Common
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
package com.foursoft.harness.vec.rdf.common;

import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.util.List;

public class VEC {

    public static final String PREFIX = "vec";
    public static final String URI = "http://www.prostep.org/ontologies/ecad/2024/03/vec#";
    public static final String DEBUG_NS = "http://www.prostep.org/ontologies/ecad/2024/03/vec-debug#";
    public static final String CHANGESET = "http://www.prostep.org/ontologies/ecad/2024/03/vec-changeset#";

    protected static Property property(String local) {
        return ResourceFactory.createProperty(URI, local);
    }

    protected static Resource resource(String local) {
        return ResourceFactory.createResource(URI + local);
    }

    public static final Property enumLiteral = property("enumLiteral");

    public static final Resource Ordered = resource("Ordered");

    public static final Property orderedIndex = property("orderedIndex");

    public static final Resource OpenEnumeration = resource("OpenEnumeration");

    public static String enumLiteralValueFor(Resource enumValueResource) {
        Statement literalStatement = enumValueResource.getProperty(VEC.enumLiteral);
        if (literalStatement == null) {
            throw new VecRdfException("No enumLiteral found for OpenEnumeration Resource" + enumValueResource);
        }
        RDFNode literalObject = literalStatement.getObject();
        if (!literalObject.isLiteral()) {
            throw new VecRdfException(
                    "EnumLiteral '" + literalObject + "' for OpenEnumeration " + enumValueResource +
                            "is not a RDF literal.");
        }

        return literalObject.asLiteral()
                .getString();
    }

    /**
     * Checks if the object has the given type as rdf:type or, if the rdf:type is subClass of the given type.
     *
     * @param object
     * @param type
     * @return
     */
    public static boolean isInstanceOf(Resource object,
                                       Resource type) {
        List<Resource> types = object.listProperties(RDF.type)
                .mapWith(Statement::getObject)
                .filterKeep(RDFNode::isResource)
                .mapWith(RDFNode::asResource)
                .toList();
        return types.stream()
                .anyMatch(t -> isSubclassOf(t, type));
    }

    public static boolean isSubclassOf(Resource type,
                                       Resource superType) {
        if (type.equals(superType)) {
            return true;
        }
        List<Resource> candidateSuperTypes = type.listProperties(RDFS.subClassOf)
                .mapWith(Statement::getObject)
                .filterKeep(RDFNode::isResource)
                .mapWith(RDFNode::asResource)
                .toList();

        if (candidateSuperTypes.stream()
                .anyMatch(superType::equals)) {
            return true;
        }
        return candidateSuperTypes.stream()
                .anyMatch(t -> isSubclassOf(t, superType));
    }
}
