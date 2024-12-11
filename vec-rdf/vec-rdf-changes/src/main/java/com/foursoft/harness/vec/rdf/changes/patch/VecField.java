package com.foursoft.harness.vec.rdf.changes.patch;

import com.foursoft.harness.vec.rdf.changes.equals.VecDataObjectEquivalence;
import com.foursoft.harness.vec.rdf.changes.equals.VecFieldEquivalence;
import com.google.common.base.Equivalence;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlTransient;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public class VecField {

    private final Field field;

    private final Equivalence<Object> equivalence;

    public VecField(Field field) {
        this.field = field;
        this.equivalence = new VecFieldEquivalence(this);
    }

    public Equivalence<Object> equivalence() {
        return equivalence;
    }

    public Equivalence<Object> elementEquivalence() {
        if (isReference()) {
            return Equivalence.equals();
        }
        if (isVecClass()) {
            return VecDataObjectEquivalence.instance();
        }
        return Equivalence.equals();
    }

    public Field getField() {
        return field;
    }

    @Override
    public String toString() {
        return String.format("VecField [field=%s]", field);
    }

    public boolean isXmlId() {
        return field.isAnnotationPresent(XmlID.class);
    }

    public boolean isTransient() {
        return field.isAnnotationPresent(XmlTransient.class);
    }

    public boolean isReference() {
        return field.isAnnotationPresent(XmlIDREF.class);
    }

    public boolean isCollection() {
        return Collection.class.isAssignableFrom(field.getType());
    }

    public boolean isVecClass() {
        return getValueType().getPackageName()
                .startsWith("com.foursoft.harness.vec");
    }

    public Class<?> getValueType() {
        if (isCollection()) {
            if (field.getGenericType() instanceof ParameterizedType parameterizedType) {
                if (parameterizedType.getActualTypeArguments().length == 1) {
                    Type type = parameterizedType.getActualTypeArguments()[0];
                    if (type instanceof Class<?>) {
                        return (Class<?>) type;
                    }
                }
            }
            throw new UnsupportedOperationException("Unable to extract value type from " + field);
        }
        return field.getType();
    }
}
