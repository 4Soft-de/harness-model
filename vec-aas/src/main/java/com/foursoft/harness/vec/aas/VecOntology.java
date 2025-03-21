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
