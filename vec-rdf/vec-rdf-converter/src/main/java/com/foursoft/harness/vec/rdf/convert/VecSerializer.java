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
import com.foursoft.harness.vec.rdf.common.meta.ClassMetaData;
import com.foursoft.harness.vec.rdf.common.meta.FieldMetaData;
import com.foursoft.harness.vec.rdf.common.meta.MetaDataService;
import com.foursoft.harness.vec.rdf.common.meta.MetaDataUtils;
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

public class VecSerializer {
    private final String targetNamespace;
    private final Model model;
    private final MetaDataService metaDataService;
    private final NamingStrategy namingStrategy;

    public VecSerializer(Model model, MetaDataService metaDataService, NamingStrategy namingStrategy,
                         String targetNamespace) {
        this.model = model;
        this.metaDataService = metaDataService;
        this.namingStrategy = namingStrategy;
        this.targetNamespace = targetNamespace;
    }

    public void handle(Identifiable identifiable) {
        ClassMetaData metaData = metaDataService.metaDataFor(identifiable.getClass());
        if (metaData.isAnonymous()) {
            return;
        }

        createObjectNode(identifiable, false, metaData);
    }

    private Resource createObjectNode(Identifiable identifiable, boolean createBNode, ClassMetaData metaData) {

        Resource rdfType = model.createResource(namingStrategy.uriFor(identifiable.getClass()));
        Resource resource = model.createResource(namingStrategy.uriFor(targetNamespace, identifiable));
        if (createBNode) {
            resource = model.createResource();
        }
        resource.addProperty(RDF.type, rdfType);
        resource.addLiteral(model.createProperty(namingStrategy.debugUri("id")), identifiable.getXmlId());
        resource.addLiteral(RDFS.label, namingStrategy.rdfLabel(identifiable));

        for (FieldMetaData field : metaData.fields()) {
            Object value = field.readValue(identifiable);

            if (value != null) {
                Property property = model.createProperty(namingStrategy.uriFor(field.getField()));
                if (value instanceof List<?> list) {
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

    private void createPropertyValue(FieldMetaData field, Resource targetResource, Property property, Object value,
                                     int index) {
        if (MetaDataUtils.isRdfLiteral(value.getClass())) {
            if (field.isEnumField() && value instanceof String enumLiteralValue) {
                handleOpenEnum(targetResource, property, enumLiteralValue, field);
            } else {
                handleLiteralProperty(targetResource, property, value);
            }
        } else if (value instanceof Identifiable destinationIdentifiable) {
            handleVecClass(field, targetResource, property, destinationIdentifiable, index);
        } else if (value instanceof Enum<?> enumLiteral) {
            handleClosedEnum(targetResource, property, enumLiteral);
        } else {
            throw new VecRdfConversionException("Unhandled Type: " + value.getClass()
                    .getSimpleName());
        }
    }

    private void handleOpenEnum(Resource resource, Property property, String enumLiteralValue, FieldMetaData field) {
        String literalUri = namingStrategy.uriForOpenEnumLiteral(field.getModelType(), enumLiteralValue,
                                                                 targetNamespace);

        Resource literalResource = model.createResource(literalUri);
        if (!literalUri.startsWith(VEC.URI)) {
            literalResource.addProperty(RDF.type, model.createResource(namingStrategy.uriFor(field.getModelType())));
            literalResource.addProperty(VEC.enumLiteral, model.createLiteral(enumLiteralValue));
        }

        resource.addProperty(property, literalResource);

    }

    private void handleClosedEnum(Resource targetResource, Property property, Enum<?> enumLiteral) {
        Resource object = model.createResource(namingStrategy.uriFor(enumLiteral));

        targetResource.addProperty(property, object);
    }

    private void handleVecClass(FieldMetaData field, Resource targetResource, Property property, Identifiable value,
                                int index) {
        Resource object = createValueNode(value);

        // We need a wrapper bucket.
        if (field.isAssociation() && (field.isOrdered() || !field.isUnique())) {
            Resource wrapper = model.createResource();
            Resource wrapperType = model.createResource(namingStrategy.uriForWrapper(field.getModelType()));
            wrapper.addProperty(RDF.type, wrapperType);
            wrapper.addProperty(model.createProperty(namingStrategy.uriForWrapperItemProperty(field.getModelType())),
                                object);
            object = wrapper;
        }
        if (field.isOrdered()) {
            object.addProperty(RDF.type, VEC.Ordered);
            object.addLiteral(VEC.orderedIndex, model.createTypedLiteral(index, XSDDatatype.XSDnonNegativeInteger));
        }

        targetResource.addProperty(property, object);
    }

    private Resource createValueNode(Identifiable value) {
        ClassMetaData metaData = metaDataService.metaDataFor(value.getClass());

        if (metaData.isAnonymous()) {
            return createObjectNode(value, true, metaData);
        }
        return model.createResource(namingStrategy.uriFor(targetNamespace, value));
    }

    private void handleLiteralProperty(Resource targetResource, Property property, Object value) {
        Literal literal;
        if (value instanceof XMLGregorianCalendar calendar) {
            literal = model.createTypedLiteral(calendar.toXMLFormat(), XSDDatatype.XSDdateTime);
        } else {
            literal = model.createTypedLiteral(value);
        }
        targetResource.addLiteral(property, literal);
    }

}
