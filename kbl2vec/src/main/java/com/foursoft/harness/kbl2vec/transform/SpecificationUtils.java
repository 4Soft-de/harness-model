package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.vec.v2x.VecPartOrUsageRelatedSpecification;

public final class SpecificationUtils {

    private SpecificationUtils() {
        throw new AssertionError("Can't instantiate utility class");
    }

    public static void withIdentification(final VecPartOrUsageRelatedSpecification specification,
                                          final String partNumber) {
        specification.setIdentification(abbreviatedClassName(specification.getClass()) + "-" + partNumber);
    }

    public static String abbreviatedClassName(final Class<?> clazz) {
        return clazz.getSimpleName().replace("Vec", "").replaceAll("[^A-Z]", "");
    }
}
