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
import com.foursoft.harness.vec.rdf.common.VEC;
import com.foursoft.harness.vec.rdf.common.meta.MetaDataUtils;
import com.foursoft.harness.vec.rdf.common.meta.VecClass;
import com.foursoft.harness.vec.rdf.common.meta.VecField;
import com.foursoft.harness.vec.rdf.common.meta.xmi.UmlField;
import com.foursoft.harness.vec.rdf.common.meta.xmi.UmlModelProvider;
import com.foursoft.harness.vec.rdf.common.meta.xmi.UmlType;
import org.eclipse.digitaltwin.aas4j.v3.model.*;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.foursoft.harness.vec.aas.ColorWrapper.isColorType;
import static com.foursoft.harness.vec.aas.LocalizedStringWrapper.isLocalizedType;
import static com.foursoft.harness.vec.rdf.common.meta.VecClass.analyzeClass;
import static java.util.Map.entry;

public class VecAasSerializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(VecAasSerializer.class);

    private final String targetNamespace;
    private final Identifiable root;
    private final UmlModelProvider modelProvider;
    private final AasNamingStrategy namingStrategy;

    private final ReferenceFactory referenceFactory;
    private final VecOntology vecOntology;

    public VecAasSerializer(final UmlModelProvider modelProvider,
                            final AasNamingStrategy namingStrategy,
                            final String targetNamespace,
                            final Identifiable root) {

        this.modelProvider = modelProvider;
        this.namingStrategy = namingStrategy;
        this.targetNamespace = targetNamespace;
        this.root = root;
        referenceFactory = new ReferenceFactory(this.namingStrategy, targetNamespace, this.root);
        vecOntology = new VecOntology(this.namingStrategy);

    }

    public Submodel serialize() {

        return new DefaultSubmodel.Builder()
                .semanticId(referenceFactory.referenceFor(VEC.URI))
                .id(targetNamespace)
                .idShort("VEC")
                .submodelElements(handleVecObject(root))
                .build();
    }

    private SubmodelElement handleVecObject(final Identifiable contextObject) {
        final VecClass metaData = analyzeClass(contextObject.getClass());
        final DefaultSubmodelElementCollection.Builder builder =
                new DefaultSubmodelElementCollection.Builder().semanticId(
                                referenceFactory.semanticIdFor(contextObject.getClass()))
                        .idShort(namingStrategy.idShort(contextObject))
                        .description(vecOntology.descriptionFor(contextObject.getClass()));

        Arrays.stream(metaData.getFields())
                .map(field -> handleVecObjectField(contextObject, field))
                .filter(Objects::nonNull)
                .forEach(builder::value);

        return builder.build();
    }

    private SubmodelElement handleVecObjectField(final Identifiable contextObject, final VecField field) {
        if (field.isCollection()) {
            if (isLocalizedType(field.getValueType())) {
                return handleMultiLangValueField(contextObject, field);
            }
            return handleMultiValueField(contextObject, field);
        } else {
            return handleSingleValueField(contextObject, field);
        }
    }

    private SubmodelElement handleSingleValueField(final Identifiable contextObject, final VecField field) {
        final Object fieldValue = field.readValue(contextObject);
        if (fieldValue == null) {
            return null;
        }
        return createProperty(contextObject, field, fieldValue, -1);
    }

    private SubmodelElement handleMultiValueField(final Identifiable contextObject, final VecField field) {
        final Object fieldValue = field.readValue(contextObject);
        if (fieldValue == null) {
            return null;
        }
        if (fieldValue instanceof final List<?> list) {
            return createList(contextObject, field, list);
        }
        throw new AasConversionException("Expected a list value for: " + field + " but got " + fieldValue);
    }

    private SubmodelElement handleMultiLangValueField(final Identifiable contextObject, final VecField field) {
        final Object fieldValue = field.readValue(contextObject);
        if (fieldValue == null) {
            return null;
        }
        if (fieldValue instanceof final List<?> list) {
            final List<LangStringTextType> langStrings = list.stream()
                    .map(LocalizedStringWrapper::wrap)
                    .map(LocalizedStringWrapper::toLangStringTextType)
                    .toList();

            final DefaultMultiLanguageProperty.Builder builder = new DefaultMultiLanguageProperty.Builder()
                    .idShort(namingStrategy.idShort(field))
                    .semanticId(referenceFactory.semanticIdFor(field))
                    .description(vecOntology.descriptionFor(field))
                    .value(langStrings);

            return builder.build();
        }
        throw new AasConversionException("Expected a list value for: " + field + " but got " + fieldValue);
    }

    private SubmodelElement handleColorFieldValue(final Identifiable contextObject) {
        return ColorWrapper.wrap(contextObject).toProperty(referenceFactory);
    }

    private SubmodelElement createList(final Identifiable context, final VecField field, final List<?> list) {
        final UmlField umlField = getUmlField(field);
        final DefaultSubmodelElementList.Builder builder = new DefaultSubmodelElementList.Builder()
                .idShort(namingStrategy.idShort(field))
                .semanticId(referenceFactory.semanticIdFor(field))
                .description(vecOntology.descriptionFor(field))
                .orderRelevant(umlField.isOrdered());

        final Class<?> vecValueType = field.getValueType();

        for (final Object value : list) {
            if (MetaDataUtils.isRdfLiteral(vecValueType)) {
                builder.typeValueListElement(AasSubmodelElements.PROPERTY);
                throw new AasConversionException("Unsupported list element type: " + vecValueType);
            } else if (value instanceof final Identifiable identifiable) {
                if (field.isReference()) {
                    builder.typeValueListElement(AasSubmodelElements.RELATIONSHIP_ELEMENT).value(
                            createReferenceNode(context, field, identifiable));
                }
                if (isColorType(field.getValueType())) {
                    builder.typeValueListElement(AasSubmodelElements.PROPERTY).value(
                            handleColorFieldValue(identifiable));
                } else {
                    builder.typeValueListElement(AasSubmodelElements.SUBMODEL_ELEMENT_COLLECTION).value(
                            handleVecObject(identifiable));
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

    private SubmodelElement handleOpenEnum(final VecField field, final String enumLiteralValue) {
        final UmlType modelType = getUmlField(field).type();
        final String literalUri = namingStrategy.uriForOpenEnumLiteral(modelType, enumLiteralValue,
                                                                       targetNamespace);

        return new DefaultProperty.Builder()
                .idShort(namingStrategy.idShort(field))
                .semanticId(referenceFactory.semanticIdFor(field))
                .description(vecOntology.descriptionFor(field))
                .value(enumLiteralValue)
                .valueType(DataTypeDefXsd.STRING)
                .valueId(referenceFactory.referenceFor(literalUri)).build();

    }

    //
    private SubmodelElement handleClosedEnumProperty(final VecField field, final Enum<?> enumLiteral) {
        final DefaultProperty.Builder propertyBuilder = new DefaultProperty.Builder()
                .idShort(namingStrategy.idShort(field))
                .semanticId(referenceFactory.semanticIdFor(field))
                .description(vecOntology.descriptionFor(field));
        propertyBuilder.value(enumLiteral.name());

        return propertyBuilder.valueType(DataTypeDefXsd.STRING).valueId(referenceFactory.referenceFor(enumLiteral))
                .build();
    }

    private SubmodelElement createReferenceNode(final Identifiable context, final VecField field,
                                                final Identifiable value) {
        return new DefaultRelationshipElement.Builder()
                .idShort(namingStrategy.idShort(value))
                .semanticId(referenceFactory.semanticIdFor(field))
                .description(vecOntology.descriptionFor(field))
                .first(referenceFactory.localReferenceFor(context))
                .second(referenceFactory.localReferenceFor(value))
                .build();
    }

    private SubmodelElement handleReferenceProperty(final Identifiable context, final VecField field,
                                                    final Identifiable value) {
        return new DefaultRelationshipElement.Builder()
                .idShort(namingStrategy.idShort(field))
                .semanticId(referenceFactory.semanticIdFor(field))
                .description(vecOntology.descriptionFor(field))
                .first(referenceFactory.localReferenceFor(context))
                .second(referenceFactory.localReferenceFor(value))
                .build();
    }

    private SubmodelElement handleObjectProperty(final VecField field, final Identifiable value) {
        final DefaultSubmodelElementCollection.Builder builder = new DefaultSubmodelElementCollection.Builder()
                .idShort(namingStrategy.idShort(field))
                .semanticId(referenceFactory.semanticIdFor(field))
                .description(vecOntology.descriptionFor(field))
                .value(handleVecObject(value));
        return builder.build();
    }

    private SubmodelElement handleLiteralProperty(final VecField field, final Object value) {
        final DefaultProperty.Builder propertyBuilder = new DefaultProperty.Builder()
                .idShort(namingStrategy.idShort(field))
                .semanticId(referenceFactory.semanticIdFor(field))
                .description(vecOntology.descriptionFor(field));
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
            entry(Boolean.class, DataTypeDefXsd.BOOLEAN),
            entry(boolean.class, DataTypeDefXsd.BOOLEAN),
            entry(Double.class, DataTypeDefXsd.DOUBLE)
    );

    private UmlField getUmlField(final VecField field) {
        return modelProvider.findField(field.getField());
    }

}
