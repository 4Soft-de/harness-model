/*-
 * ========================LICENSE_START=================================
 * xml-runtime
 * %%
 * Copyright (C) 2019 4Soft GmbH
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
package com.foursoft.jaxb.navext.runtime.postprocessing;

import com.foursoft.jaxb.navext.runtime.annotations.XmlBackReference;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BackReferencePropertyHandler {

    private final Field sourceField;
    private final boolean isCollection;
    private final String destinationFieldName;
    private final Map<Class<?>, Field> destinationFields = new HashMap<>();

    public BackReferencePropertyHandler(final Field field) {
        if (!field.isAnnotationPresent(XmlBackReference.class)) {
            throw new ModelPostProcessorException(
                    "For the sourceField " + field.getName() + " in " + field.getDeclaringClass()
                            .getName() + " no backreference annotation is present.");
        }
        sourceField = field;
        sourceField.setAccessible(true);
        destinationFieldName = field.getAnnotation(XmlBackReference.class)
                .destinationField();
        isCollection = Collection.class.isAssignableFrom(sourceField.getType());
    }

    public void handleObject(final Object source) {
        try {
            if (isCollection) {
                handleCollection(source);
            } else {
                handleSingleObjectReference(source);
            }
        } catch (final ModelPostProcessorException e) {
            throw new ModelPostProcessorException("An error occurred during the backreference handling of field: "
                    + sourceField.getName() + " for object: " + source, e);
        }
    }

    public void handleCollection(final Object sourceObject) {
        try {
            final Collection<?> sourceCollection = (Collection<?>) sourceField.get(sourceObject);
            if (sourceCollection != null) {
                for (final Object destinationObject : sourceCollection) {
                    addSourceToDestination(sourceObject, destinationObject);
                }
            }
        } catch (final IllegalArgumentException | IllegalAccessException e) {
            throw new ModelPostProcessorException(e);
        }
    }

    public void handleSingleObjectReference(final Object sourceObject) {
        try {
            final Object destinationObject = sourceField.get(sourceObject);
            if (destinationObject != null) {
                addSourceToDestination(sourceObject, destinationObject);
            }
        } catch (final IllegalArgumentException | IllegalAccessException e) {
            throw new ModelPostProcessorException(e);
        }
    }

    private Field findDestinationField(final Object destinationObject) {
        try {
            return destinationFields.computeIfAbsent(destinationObject.getClass(), this::computeDestinationField);
        } catch (final ModelPostProcessorException e) {
            throw new ModelPostProcessorException(
                    "Could not find destination field " + destinationFieldName + " for object " + destinationObject, e);
        }
    }

    private Field computeDestinationField(final Class<?> destinationClass) {
        try {
            final Field result = destinationClass.getDeclaredField(destinationFieldName);
            result.setAccessible(true);
            return result;
        } catch (final SecurityException e) {
            throw new ModelPostProcessorException(e);
        } catch (final NoSuchFieldException e) {
            if (destinationClass.getSuperclass() != null) {
                return computeDestinationField(destinationClass.getSuperclass());
            }
            throw new ModelPostProcessorException(
                    "Could not find destination field: " + destinationClass + '.' + destinationFieldName, e);
        }

    }

    private void addSourceToDestination(final Object sourceObject, final Object destinationObject)
            throws IllegalAccessException {
        final Set<Object> destinationReference = (Set<Object>) findDestinationField(destinationObject)
                .get(destinationObject);
        destinationReference.add(sourceObject);
    }

}
