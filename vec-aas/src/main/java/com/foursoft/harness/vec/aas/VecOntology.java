/*-
 * ========================LICENSE_START=================================
 * VEC to AAS Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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
package com.foursoft.harness.vec.aas;

import com.foursoft.harness.vec.rdf.common.meta.VecField;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDFS;
import org.eclipse.digitaltwin.aas4j.v3.model.LangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringTextType;

import java.io.InputStream;

public class VecOntology {

    public static final String ONTOLOGY_LOCATION = "/vec/v2.1.0/vec-2.1.0-ontology.ttl";
    private final Model vecOntology;
    private final AasNamingStrategy namingStrategy;

    public VecOntology(final AasNamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
        vecOntology = ModelFactory.createDefaultModel();
        final InputStream ontologyInputStream = this.getClass().getResourceAsStream(ONTOLOGY_LOCATION);
        if (ontologyInputStream == null) {
            throw new AasConversionException("Cannot find VEC ontology: " + ONTOLOGY_LOCATION);
        }
        RDFDataMgr.read(vecOntology, ontologyInputStream, Lang.TTL);
    }

    public LangStringTextType descriptionFor(final Class<?> vecElement) {
        final String typeIRI = namingStrategy.uriFor(vecElement);
        final Resource vecResource = vecOntology.getResource(typeIRI);

        final Statement property = vecResource.getProperty(RDFS.comment);

        return new DefaultLangStringTextType.Builder().language("en").text(property.getString()).build();
    }

    public LangStringTextType descriptionFor(final VecField vecElement) {
        final String typeIRI = namingStrategy.uriFor(vecElement.getField());
        final Resource vecResource = vecOntology.getResource(typeIRI);

        final Statement property = vecResource.getProperty(RDFS.comment);
        if (property == null) {
            return new DefaultLangStringTextType.Builder().language("en").text("").build();
        }

        return new DefaultLangStringTextType.Builder().language("en").text(property.getString()).build();
    }
}
