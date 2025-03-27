package com.foursoft.harness.vec.aas.wrapper;

import com.foursoft.harness.vec.aas.AasConversionException;
import com.foursoft.harness.vec.rdf.common.meta.VecClass;
import com.foursoft.harness.vec.rdf.common.meta.VecField;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class GenericVecObjectWrapper {

    private final Object contextObject;
    private final Map<String, Field> fields;

    protected GenericVecObjectWrapper(final Object contextObject) {
        this.contextObject = contextObject;
        Objects.requireNonNull(contextObject, "contextObject");

        fields = Arrays.stream(VecClass.analyzeClass(contextObject.getClass()).getFields()).collect(
                Collectors.toMap(VecField::getName, VecField::getField));
    }

    protected Map<String, Field> getFields() {
        return fields;
    }

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
}
