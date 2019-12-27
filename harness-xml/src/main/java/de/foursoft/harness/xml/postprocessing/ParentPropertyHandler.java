package de.foursoft.harness.xml.postprocessing;

import java.lang.reflect.Field;

import de.foursoft.harness.xml.annotations.XmlParent;

public class ParentPropertyHandler {

    private final Field field;
    private final Class<?> typeOfField;

    public ParentPropertyHandler(final Field field) {
        if (!field.isAnnotationPresent(XmlParent.class)) {
            throw new ModelPostProcessorException(
                    "For the field " + field.getName() + " in " + field.getDeclaringClass()
                            .getName() + " no parent annotation is present.");
        }
        this.field = field;
        this.field.setAccessible(true);
        typeOfField = field.getType();
    }

    public boolean isHandlingParent(final Object parent) {
        return typeOfField.isInstance(parent);
    }

    public void handleParentProperty(final Object target, final Object parent) {
        try {
            field.set(target, parent);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new ModelPostProcessorException("Can not set parent property value.", e);
        }
    }

}
