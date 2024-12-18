/*-
 * ========================LICENSE_START=================================
 * VEC RDF Changesets (Experimental)
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
package com.foursoft.harness.vec.rdf.changes.patch;

import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.rdf.changes.equivalences.jaxb.VecFieldEquivalence;
import com.foursoft.harness.vec.rdf.changes.equivalences.jaxb.VecValueObjectEquivalence;
import com.foursoft.harness.vec.rdf.common.VEC;
import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;
import com.foursoft.harness.vec.rdf.common.meta.VecClass;
import com.google.common.base.Equivalence;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.foursoft.harness.vec.rdf.changes.patch.PatchUtils.isVecProperty;

public class VecObjectWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(VecObjectWrapper.class);

    private final Identifiable wrappedObject;
    private final MergeContext mergeContext;
    private final RdfToXmlValueStrategy rdfToXmlValueStrategy = new RdfToXmlValueStrategy();
    private final Map<String, PropertyDescriptor> propertyDescriptors;
    private final VecClass vecClass;

    public VecObjectWrapper(final MergeContext mergeContext, final Identifiable wrappedObject) {
        this.mergeContext = mergeContext;
        Objects.requireNonNull(wrappedObject, "wrappedObject must not be null");
        this.wrappedObject = wrappedObject;
        this.vecClass = VecClass.analyzeClass(wrappedObject.getClass());
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(wrappedObject.getClass());
            this.propertyDescriptors = Arrays.stream(beanInfo.getPropertyDescriptors()).collect(
                    Collectors.toMap(PropertyDescriptor::getName, desc -> desc));
        } catch (final IntrospectionException e) {
            throw new VecRdfException("Error loading BeanInfo", e);
        }
    }

    public void removeValue(final Property property, final RDFNode valueNode) {
        if (!isVecProperty(property)) {
            LOGGER.debug("Ignoring property: {}, because it is no VEC native property.", property);
            return;
        }
        LOGGER.info("Removing from: '{}', property: {}, value: {}", wrappedObject, property, valueNode);

        unsetValue(property, valueNode.visitWith(rdfToXmlValueStrategy));
    }

    public void loadValue(final Property property, final RDFNode valueNode) {
        if (!isVecProperty(property)) {
            LOGGER.debug("Ignoring property: {}, because it is no VEC native property.", property);
            return;
        }
        LOGGER.info("Loading into: {}, property: {}, value: {}", wrappedObject, property, valueNode);

        setValue(property, valueNode.visitWith(rdfToXmlValueStrategy));
    }

    private void unsetValue(final Property property, final Object oldValue) {
        final PropertyDescriptor propertyDescriptor = findPropertyDescriptor(property);
        if (isCollection(propertyDescriptor)) {
            removeCollectionValue(propertyDescriptor, oldValue);
        } else {
            unsetSingleValue(propertyDescriptor, oldValue);
        }
    }

    private void setValue(final Property property, final Object value) {
        final PropertyDescriptor propertyDescriptor = findPropertyDescriptor(property);
        if (isCollection(propertyDescriptor)) {
            addCollectionValue(propertyDescriptor, value);
        } else {
            setSingleValue(propertyDescriptor, value);
        }
    }

    private void removeCollectionValue(final PropertyDescriptor propertyDescriptor, final Object value) {
        final Collection<Object> collection = (Collection<Object>) getPropertyValue(propertyDescriptor);
        final Predicate<Object> existencePredicate = elementEquivalence(propertyDescriptor).equivalentTo(value);
        if (collection == null) {
            throw new VecRdfException("Collection of VEC objects must not be null.");
        }
        final Optional<Object> currentValue = collection.stream()
                .filter(existencePredicate)
                .findAny();

        collection.remove(currentValue.orElseThrow(() -> new MergeConflictException(
                "Removing value: " + value + " from " + propertyDescriptor.getName() +
                        "is not possible, because it does exist.")));
    }

    private void addCollectionValue(final PropertyDescriptor propertyDescriptor, final Object value) {
        final Collection<Object> collection = (Collection<Object>) getPropertyValue(propertyDescriptor);
        if (collection == null) {
            throw new VecRdfException("Collection of VEC objects must not be null.");
        }
        collection.add(value);
    }

    private void setSingleValue(final PropertyDescriptor propertyDescriptor, final Object value) {
        final Object oldValue = getPropertyValue(propertyDescriptor);
        if (valueIsSet(oldValue)) {
            throw new MergeConflictException(
                    "Overwriting existing value for '" + wrappedObject.toString() + "." + propertyDescriptor.getName() +
                            "': " + oldValue + " <- " + value);
        }
        setPropertyValue(propertyDescriptor, value);
    }

    private void unsetSingleValue(final PropertyDescriptor propertyDescriptor, final Object value) {
        final Object oldValue = getPropertyValue(propertyDescriptor);
        final Equivalence<Object> elementEquivalence = elementEquivalence(propertyDescriptor);
        if (!elementEquivalence.equivalent(oldValue, value)) {
            throw new MergeConflictException(
                    "Current value for '" + wrappedObject.toString() + "." + propertyDescriptor.getName() +
                            "' is not the same as removed value:" + oldValue + " <- " + value);
        }
        setPropertyValue(propertyDescriptor, null);
    }

    private Equivalence<Object> elementEquivalence(final PropertyDescriptor propertyDescriptor) {
        return VecFieldEquivalence.elementEquivalence(this.vecClass.getField(propertyDescriptor),
                                                      VecValueObjectEquivalence.instance());
    }

    private Object getPropertyValue(final PropertyDescriptor propertyDescriptor) {
        try {
            return propertyDescriptor.getReadMethod().invoke(wrappedObject);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            throw new VecRdfException("Unable to get property value.", e);
        }
    }

    private void setPropertyValue(final PropertyDescriptor propertyDescriptor, final Object value) {
        try {
            propertyDescriptor.getWriteMethod().invoke(wrappedObject, value);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            throw new VecRdfException("Unable to set property value.", e);
        }
    }

    private PropertyDescriptor findPropertyDescriptor(final Property property) {
        final String propertyName = PatchUtils.resolvePropertyName(wrappedObject.getClass(), property);

        PropertyDescriptor descriptor = getPropertyDescriptor(propertyName);

        if (descriptor == null) {
            descriptor = getPropertyDescriptor(propertyName + "s");
            if (descriptor == null) {
                throw new VecRdfException(String.format(
                        "Property: %1$s(s) does not exist on object %2$s, tried singular and plural variant.", property,
                        wrappedObject));
            }
        }
        return descriptor;
    }

    PropertyDescriptor getPropertyDescriptor(final String name) {
        PropertyDescriptor pd = this.propertyDescriptors.get(name);
        if (pd == null && StringUtils.isNotEmpty(name)) {
            // Same lenient fallback checking as in Property...
            pd = this.propertyDescriptors.get(StringUtils.uncapitalize(name));
            if (pd == null) {
                pd = this.propertyDescriptors.get(StringUtils.capitalize(name));
            }
        }
        return pd;
    }

    private boolean isCollection(final PropertyDescriptor propertyDescriptor) {
        return vecClass.getField(propertyDescriptor).isCollection();
    }

    private static boolean valueIsSet(final Object value) {
        if (value == null) {
            return false;
        }
        if (ClassUtils.isPrimitiveWrapper(value.getClass())) {
            final Class<?> primitiveClass = ClassUtils.wrapperToPrimitive(value.getClass());
            final Object primitiveDefaultValue = getPrimitiveDefaultValue(primitiveClass);
            return !value.equals(primitiveDefaultValue);
        }
        return true;
    }

    private static Object getPrimitiveDefaultValue(final Class<?> primitiveClass) {

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

    private class RdfToXmlValueStrategy implements RDFVisitor {

        private Identifiable createVecObjectForBNode(final Resource valueNode) {
            final Identifiable value = mergeContext.create(valueNode);
            final VecObjectWrapper vecObjectWrapper = new VecObjectWrapper(mergeContext, value);
            valueNode.listProperties()
                    .forEach(s -> vecObjectWrapper.loadValue(s.getPredicate(), s.getObject()));

            return value;
        }

        @Override
        public Object visitBlank(final Resource r, final AnonId id) {
            return createVecObjectForBNode(r);
        }

        @Override
        public Object visitURI(final Resource r, final String uri) {
            if (VEC.isInstanceOf(r, VEC.OpenEnumeration)) {
                final String openEnumLiteralValue = VEC.enumLiteralValueFor(r);
                LOGGER.info("Selected '{}' as literal value for {}", openEnumLiteralValue, r);
                return openEnumLiteralValue;
            }
            return mergeContext.getVecObjectForUri(uri);
        }

        @Override
        public Object visitLiteral(final Literal l) {
            return l.getValue();
        }

        @Override
        public Object visitStmt(final Resource r, final Statement statement) {
            throw new VecRdfException("Statement resources are not support for XML mapping.");
        }
    }

}
