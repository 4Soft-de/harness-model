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

import com.foursoft.harness.vec.rdf.common.VEC;
import com.foursoft.harness.vec.rdf.convert.Vec2RdfConverter;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.core.SerializationException;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.json.JsonSerializer;
import org.eclipse.digitaltwin.aas4j.v3.model.*;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class AasPlaygroundTest {

    private final JsonSerializer serializer = new JsonSerializer();

    @Test
    void convertTestData() throws FileNotFoundException {
        final Vec2RdfConverter converter = new Vec2RdfConverter();

        final InputStream inputFile = this.getClass().getResourceAsStream("/kostal-32124733993.vec");

        final Model onto = ModelFactory.createDefaultModel();
        RDFDataMgr.read(onto, this.getClass().getResourceAsStream("/vec/v2.1.0/vec-2.1.0-ontology.ttl"), Lang.TTL);

        final Model model = converter.convert(inputFile, "https://www.acme.inc/kostal/32124733993");

        //final OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF);
        final Model ontModel = ModelFactory.createDefaultModel();
        ontModel.add(onto);
        ontModel.add(model);

        ontModel.write(new FileOutputStream("kostal-example-with-ontology.ttl"), "TURTLE");

////        final Model expectedModel = ModelFactory.createDefaultModel();
////        expectedModel.read("fixtures/oldbeetle/oldbeetle.ttl", "TURTLE");
//
//        assertThat(model).satisfies(new Condition<>(m -> m.isIsomorphicWith(expectedModel),
//                                                    "model is isomorphic with expected model"));

    }

    @Test
    void test() throws SerializationException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        final Model vecInput = ModelFactory.createDefaultModel();
        RDFDataMgr.read(vecInput, this.getClass().getResourceAsStream("/kostal-example-with-ontology.ttl"), Lang.TTL);

        final Resource root = vecInput.createResource(
                "https://www.acme.inc/kostal/32124733993#DocumentVersion-DocumentVersion_00001");

        //   root.listProperties().forEach(System.out::println);

//        final DefaultSubmodelElementCollection element = new DefaultSubmodelElementCollection.Builder()
//                .idShort("SomeIDShort")
//                .description(new DefaultLangStringTextType.Builder().language("en").text("Some Engslish text")
//                .build())
//
//                .build();

        serializer.write(baos, StandardCharsets.UTF_8, toSubmodelElement(root));

        System.out.println(baos.toString(StandardCharsets.UTF_8));
    }

    public SubmodelElement toSubmodelElement(final Resource vecElement) {
        if (vecElement.isURIResource()) {
            final DefaultSubmodelElementCollection.Builder builder = new DefaultSubmodelElementCollection.Builder()
                    .idShort(vecElement.getLocalName())
                    .semanticId(semanticIdFor(vecElement))
                    .description(descriptionFor(vecElement));

            vecElement.listProperties().forEach(s -> {
                final SubmodelElement result = handleProperty(s);
                if (result != null) {
                    builder.value(result);
                }
            });

            return builder
                    .build();
        }
        return null;
    }

    private SubmodelElement handleProperty(final Statement s) {
        final Property predicate = s.getPredicate();
        if (predicate.getNameSpace().equals(VEC.NS)) {
            if (predicate.getProperty(RDF.type).getObject().equals(OWL.DatatypeProperty)) {
                return new DefaultProperty.Builder().idShort(predicate.getProperty(RDFS.label).getString()).description(
                        descriptionForVecType(predicate)).value(
                        s.getString()).semanticId(semanticIdFor(predicate)).build();
            } else {
                System.out.println("Unhandled:" + predicate.getProperty(RDFS.label).getString());
            }
        }
        return null;
    }

    private LangStringTextType descriptionFor(final Resource vecElement) {
        final Resource rdfType = (Resource) vecElement.getProperty(RDF.type).getObject();
        return descriptionForVecType(rdfType);
    }

    private LangStringTextType descriptionForVecType(final Resource vecElement) {
        final Statement property = vecElement.getProperty(RDFS.comment);

        return new DefaultLangStringTextType.Builder().language("en").text(property.getString()).build();
    }

    public Reference semanticIdFor(final Resource vecElement) {
        final Resource rdfType = (Resource) vecElement.getProperty(RDF.type).getObject();
        return new DefaultReference.Builder().type(ReferenceTypes.EXTERNAL_REFERENCE)
                .keys(new DefaultKey.Builder().type(KeyTypes.GLOBAL_REFERENCE).value(rdfType.getURI()).build())
                .build();
    }

    public Reference semanticIdFor(final Property vecElement) {
        return new DefaultReference.Builder().type(ReferenceTypes.EXTERNAL_REFERENCE)
                .keys(new DefaultKey.Builder().type(KeyTypes.GLOBAL_REFERENCE).value(vecElement.getURI()).build())
                .build();
    }

}
