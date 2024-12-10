package com.foursoft.harness.vec.rdf.common;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VECTest {

    private Model ontology;

    @BeforeEach
    void loadOntologyModel() {
        ontology = ModelFactory.createDefaultModel();
        ontology.read(VECTest.class.getResourceAsStream("/vec/v2.1.0/vec-2.1.0-ontology.ttl"), VEC.URI, "TURTLE");
    }

    @Nested
    class IsInstanceOf {

        @Test
        void should_return_true_for_color_reference_system() {
            final Resource type = ontology.getResource(VEC.URI + "ColorReferenceSystem");
            final Resource instance = ontology.getResource(VEC.URI + "ColorReferenceSystem_RAL");

            assertThat(VEC.isInstanceOf(instance, type)).isTrue();
        }

        @Test
        void should_return_false_for_specification() {
            final Resource type = ontology.getResource(VEC.URI + "Specification");
            final Resource instance = ontology.getResource(VEC.URI + "ColorReferenceSystem_RAL");

            assertThat(VEC.isInstanceOf(instance, type)).isFalse();
        }

    }

    @Nested
    class IsSubclassOf {

        @Test
        void should_return_true_for_open_enumeration() {
            final Resource subType = ontology.getResource(VEC.URI + "ColorReferenceSystem");
            final Resource superType = VEC.OpenEnumeration;

            assertThat(VEC.isSubclassOf(subType, superType)).isTrue();
        }

        @Test
        void should_return_false_for_specification() {
            final Resource subType = ontology.getResource(VEC.URI + "ColorReferenceSystem");
            final Resource superType = ontology.getResource(VEC.URI + "Specification");

            assertThat(VEC.isSubclassOf(subType, superType)).isFalse();
        }

    }

}