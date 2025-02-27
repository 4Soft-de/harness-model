package com.foursoft.harness.kbl2vec.utils;

import com.foursoft.harness.kbl2vec.core.NoMappingDefinedException;

public class EnumMapper {

    public static <D extends Enum<D>> D mapEnum(final Enum<?> source, final Class<D> enumClass) {
        final String name = source.name();
        final D[] literals = enumClass.getEnumConstants();
        for (final D literal : literals) {
            if (name.equalsIgnoreCase(literal.name())) {
                return literal;
            }
        }
        throw new NoMappingDefinedException("No mapping defined for " + enumClass.getSimpleName() + ": " + name);
    }
}
