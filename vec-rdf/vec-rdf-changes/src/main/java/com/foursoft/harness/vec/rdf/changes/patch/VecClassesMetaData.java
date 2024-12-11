package com.foursoft.harness.vec.rdf.changes.patch;

import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Stream;

public class VecClassesMetaData {

    private static final VecClassesMetaData INSTANCE = new VecClassesMetaData();

    private VecClassesMetaData() {
        // HIDE
    }

    public static VecClassesMetaData metaData() {
        return INSTANCE;
    }

    private final Map<Class<?>, VecField[]> classFields = new HashMap<Class<?>, VecField[]>();

    public VecField[] fieldsForClass(final Class<?> clazz) {
        return classFields.computeIfAbsent(clazz, this::computeFieldsForClass);
    }

    private VecField[] computeFieldsForClass(final Class<?> clazz) {
        Class<?> currentClass = clazz;
        List<VecField> vecFields = new ArrayList<>();
        while (currentClass != null) {
            Stream.of(currentClass.getDeclaredFields())
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .map(VecField::new)
                    .forEach(vecFields::add);

            currentClass = currentClass.getSuperclass();
        }
        return vecFields.toArray(new VecField[0]);
    }

    public VecField fieldForPropertyDescriptor(PropertyDescriptor propertyDescriptor) {
        return Arrays.stream(fieldsForClass(propertyDescriptor.getReadMethod()
                                                    .getDeclaringClass()))
                .filter(vecField -> vecField.getField()
                        .getName()
                        .equals(propertyDescriptor.getName()))
                .findAny()
                .orElseThrow(() -> new VecRdfException("No VEC field descriptor found for " + propertyDescriptor));
    }

}
