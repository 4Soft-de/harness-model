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
