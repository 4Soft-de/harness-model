package com.foursoft.harness.vec.aas;

import com.foursoft.harness.vec.rdf.common.meta.VecClass;
import com.foursoft.harness.vec.rdf.common.meta.VecField;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXsd;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class SemanticValueAdapter {

    private final Object contextObject;
    private final Map<String, Field> fields;

    protected SemanticValueAdapter(final Object contextObject) {
        this.contextObject = contextObject;
        Objects.requireNonNull(contextObject, "contextObject");

        fields = Arrays.stream(VecClass.analyzeClass(contextObject.getClass()).getFields()).collect(
                Collectors.toMap(VecField::getName, VecField::getField));
    }
    public abstract String getType();

    public abstract String getKey();

    public abstract String getReferenceSystem();

    protected Object getFieldValue(final String fieldName) {
        if (!fields.containsKey(fieldName)) {
            throw new AasConversionException("Cannot find field: " + fieldName);
        }
        try {
            return FieldUtils.readField(fields.get(fieldName), contextObject, true);
        } catch (final IllegalAccessException e) {
            throw new AasConversionException("Access to field " + fieldName + " failed", e);
        }
    }

    public SubmodelElement toProperty(final ReferenceFactory referenceFactory) {
        return new DefaultProperty.Builder()
                .idShort(getReferenceSystem() + "_" + getKey())
                .value(getKey())
                .valueType(DataTypeDefXsd.STRING)
                .valueId(referenceFactory.referenceFor(this))
                .build();
    }
}
