package de.foursoft.harness.xml.postprocessing;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.foursoft.harness.xml.annotations.XmlBackReference;

public class BackReferencePropertyHandler {

    private final Field sourceField;
    // private final Field destinationField;
    private final boolean isCollection;
    private final String destinationFieldName;
    private final Map<Class<?>, Field> destinationFields = new HashMap<>();

    public BackReferencePropertyHandler(final Field field) {
        if (!field.isAnnotationPresent(XmlBackReference.class)) {
            throw new ModelPostProcessorException(
                    "For the sourceField " + field.getName() + " in " + field.getDeclaringClass()
                            .getName() + " no backreference annotation is present.");
        }
        this.sourceField = field;
        this.sourceField.setAccessible(true);
        this.destinationFieldName = field.getAnnotation(XmlBackReference.class)
                .destinationField();
        // this.destinationField = findDestinationField(field);
        // this.destinationField.setAccessible(true);
        this.isCollection = Collection.class.isAssignableFrom(sourceField.getType());
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
                    "Could not find destination field: " + destinationClass + "." + destinationFieldName);
        }

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

        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new ModelPostProcessorException(e);
        }

    }

    private void addSourceToDestination(final Object sourceObject, final Object destinationObject)
            throws IllegalArgumentException, IllegalAccessException {
        final Set<Object> destinationReference = (Set<Object>) findDestinationField(destinationObject)
                .get(destinationObject);
        destinationReference.add(sourceObject);
    }

    public void handleSingleObjectReference(final Object sourceObject) {
        try {
            final Object destinationObject = sourceField.get(sourceObject);
            if (destinationObject != null) {
                addSourceToDestination(sourceObject, destinationObject);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new ModelPostProcessorException(e);
        }
    }

}
