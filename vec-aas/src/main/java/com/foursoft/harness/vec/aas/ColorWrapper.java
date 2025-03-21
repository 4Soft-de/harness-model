package com.foursoft.harness.vec.aas;

import java.util.Set;

public class ColorWrapper extends SemanticValueAdapter {

    private static final Set<String> COLOR_TYPES = Set.of("VecColor");

    private ColorWrapper(final Object contextObject) {
        super(contextObject);
        if (!isColorType(contextObject.getClass())) {
            throw new AasConversionException("Cannot convert context object to a Color type: " + contextObject);
        }
    }

    @Override
    public String getReferenceSystem() {
        return getFieldValue("referenceSystem").toString().toLowerCase();
    }

    @Override
    public String getKey() {
        return getFieldValue("key").toString();
    }

    @Override public String getType() {
        return "Color";
    }

    public static ColorWrapper wrap(final Object contextObject) {
        return new ColorWrapper(contextObject);
    }

    public static boolean isColorType(final Class<?> clazz) {
        return COLOR_TYPES.contains(clazz.getSimpleName());
    }
}
