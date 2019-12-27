package de.foursoft.harness.xml.postprocessing;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import de.foursoft.harness.xml.annotations.XmlBackReference;
import de.foursoft.harness.xml.annotations.XmlParent;

public class ReflectiveAssociationPostProcessor implements ModelPostProcessor {

    private final Class<?> classToHandle;
    private final List<ParentPropertyHandler> parentProperties = new LinkedList<>();
    private final List<BackReferencePropertyHandler> backReferenceProperties = new LinkedList<>();

    public ReflectiveAssociationPostProcessor(final Class<?> classToHandle) {
        this.classToHandle = classToHandle;
        for (final Field f : classToHandle.getDeclaredFields()) {
            if (f.isAnnotationPresent(XmlParent.class)) {
                parentProperties.add(new ParentPropertyHandler(f));
            }
            if (f.isAnnotationPresent(XmlBackReference.class)) {
                backReferenceProperties.add(new BackReferencePropertyHandler(f));
            }
        }
    }

    @Override
    public Class<?> getClassToHandle() {
        return classToHandle;
    }

    @Override
    public void afterUnmarshalling(final Object target, final Object parent) {
        for (final ParentPropertyHandler h : parentProperties) {
            if (h.isHandlingParent(parent)) {
                h.handleParentProperty(target, parent);
            }
        }
    }

    @Override
    public void afterUnmarshallingCompleted(final Object target) {
        for (final BackReferencePropertyHandler h : backReferenceProperties) {
            h.handleObject(target);
        }
    }

    @Override
    public void clearState() {
        // No state stored during unmarshalling.
    }

}
