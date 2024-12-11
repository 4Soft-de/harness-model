package com.foursoft.harness.vec.rdf.changes.patch;

import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.rdf.common.VEC;
import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;
import com.google.common.base.Equivalence;
import com.google.common.base.Predicate;
import org.apache.commons.lang3.ClassUtils;
import org.apache.jena.rdf.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.foursoft.harness.vec.rdf.changes.patch.PatchUtils.isVecProperty;
import static com.foursoft.harness.vec.rdf.changes.patch.VecClassesMetaData.metaData;

public class VecObjectWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(VecObjectWrapper.class);

    private final Identifiable wrappedObject;
    private final MergeContext mergeContext;
    private final RdfToXmlValueStrategy rdfToXmlValueStrategy = new RdfToXmlValueStrategy();
    private final Map<String, PropertyDescriptor> propertyDescriptors;

    public VecObjectWrapper(MergeContext mergeContext, Identifiable wrappedObject) {
        this.mergeContext = mergeContext;
        Objects.requireNonNull(wrappedObject, "wrappedObject must not be null");
        this.wrappedObject = wrappedObject;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(wrappedObject.getClass());
            this.propertyDescriptors = Arrays.stream(beanInfo.getPropertyDescriptors()).collect(
                    Collectors.toMap(PropertyDescriptor::getName, desc -> desc));
        } catch (IntrospectionException e) {
            throw new VecRdfException("Error loading BeanInfo", e);
        }
    }

    public void removeValue(Property property, RDFNode valueNode) {
        if (!isVecProperty(property)) {
            LOGGER.debug("Ignoring property: {}, because it is no VEC native property.", property);
            return;
        }
        LOGGER.info("Removing from: '{}', property: {}, value: {}", wrappedObject, property, valueNode);

        unsetValue(property, valueNode.visitWith(rdfToXmlValueStrategy));
    }

    public void loadValue(Property property, RDFNode valueNode) {
        if (!isVecProperty(property)) {
            LOGGER.debug("Ignoring property: {}, because it is no VEC native property.", property);
            return;
        }
        LOGGER.info("Loading into: {}, property: {}, value: {}", wrappedObject, property, valueNode);

        setValue(property, valueNode.visitWith(rdfToXmlValueStrategy));
    }

    private void unsetValue(Property property, Object oldValue) {
        PropertyDescriptor propertyDescriptor = findPropertyDescriptor(property);
        if (isCollection(propertyDescriptor)) {
            removeCollectionValue(propertyDescriptor, oldValue);
        } else {
            unsetSingleValue(propertyDescriptor, oldValue);
        }
    }

    private void setValue(Property property, Object value) {
        PropertyDescriptor propertyDescriptor = findPropertyDescriptor(property);
        if (isCollection(propertyDescriptor)) {
            addCollectionValue(propertyDescriptor, value);
        } else {
            setSingleValue(propertyDescriptor, value);
        }
    }

    private void removeCollectionValue(PropertyDescriptor propertyDescriptor, Object value) {
        Collection<Object> collection = (Collection<Object>) getPropertyValue(propertyDescriptor);
        Predicate<Object> existencePredicate = elementEquivalence(propertyDescriptor).equivalentTo(value);
        if (collection == null) {
            throw new VecRdfException("Collection of VEC objects must not be null.");
        }
        if (collection.stream()
                .noneMatch(existencePredicate)) {
            throw new MergeConflictException(
                    "Removing value: " + value + " from " + propertyDescriptor.getName() +
                            "is not possible, because it does exist.");
        }
        collection.remove(value);
    }

    private void addCollectionValue(PropertyDescriptor propertyDescriptor, Object value) {
        Collection<Object> collection = (Collection<Object>) getPropertyValue(propertyDescriptor);
        if (collection == null) {
            throw new VecRdfException("Collection of VEC objects must not be null.");
        }
        collection.add(value);
    }

    private void setSingleValue(PropertyDescriptor propertyDescriptor, Object value) {
        Object oldValue = getPropertyValue(propertyDescriptor);
        if (valueIsSet(oldValue)) {
            throw new MergeConflictException(
                    "Overwriting existing value for '" + wrappedObject.toString() + "." + propertyDescriptor.getName() +
                            "': " + oldValue + " <- " + value);
        }
        setPropertyValue(propertyDescriptor, value);
    }

    private void unsetSingleValue(PropertyDescriptor propertyDescriptor, Object value) {
        Object oldValue = getPropertyValue(propertyDescriptor);
        Equivalence<Object> elementEquivalence = elementEquivalence(propertyDescriptor);
        if (!elementEquivalence.equivalent(oldValue, value)) {
            throw new MergeConflictException(
                    "Current value for '" + wrappedObject.toString() + "." + propertyDescriptor.getName() +
                            "' is not the same as removed value:" + oldValue + " <- " + value);
        }
        setPropertyValue(propertyDescriptor, null);
    }

    private static Equivalence<Object> elementEquivalence(PropertyDescriptor propertyDescriptor) {
        return metaData().fieldForPropertyDescriptor(propertyDescriptor)
                .elementEquivalence();
    }

    private Object getPropertyValue(PropertyDescriptor propertyDescriptor) {
        try {
            return propertyDescriptor.getReadMethod().invoke(wrappedObject);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new VecRdfException("Unable to get property value.", e);
        }
    }

    private void setPropertyValue(PropertyDescriptor propertyDescriptor, Object value) {
        try {
            propertyDescriptor.getWriteMethod().invoke(wrappedObject, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new VecRdfException("Unable to set property value.", e);
        }
    }

    //TODO: Handle OPEN Enums.

    private PropertyDescriptor findPropertyDescriptor(Property property) {
        String propertyName = PatchUtils.resolvePropertyName(wrappedObject.getClass(), property);

        if (!propertyDescriptors.containsKey(propertyName)) {
            propertyName = propertyName + "s";
            if (!propertyDescriptors.containsKey(propertyName)) {
                throw new VecRdfException(String.format(
                        "Property: %1$s(s) does not exist on object %2$s, tried singular and plural variant.", property,
                        wrappedObject));
            }
        }
        return propertyDescriptors.get(propertyName);
    }

    private boolean isCollection(PropertyDescriptor propertyDescriptor) {
        return metaData().fieldForPropertyDescriptor(propertyDescriptor)
                .isCollection();
    }

    private static boolean valueIsSet(Object value) {
        if (value == null) {
            return false;
        }
        if (ClassUtils.isPrimitiveWrapper(value.getClass())) {
            Class<?> primitiveClass = ClassUtils.wrapperToPrimitive(value.getClass());
            Object primitiveDefaultValue = getPrimitiveDefaultValue(primitiveClass);
            return !value.equals(primitiveDefaultValue);
        }
        return true;
    }

    private static Object getPrimitiveDefaultValue(Class<?> primitiveClass) {

        if (primitiveClass == boolean.class) {
            return false;
        } else if (primitiveClass == char.class) {
            return '\u0000';
        } else if (primitiveClass == byte.class) {
            return (byte) 0;
        } else if (primitiveClass == short.class) {
            return (short) 0;
        } else if (primitiveClass == int.class) {
            return 0;
        } else if (primitiveClass == long.class) {
            return 0L;
        } else if (primitiveClass == float.class) {
            return 0f;
        } else if (primitiveClass == double.class) {
            return 0.0d;
        }
        return null;
    }

    private Identifiable createVecObjectForBNode(Resource valueNode) {
        Identifiable value = mergeContext.create(valueNode);
        VecObjectWrapper vecObjectWrapper = new VecObjectWrapper(mergeContext, value);
        valueNode.listProperties()
                .forEach(s -> vecObjectWrapper.loadValue(s.getPredicate(), s.getObject()));

        return value;
    }

    private class RdfToXmlValueStrategy implements RDFVisitor {

        @Override
        public Object visitBlank(Resource r, AnonId id) {
            return createVecObjectForBNode(r);
        }

        @Override
        public Object visitURI(Resource r, String uri) {
            if (VEC.isInstanceOf(r, VEC.OpenEnumeration)) {
                String openEnumLiteralValue = VEC.enumLiteralValueFor(r);
                LOGGER.info("Selected '{}' as literal value for {}", openEnumLiteralValue, r);
                return openEnumLiteralValue;
            }
            return mergeContext.getVecObjectForUri(uri);
        }

        @Override
        public Object visitLiteral(Literal l) {
            return l.getValue();
        }

        @Override
        public Object visitStmt(Resource r, Statement statement) {
            throw new VecRdfException("Statement resources are not support for XML mapping.");
        }
    }

}
