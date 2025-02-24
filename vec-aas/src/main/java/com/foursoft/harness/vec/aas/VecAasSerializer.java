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
package com.foursoft.harness.vec.aas;

import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.rdf.common.NamingStrategy;
import com.foursoft.harness.vec.rdf.common.meta.MetaDataUtils;
import com.foursoft.harness.vec.rdf.common.meta.VecClass;
import com.foursoft.harness.vec.rdf.common.meta.VecField;
import com.foursoft.harness.vec.rdf.common.meta.xmi.UmlField;
import com.foursoft.harness.vec.rdf.common.meta.xmi.UmlModelProvider;
import com.foursoft.harness.vec.rdf.common.meta.xmi.UmlType;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.util.SplitIRI;
import org.apache.jena.vocabulary.RDFS;
import org.eclipse.digitaltwin.aas4j.v3.model.*;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static com.foursoft.harness.vec.rdf.common.meta.VecClass.analyzeClass;
import static java.util.Map.entry;

public class VecAasSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(VecAasSerializer.class);
    private final String targetNamespace;
    private final UmlModelProvider modelProvider;
    private final NamingStrategy namingStrategy;
    private final Model vecOntology;

    public VecAasSerializer(final UmlModelProvider modelProvider,
                            final NamingStrategy namingStrategy,
                            final String targetNamespace) {

        this.modelProvider = modelProvider;
        this.namingStrategy = namingStrategy;
        this.targetNamespace = targetNamespace;
        vecOntology = ModelFactory.createDefaultModel();
        RDFDataMgr.read(vecOntology, this.getClass().getResourceAsStream("/vec/v2.1.0/vec-2.1.0-ontology.ttl"),
                        Lang.TTL);
    }

    public SubmodelElement handle(final Identifiable identifiable) {
        final VecClass metaData = analyzeClass(identifiable.getClass());
        return createObjectNode(identifiable, false, metaData);
    }

    private SubmodelElementCollection createObjectNode(final Identifiable identifiable, final boolean createBNode,
                                                       final VecClass metaData) {
        final String elementIRI = namingStrategy.uriFor(targetNamespace, identifiable);

        final DefaultSubmodelElementCollection.Builder builder =
                new DefaultSubmodelElementCollection.Builder().semanticId(semanticIdFor(identifiable.getClass()))
                        .idShort(SplitIRI.localname(elementIRI))
                        .description(descriptionFor(identifiable.getClass()));

        for (final VecField field : metaData.getFields()) {
            final Object value = field.readValue(identifiable);

            if (value != null) {
                if (value instanceof final List<?> list) {
                    if (!list.isEmpty()) {
                        builder.value(createList(identifiable, field, list));
                    }
                } else {
                    builder.value(createProperty(identifiable, field, value, -1));
                }

            }
        }

        return builder.build();
    }

    public Reference semanticIdFor(final Class<?> vecElement) {
        final String typeIRI = namingStrategy.uriFor(vecElement);
        return referenceFor(typeIRI);
    }

    public Reference semanticIdFor(final VecField vecElement) {
        final String typeIRI = namingStrategy.uriFor(vecElement.getField());
        return referenceFor(typeIRI);
    }

    public Reference referenceFor(final Enum<?> enumLiteral) {
        final String typeIRI = namingStrategy.uriFor(enumLiteral);
        return referenceFor(typeIRI);
    }

    public Reference referenceFor(final String iri) {
        return new DefaultReference.Builder().type(ReferenceTypes.EXTERNAL_REFERENCE)
                .keys(new DefaultKey.Builder().type(KeyTypes.GLOBAL_REFERENCE).value(iri).build())
                .build();
    }

    public Reference localReferenceFor(final Identifiable vecElement) {
        final String elementIRI = namingStrategy.uriFor(targetNamespace, vecElement);
        final String localName = SplitIRI.localname(elementIRI);
        return new DefaultReference.Builder()
                .type(ReferenceTypes.MODEL_REFERENCE)
                .keys(
                        new DefaultKey.Builder()
                                .type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
                                .value(localName)
                                .build())
                .build();
    }

    private LangStringTextType descriptionFor(final Class<?> vecElement) {
        final String typeIRI = namingStrategy.uriFor(vecElement);
        final Resource vecResource = vecOntology.getResource(typeIRI);

        final Statement property = vecResource.getProperty(RDFS.comment);

        return new DefaultLangStringTextType.Builder().language("en").text(property.getString()).build();
    }

    private LangStringTextType descriptionFor(final VecField vecElement) {
        final String typeIRI = namingStrategy.uriFor(vecElement.getField());
        final Resource vecResource = vecOntology.getResource(typeIRI);

        final Statement property = vecResource.getProperty(RDFS.comment);
        if (property == null) {
            return new DefaultLangStringTextType.Builder().language("en").text("").build();
        }

        return new DefaultLangStringTextType.Builder().language("en").text(property.getString()).build();
    }

    private SubmodelElement createList(final Identifiable context, final VecField field, final List<?> list) {
        final UmlField umlField = getUmlField(field);
        final DefaultSubmodelElementList.Builder builder = new DefaultSubmodelElementList.Builder().idShort(
                        field.getName())
                .semanticId(semanticIdFor(field))
                .description(descriptionFor(field)).orderRelevant(umlField.isOrdered());

        final Class<?> vecValueType = field.getValueType();

        for (final Object value : list) {
            if (MetaDataUtils.isRdfLiteral(vecValueType)) {
                builder.typeValueListElement(AasSubmodelElements.PROPERTY);
                throw new AasConversionException("Unsupported list element type: " + vecValueType);
            } else if (value instanceof final Identifiable identifiable) {
                if (field.isReference()) {
                    builder.typeValueListElement(AasSubmodelElements.RELATIONSHIP_ELEMENT).value(
                            createReferenceNode(context, field, identifiable));
                } else {
                    builder.typeValueListElement(AasSubmodelElements.SUBMODEL_ELEMENT_COLLECTION).value(
                            createObjectNode(identifiable, false, VecClass.analyzeClass(identifiable.getClass())));
                }
            } else {
                throw new AasConversionException("Unsupported list element type: " + vecValueType);
            }
        }

        return builder.build();
    }

    private SubmodelElement createProperty(final Identifiable context, final VecField field, final Object value,
                                           final int index) {
        if (MetaDataUtils.isRdfLiteral(value.getClass())) {

            if (getUmlField(field).isEnumField() && value instanceof final String enumLiteralValue) {
                return handleOpenEnum(field, enumLiteralValue);
            } else {
                return handleLiteralProperty(field, value);
            }
        } else if (value instanceof final Identifiable destinationIdentifiable) {
            if (!field.isReference()) {
                return handleObjectProperty(field, destinationIdentifiable);
            } else {
                return handleReferenceProperty(context, field, destinationIdentifiable);
            }
        } else if (value instanceof final Enum<?> enumLiteral) {
            return handleClosedEnumProperty(field, enumLiteral);
        }
        throw new AasConversionException("Unhandled Type: " + value.getClass()
                .getSimpleName());

    }

    private UmlField getUmlField(final VecField field) {
        return modelProvider.findField(field.getField());
    }

    private SubmodelElement handleOpenEnum(final VecField field, final String enumLiteralValue) {
        final UmlType modelType = getUmlField(field).type();
        final String literalUri = namingStrategy.uriForOpenEnumLiteral(modelType, enumLiteralValue,
                                                                       targetNamespace);

        return new DefaultProperty.Builder()
                .idShort(field.getName())
                .semanticId(semanticIdFor(field))
                .description(descriptionFor(field))
                .value(enumLiteralValue)
                .valueType(DataTypeDefXsd.STRING)
                .valueId(referenceFor(literalUri)).build();

    }

    //
    private SubmodelElement handleClosedEnumProperty(final VecField field, final Enum<?> enumLiteral) {
        final DefaultProperty.Builder propertyBuilder = new DefaultProperty.Builder()
                .idShort(field.getName())
                .semanticId(semanticIdFor(field))
                .description(descriptionFor(field));
        propertyBuilder.value(enumLiteral.name());

        return propertyBuilder.valueType(DataTypeDefXsd.STRING).valueId(referenceFor(enumLiteral)).build();
    }

    private SubmodelElement createReferenceNode(final Identifiable context, final VecField field,
                                                final Identifiable value) {
        final String elementIRI = namingStrategy.uriFor(targetNamespace, value);
        return new DefaultRelationshipElement.Builder()
                .idShort(SplitIRI.localname(elementIRI))
                .semanticId(semanticIdFor(field))
                .description(descriptionFor(field))
                .first(localReferenceFor(context))
                .second(localReferenceFor(value))
                .build();
    }

    private SubmodelElement handleReferenceProperty(final Identifiable context, final VecField field,
                                                    final Identifiable value) {
        return new DefaultRelationshipElement.Builder()
                .idShort(field.getName())
                .semanticId(semanticIdFor(field))
                .description(descriptionFor(field))
                .first(localReferenceFor(context))
                .second(localReferenceFor(value))
                .build();
    }

    private SubmodelElement handleObjectProperty(final VecField field, final Identifiable value) {
        final DefaultSubmodelElementCollection.Builder builder = new DefaultSubmodelElementCollection.Builder().idShort(
                        field.getName())
                .semanticId(semanticIdFor(field))
                .description(descriptionFor(field))
                .value(createObjectNode(value, false, VecClass.analyzeClass(value.getClass())));
        return builder.build();
    }

    private SubmodelElement handleLiteralProperty(final VecField field, final Object value) {
        final DefaultProperty.Builder propertyBuilder = new DefaultProperty.Builder()
                .idShort(field.getName())
                .semanticId(semanticIdFor(field))
                .description(descriptionFor(field));
        if (value instanceof final XMLGregorianCalendar calendar) {
            propertyBuilder.value(calendar.toXMLFormat());
        } else {
            propertyBuilder.value(value.toString());
        }
        return propertyBuilder.valueType(valueTypeFor(field)).build();
    }

    private DataTypeDefXsd valueTypeFor(final VecField field) {
        final Class<?> valueType = field.getValueType();
        final DataTypeDefXsd dataTypeDefXsd = dataTypeDefXsdCache.get(valueType);
        if (dataTypeDefXsd != null) {
            return dataTypeDefXsd;
        }

        LOGGER.warn("At the moment, everthing is a string. Implment type mapping for:.{}", valueType);
        return DataTypeDefXsd.STRING;
    }

    private static final Map<Class<?>, DataTypeDefXsd> dataTypeDefXsdCache = Map.ofEntries(
            entry(XMLGregorianCalendar.class, DataTypeDefXsd.DATE_TIME),
            entry(String.class, DataTypeDefXsd.STRING),
            entry(double.class, DataTypeDefXsd.DOUBLE),
            entry(BigInteger.class, DataTypeDefXsd.LONG),
            entry(Boolean.class, DataTypeDefXsd.BOOLEAN)
    );

}
