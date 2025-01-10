/*-
 * ========================LICENSE_START=================================
 * VEC RDF Converter
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
package com.foursoft.harness.vec.rdf.convert;

import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.rdf.common.NamingStrategy;
import com.foursoft.harness.vec.rdf.common.VEC;
import com.foursoft.harness.vec.rdf.common.meta.MetaDataUtils;
import com.foursoft.harness.vec.rdf.common.meta.VecClass;
import com.foursoft.harness.vec.rdf.common.meta.VecField;
import com.foursoft.harness.vec.rdf.common.meta.xmi.UmlField;
import com.foursoft.harness.vec.rdf.common.meta.xmi.UmlModelProvider;
import com.foursoft.harness.vec.rdf.common.meta.xmi.UmlType;
import com.foursoft.harness.vec.rdf.convert.exception.VecRdfConversionException;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

import static com.foursoft.harness.vec.rdf.common.meta.VecClass.analyzeClass;

public class VecSerializer {
    private final String targetNamespace;
    private final Model model;
    private final UmlModelProvider modelProvider;
    private final NamingStrategy namingStrategy;

    public VecSerializer(final Model model, final UmlModelProvider modelProvider,
                         final NamingStrategy namingStrategy,
                         final String targetNamespace) {

        this.model = model;
        this.modelProvider = modelProvider;
        this.namingStrategy = namingStrategy;
        this.targetNamespace = targetNamespace;
    }

    public void handle(final Identifiable identifiable) {
        if (modelProvider.isValueType(identifiable.getClass())) {
            return;
        }

        final VecClass metaData = analyzeClass(identifiable.getClass());
        createObjectNode(identifiable, false, metaData);
    }

    private Resource createObjectNode(final Identifiable identifiable, final boolean createBNode,
                                      final VecClass metaData) {

        final Resource rdfType = model.createResource(namingStrategy.uriFor(identifiable.getClass()));
        Resource resource = model.createResource(namingStrategy.uriFor(targetNamespace, identifiable));
        if (createBNode) {
            resource = model.createResource();
        }
        resource.addProperty(RDF.type, rdfType);
        resource.addLiteral(model.createProperty(namingStrategy.debugUri("id")), identifiable.getXmlId());
        resource.addLiteral(RDFS.label, namingStrategy.rdfLabel(identifiable));

        for (final VecField field : metaData.getFields()) {
            final Object value = field.readValue(identifiable);

            if (value != null) {
                final Property property = model.createProperty(namingStrategy.uriFor(field.getField()));
                if (value instanceof final List<?> list) {
                    for (int i = 0; i < list.size(); i++) {
                        createPropertyValue(field, resource, property, list.get(i), i);
                    }
                } else {
                    createPropertyValue(field, resource, property, value, 0);
                }

            }
        }
        return resource;
    }

    private void createPropertyValue(final VecField field, final Resource targetResource, final Property property,
                                     final Object value,
                                     final int index) {
        if (MetaDataUtils.isRdfLiteral(value.getClass())) {
            if (getUmlField(field).isEnumField() && value instanceof final String enumLiteralValue) {
                handleOpenEnum(targetResource, property, enumLiteralValue, field);
            } else {
                handleLiteralProperty(targetResource, property, value);
            }
        } else if (value instanceof final Identifiable destinationIdentifiable) {
            handleVecClass(field, targetResource, property, destinationIdentifiable, index);
        } else if (value instanceof final Enum<?> enumLiteral) {
            handleClosedEnum(targetResource, property, enumLiteral);
        } else {
            throw new VecRdfConversionException("Unhandled Type: " + value.getClass()
                    .getSimpleName());
        }
    }

    private UmlField getUmlField(final VecField field) {
        return modelProvider.findField(field.getField());
    }

    private void handleOpenEnum(final Resource resource, final Property property, final String enumLiteralValue,
                                final VecField field) {
        final UmlType modelType = getUmlField(field).type();
        final String literalUri = namingStrategy.uriForOpenEnumLiteral(modelType, enumLiteralValue,
                                                                       targetNamespace);

        final Resource literalResource = model.createResource(literalUri);
        if (!literalUri.startsWith(VEC.URI)) {
            literalResource.addProperty(RDF.type, model.createResource(namingStrategy.uriFor(modelType)));
            literalResource.addProperty(VEC.enumLiteral, model.createLiteral(enumLiteralValue));
        }

        resource.addProperty(property, literalResource);

    }

    private void handleClosedEnum(final Resource targetResource, final Property property, final Enum<?> enumLiteral) {
        final Resource object = model.createResource(namingStrategy.uriFor(enumLiteral));

        targetResource.addProperty(property, object);
    }

    private void handleVecClass(final VecField field, final Resource targetResource, final Property property,
                                final Identifiable value,
                                final int index) {
        Resource object = createValueNode(value);

        final UmlField umlField = getUmlField(field);

        // We need a wrapper bucket.
        if (umlField.isAssociation() && (umlField.isOrdered() || !umlField.isUnique())) {
            final Resource wrapper = model.createResource();
            final Resource wrapperType = model.createResource(namingStrategy.uriForWrapper(umlField.type()));
            wrapper.addProperty(RDF.type, wrapperType);
            wrapper.addProperty(model.createProperty(namingStrategy.uriForWrapperItemProperty(umlField.type())),
                                object);
            object = wrapper;
        }
        if (umlField.isOrdered()) {
            object.addProperty(RDF.type, VEC.Ordered);
            object.addLiteral(VEC.orderedIndex, model.createTypedLiteral(index, XSDDatatype.XSDnonNegativeInteger));
        }

        targetResource.addProperty(property, object);
    }

    private Resource createValueNode(final Identifiable value) {
        final VecClass metaData = analyzeClass(value.getClass());

        if (modelProvider.isValueType(value.getClass())) {
            return createObjectNode(value, true, metaData);
        }
        return model.createResource(namingStrategy.uriFor(targetNamespace, value));
    }

    private void handleLiteralProperty(final Resource targetResource, final Property property, final Object value) {
        final Literal literal;
        if (value instanceof final XMLGregorianCalendar calendar) {
            literal = model.createTypedLiteral(calendar.toXMLFormat(), XSDDatatype.XSDdateTime);
        } else {
            literal = model.createTypedLiteral(value);
        }
        targetResource.addLiteral(property, literal);
    }

}
