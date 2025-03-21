package com.foursoft.harness.vec.aas;

import com.foursoft.harness.vec.rdf.common.meta.VecClass;
import com.foursoft.harness.vec.rdf.common.meta.VecField;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.eclipse.digitaltwin.aas4j.v3.model.LangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringTextType;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class LocalizedStringWrapper {
    private static final Set<String> LOCALIZED_TYPES = Set.of("VecAbstractLocalizedString", "VecLocalizedString",
                                                              "VecLocalizedTypedString");
    private final Object contextObject;
    private final Map<String, Field> fields;

    private LocalizedStringWrapper(final Object contextObject) {
        this.contextObject = contextObject;
        Objects.requireNonNull(contextObject, "contextObject");
        if (!isLocalizedType(contextObject.getClass())) {
            throw new AasConversionException("Cannot convert context object to a LocalizedString: " + contextObject);
        }

        fields = Arrays.stream(VecClass.analyzeClass(contextObject.getClass()).getFields()).collect(
                Collectors.toMap(VecField::getName, VecField::getField));
    }

    public String getLanguage() {
        return getFieldValue("languageCode").toString().toLowerCase();
    }

    public String getValue() {
        return getFieldValue("value").toString();
    }

    public String getType() {
        if (fields.containsKey("type")) {
            return getFieldValue("type").toString();
        }
        return "";
    }

    public LangStringTextType toLangStringTextType() {
        return new DefaultLangStringTextType.Builder()
                .language(getLanguage())
                .text(getValue())
                .build();
    }

    private Object getFieldValue(final String fieldName) {
        if (!fields.containsKey(fieldName)) {
            throw new AasConversionException("Cannot find field: " + fieldName);
        }
        try {
            return FieldUtils.readField(fields.get(fieldName), contextObject, true);
        } catch (final IllegalAccessException e) {
            throw new AasConversionException("Access to field " + fieldName + " failed", e);
        }
    }

    public static LocalizedStringWrapper wrap(final Object contextObject) {
        return new LocalizedStringWrapper(contextObject);
    }

    public static boolean isLocalizedType(final Class<?> clazz) {
        return LOCALIZED_TYPES.contains(clazz.getSimpleName());
    }

}
